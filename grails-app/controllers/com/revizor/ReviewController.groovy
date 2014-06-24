package com.revizor

import static org.springframework.http.HttpStatus.*
import org.junit.internal.runners.statements.FailOnTimeout;
import grails.transaction.Transactional
import com.revizor.utils.Constants;
import com.revizor.ReviwerStatus;
import com.revizor.ReviewStatus;
import com.revizor.Action
import revizor.HelpTagLib;

@Transactional(readOnly = true)
class ReviewController {

    def notificationService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        def list
        def reviewFilter = params.filter as ReviewFilter
        switch(reviewFilter) {
            
            case ReviewFilter.ALL:
                list = Review.list(params);
                break;

            case ReviewFilter.ONLY_MINE:
                list = Review.findAllByAuthorAndStatus(session.user, ReviewStatus.OPEN);
                break;

            case ReviewFilter.WHERE_I_AM_REVIEWER:
                list = Review.findAllByAuthor(session.user);
                break;

            case ReviewFilter.ARCHIVED:
                list = Review.findAllByAuthorAndStatus(session.user, ReviewStatus.CLOSED);
                break;

            default:
                list = Review.list(params);
                break;                
        }

        def repos = Repository.list()
        respond list, model:[reviewInstanceCount: Review.count(), reviewFilter: reviewFilter, repos: repos]
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
		
        respond reviewInstance, view: view, model:[fileName: params[Constants.PARAM_FILE_NAME]]
    }

    def create() {
		if (!params.id) {
            flash.error = "${message(code: 'controller.review.params.id.not.specified')}"
            redirect(action: "index")
        }
        else {
            //respond new Review(params)
            //def reviewInstance = new Review(params)
            //reviewInstance.properties = params
            respond new Review(params), model:[repository: Repository.read(params.id)]
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
            
            notificationService.create(session.user, Action.REVIEW_INVITED_REVIEWER, [session.user, reviewer, review])

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
        def isFirstResolution = (null == reviewerObj.status)
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
        def r = reviewInstance.save(flush:true)
		
        notificationService.create(session.user, Action.REVIEW_START, [session.user, r])

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'reviewInstance.label', default: 'Review'), reviewInstance.id])
                redirect reviewInstance
            }
            '*' { respond reviewInstance, repository, [status: CREATED] }
        }
    }

    def edit(Review reviewInstance) {
        respond reviewInstance
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

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Review.label', default: 'Review'), reviewInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'reviewInstance.label', default: 'Review'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
	
}


enum ReviewFilter {
    ALL("reviews.all"),
    ONLY_MINE("reviews.only.mine"),
    WHERE_I_AM_REVIEWER("reviews.where.i.reviewer"),
    ARCHIVED("reviews.archived"),

    ReviewFilter(String value) { this.value = value }

    private final String value
    public String value() { return value }
}