package com.revizor

import com.revizor.issuetracker.ITracker
import com.revizor.repos.Commit
import com.revizor.utils.Constants
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import revizor.Alias
import org.springframework.context.i18n.LocaleContextHolder as LCH


class ReviewService {

    Log log = LogFactory.getLog(this.class)
    def notificationService
    def groovyPageRenderer
    def messageSource

    /**
     * Runs through the given list of commits, and if it detects Smart Commits,
     * then it creates new review for each of them
     *
     * @param lstCommits
     * @return
     */
    def checkNewRevisionsForSmartCommits(List<Commit> lstCommits, Repository repository) {
        lstCommits.each { Commit commit ->

            if (commit.fullMessage.contains(Constants.SMART_COMMIT_CREATE_REVIEW)) {
                // smart commits detected
                Review review = this.createReviewFromSmartCommit(repository, commit)
                if (review) {
                    review.save(flush:true, failOnError: true)

                    // show notification about new review
                    notificationService.create(review.author, Action.REVIEW_START, [review.author, review])

                    // send email to author
                    Object[] args = [review.title] as Object[]
                    def bodyText = messageSource.getMessage("notification.subject.review.was.create.smart.commit", args, LCH.getLocale())
                    def header = messageSource.getMessage("notification.subject", null, LCH.getLocale())
                    notificationService.sendPlainEmail(header, review.author.email, bodyText)

                    // send email to reviewers
                    review.reviewers.each { Reviewer reviewer ->
                        Object[] argsReviewer = [review.author.username, review.title] as Object[]
                        def bodyTextReviewer = messageSource.getMessage("notification.subject.you.were.invited", argsReviewer, LCH.getLocale())
                        notificationService.sendPlainEmail(header, reviewer.reviewer.email, bodyTextReviewer)
                    }

                    // notify Issue Tracker(s) that we created a review
                    review.getIssueTickets().each { Issue issue ->
                        ITracker issueTracker = issue.tracker.initImplementation();
                        issueTracker.before()
                        issueTracker.notifyTrackerReviewCreated(issue.key, review)
                    }
                }
            }
        }
    }

    /**
     * Parses a commit message for a smart commit commands and
     * creates proper review
     *
     * @param message
     */
    def createReviewFromSmartCommit(repo, Commit commit) {

        User user = _getUserByEmailOrAlias(commit.authorEmail)

        if(user) {
            def arr = getHeaderAndMessage(commit)

            Review review = new Review(author: user,
                    title: arr[0],
                    description: arr[1],
                    commits: [commit.id],
                    status: ReviewStatus.OPEN,
                    repository: repo,
                    smartCommitId: commit.id)

            arr[2].each {reviewerEmail ->
                User reviewerUser = _getUserByEmailOrAlias(reviewerEmail)
                if (reviewerUser) {
                    review.addToReviewers(new Reviewer(reviewer: reviewerUser, status: ReviwerStatus.INVITED))
                }
            }

            // find all issue tickets in the commit message and associate them with current review
            getIssueTickets(commit, review).each { issueTicket ->
                review.addToIssueTickets(issueTicket)
            }

            return review
        }
        else {
            log.warn("Were asked to create a new review, but the author ${commit.author} was not found in the Revizor database, thus " +
                    "I don't know who is the author. The review was not created.")
            return null
        }
    }

    private User _getUserByEmailOrAlias(String strEmail) {
        def user = User.findByEmail(strEmail)
        if (!user) {
            def alias = Alias.findByAliasEmail(strEmail)
            user = alias?.user
        }
        return user
    }

    /**
     * Extracts header and message from commit message. Expected the first line
     * without "+review" smart commit tag
     *
     * @see https://github.com/w32blaster/revizor/wiki/Smart-commits for more details
     *
     * @param commit
     * @return array:
     *  [0] - extracted header
     *  [1] - extracted message (or empty string in case of commit has only one line)
     *  [2] - list of reviewers
     */
    def getHeaderAndMessage(Commit commit) {
        def lines = commit.fullMessage.readLines()
        def header = lines[0].contains(Constants.SMART_COMMIT_CREATE_REVIEW) ?
                lines[0].substring(0, lines[0].indexOf(Constants.SMART_COMMIT_CREATE_REVIEW)).trim()
                : lines[0];

        def message = (lines.size() == 1) ? "no description" : lines[1.. -1]
                                                    .join("\n")
                                                    .trim()
                                                    .replace(Constants.SMART_COMMIT_CREATE_REVIEW, "")

        // list of reviewers. Expected format: "+review one@strEmail.com,two@strEmail.com[end of line or end of message]"
        int since = commit.fullMessage.indexOf(Constants.SMART_COMMIT_CREATE_REVIEW)
        def until = commit.fullMessage.indexOf("\n", since)
        if (until == -1) until = commit.fullMessage.length()

        def argumentsAfterReviewTag = commit.fullMessage.substring(since, until)
        def emails = argumentsAfterReviewTag.findAll( /[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+/ )

        return [header, message, emails]
    }

    /**
     *
     * Finds all the issues added to a commit message as a Smart Commit.
     * Expected, that all the issue keys started with #-symbol.
     *
     * For example, in the commit message:
     *    "add new feature #TROLOLO-37"
     * current method will find ticket with key TROLOLO-37
     *
     * @param commit
     * @return
     */
    def getIssueTickets(Commit commit, Review review) {
        def issues = []
        IssueTracker.all.each { issueTracker ->

            def pattern = ~/#{1}${issueTracker.issueKeyPattern}($|\s+)/

            commit.fullMessage.findAll(pattern).each { foundKey ->
                issues << new Issue(
                        key: foundKey.replace('#','').trim(),
                        tracker: issueTracker,
                        review: review)
            }
        }

        return issues
    }

}
