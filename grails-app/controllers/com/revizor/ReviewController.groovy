package com.revizor

import com.revizor.chats.IChat
import com.revizor.issuetracker.ITracker
import com.revizor.utils.Constants
import grails.async.Promises
import grails.transaction.Transactional
import org.apache.commons.logging.LogFactory
import revizor.HelpTagLib

import static org.springframework.http.HttpStatus.*
import static grails.async.Promises.*

enum ReviewFilter {
    ALL("reviews.all"),
    ONLY_MINE("reviews.only.mine"),
    WHERE_I_AM_REVIEWER("reviews.where.i.reviewer"),
    ARCHIVED("reviews.archived"),
    GROUPED_BY_ISSUE_TICKETS("reviews.grouped.by.issue.tickets");

    ReviewFilter(String value) { this.value = value }

    private final String value
    public String value() { return value }

    public static msgCodeFromValue(String value) {
        switch (value) {

            case ReviewFilter.ALL.name():
                return ReviewFilter.ALL.value()

            case ReviewFilter.ONLY_MINE.name():
                return ReviewFilter.ONLY_MINE.value()

            case ReviewFilter.WHERE_I_AM_REVIEWER.name():
                return ReviewFilter.WHERE_I_AM_REVIEWER.value()

            case ReviewFilter.GROUPED_BY_ISSUE_TICKETS.name():
                return "reviews.grouped.by.issue.tickets.full"

            case ReviewFilter.ARCHIVED.name():
                return ReviewFilter.ARCHIVED.value()

            default:
                return ""
        }
    }
}


@Transactional(readOnly = true)
class ReviewController {

    def notificationService
    def log = LogFactory.getLog(this.class)

    static allowedMethods = [save: "POST", update: "PUT"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        def list
        def reviewFilter = params.filter as ReviewFilter
        def groupedIssues = []
        switch(reviewFilter) {

            case ReviewFilter.ALL:
                list = Review.list(params);
                break;

            case ReviewFilter.ONLY_MINE:
                list = Review.findAllByAuthorAndStatus(session.user, ReviewStatus.OPEN,
                        [sort: "id", order: "desc"]);
                break;

            case ReviewFilter.WHERE_I_AM_REVIEWER:
                list = Reviewer.findAllByReviewer(session.user, [sort: "id", order: "desc"]).collect { it.review };
                break;

            case ReviewFilter.ARCHIVED:
                list = Review.findAllByAuthorAndStatus(session.user, ReviewStatus.CLOSED,
                        [sort: "id", order: "desc"]);
                break;

            case ReviewFilter.GROUPED_BY_ISSUE_TICKETS:
                groupedIssues = Issue.findAll().groupBy { it.key }
                def reviewsWithIssues = Issue.findAll().collect { it.review }
                list = Review.list(params).findAll{ !reviewsWithIssues.contains(it) }
                break;

            default:
                list = Review.list(params);
                break;
        }

        // new reviews, that we haven't read yet
        def unreadReviewIds = notificationService.getNewUnreadItemsForMe(ObjectType.REVIEW, session.user)
        def repos = Repository.list()

        respond list, model:[
                reviewInstanceCount: Review.count(),
                reviewFilter: reviewFilter,
                repos: repos,
                groupedIssues: groupedIssues,
                unreadReviews: unreadReviewIds]
    }

    def show(Review reviewInstance) {
        def view;

        switch (params.viewType) {

            case Constants.REVIEW_SINGLE_VIEW:
                view = "showSingleView";
                break

            case Constants.REVIEW_SIDE_BY_SIDE_VIEW:
                view = "showSideBySideView";
                break;

            default:
                view = "show";
                break;
        }

        // mark "unread" the event about this review for current user
        notificationService.markReadEvent(ObjectType.REVIEW, session.user)

        respond reviewInstance, view: view, model:[
                fileName: params[Constants.PARAM_FILE_NAME],
                urlPrefix: g.createLink(controller: 'review', action: 'show', id: reviewInstance.ident())
        ]
    }

    def create() {
        if (!params.id) {
            flash.error = "${message(code: 'controller.review.params.id.not.specified')}"
            redirect(action: "index")
        }
        else {
            respond new Review(params), model:[repository: Repository.read(params.id), isEdit: false]
        }
    }

    @Transactional
    def close() {
        if (!params.review) {
            render status: NO_CONTENT
            return
        }

        def review = Review.get(params.review);
        if (!review) {
            render status: NOT_FOUND
            return
        }

        review.status = ReviewStatus.CLOSED;
        review.save(flush:true);

        if (review.hasErrors()) {
            render status: 500
        }
        else {
            notificationService.create(session.user, Action.REVIEW_CLOSE, [session.user, review])

            // notify Issue Tracker(s) that user just closed a review
            def taskNotifyIssueTracker = task {
                review.getIssueTickets().each { Issue issue ->
                    ITracker issueTracker = issue.tracker.initImplementation();
                    issueTracker.before()
                    issueTracker.notifyTrackerReviewClosed(issue.key, review)
                }
            }

            // notify chats that user just closed a review
            def taskNotifyChat = task {
                Chat.getAll().each { Chat chat ->
                    IChat chatImpl = chat.initImplementation();
                    chatImpl.before();
                    chatImpl.notifyReviewClosed(review)
                }
            }

            /*
                Communication with Issue Tracker and Chats is a network call
                and potentially it is expensive operation.
                Let's execute it in parallel.
             */
            Promises.onComplete([taskNotifyIssueTracker, taskNotifyChat]) { List lst ->
                log.info("Two tasks were executed in parallel. $lst")
            }

            render status: OK
        }
    }

    @Transactional
    def inviteReviewer() {
        if (!params.review || !params.user) {
            render status: NO_CONTENT
            return
        }

        def review = Review.get(params.review);
        if (!review) {
            render status: NOT_FOUND
            return
        }

        def reviewer = User.get(params.user);
        if (!reviewer) {
            render status: NOT_FOUND
            return
        }

        if (review.findReviewerByUser(reviewer)) {
            render(status: 400, text: 'this user is already assigned as a reviewer to this review')
            return
        }

        def reviewerObj = new Reviewer(reviewer: reviewer, status: ReviwerStatus.INVITED);
        review.addToReviewers(reviewerObj);
        review.save(flush:true);

        if (review.hasErrors()){
            render status: 500
        }
        else {

            def notification = notificationService.create(session.user, Action.REVIEW_INVITED_REVIEWER, [session.user, reviewer, review])

            def header = message(code: "notification.subject.you.were.invited", args: [review.author.username, review.title])
            notificationService.sendNotificationViaEmail(notification, header, null)

            def htmlToRender = g.render(template: '/review/reviewer' , model: [
                    'reviewer': reviewer,
                    'status' : ReviwerStatus.INVITED])

            render HelpTagLib.toSingleLine(htmlToRender)
        }
    }

    @Transactional
    def resolve() {
        if (!params.review || !params.status) {
            render status: NO_CONTENT
            return
        }

        def review = Review.get(params.review);
        if (!review) {
            render status: NOT_FOUND
            return
        }

        def reviewStatus = ReviwerStatus.valueOfName(params.status);
        if (!reviewStatus) {
            render status: NOT_FOUND
            return
        }

        def reviewerObj = review.findReviewerByUser(session.user) ?: new Reviewer(reviewer: session.user);
        def isFirstResolution = (null == reviewerObj.status || reviewerObj.status == ReviwerStatus.INVITED)
        def oldStatus = isFirstResolution ? ReviwerStatus.INVITED : reviewerObj.status;

        reviewerObj.status = reviewStatus;
        review.addToReviewers(reviewerObj);
        review.save(flush:true);

        if (review.hasErrors()){
            render status: 500
        }
        else {

            if (isFirstResolution) {
                notificationService.create(session.user, Action.REVIEW_FINISH_WITH_DECISION, [session.user, review, reviewStatus.getThumbIconStyle()])
            }
            else {
                notificationService.create(session.user, Action.REVIEW_REVIEWER_CHANGED_HIS_MIND, [session.user, review, oldStatus.getThumbIconStyle(), reviewStatus.getThumbIconStyle()])
            }

            render status: OK
        }
    }

    @Transactional
    def save(Review reviewInstance) {
        if (reviewInstance == null) {
            notFound()
            return
        }

        if (!reviewInstance.validate()) {
            respond reviewInstance.errors, view:'create'
            return
        }

        def repository = Repository.read(params.repository)
        reviewInstance.setRepository(repository)
        def review = reviewInstance.save(flush:true)

        // create new notification about that event...
        def n = notificationService.create(session.user, Action.REVIEW_START, [session.user, review])
        // ...and mark for all the users, that this event is unread yet
        notificationService.makeUnreadEventsForAllUsers(n, ObjectType.REVIEW, review.ident())

        // notify chats
        Chat.getAll().each { Chat chat ->
            IChat chatImpl = chat.initImplementation();
            chatImpl.notifyReviewStarted(review)
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'reviewInstance.label', default: 'Review'), reviewInstance.id])
                redirect reviewInstance
            }
            '*' { respond reviewInstance, repository, [status: CREATED] }
        }
    }

    def edit(Review reviewInstance) {
        respond reviewInstance, model:[repository: reviewInstance.repository, isEdit: true]
    }

    @Transactional
    def update(Review reviewInstance) {
        if (reviewInstance == null) {
            notFound()
            return
        }

        if (reviewInstance.hasErrors()) {
            respond reviewInstance.errors, view:'edit'
            return
        }

        reviewInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Review.label', default: 'Review'), reviewInstance.id])
                redirect reviewInstance
            }
            '*'{ respond reviewInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(Review reviewInstance) {

        if (reviewInstance == null) {
            notFound()
            return
        }

        reviewInstance.delete flush:true

        flash.message = message(code: 'default.deleted.message', args: [message(code: 'Review.label', default: 'Review'), reviewInstance.title])
        redirect action:"index", method:"GET", params: [filter: ReviewFilter.ONLY_MINE]
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'Review.label', default: 'Review'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }

}
