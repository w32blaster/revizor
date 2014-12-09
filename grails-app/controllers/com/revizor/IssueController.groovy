package com.revizor

import com.revizor.issuetracker.ITracker
import com.revizor.issuetracker.IssueTicket
import grails.converters.JSON
import grails.transaction.Transactional
import revizor.HelpTagLib

import static org.springframework.http.HttpStatus.*

@Transactional(readOnly = true)
class IssueController {

    def issueTrackerService

    static allowedMethods = [save: "POST", assignReviewWithAnIssue: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Issue.list(params), model:[issueInstanceCount: Issue.count()]
    }

    def show(Issue issueInstance) {
        respond issueInstance
    }

    def create() {
        respond new Issue(params)
    }

    @Transactional
    def save(Issue issueInstance) {
        if (issueInstance == null) {
            notFound()
            return
        }

        if (issueInstance.hasErrors()) {
            respond issueInstance.errors, view:'create'
            return
        }

        issueInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'issueInstance.label', default: 'Issue'), issueInstance.id])
                redirect issueInstance
            }
            '*' { respond issueInstance, [status: CREATED] }
        }
    }

    def edit(Issue issueInstance) {
        respond issueInstance
    }

    def requestIssueDetailsBig() {
        requestIssueDetails(false)
    }

    def requestIssueDetailsSmall() {
        requestIssueDetails(true)
    }

    @Transactional
    private def requestIssueDetails(isSmall) {

        def issue = Issue.get(params.id)
        if (!issue) {
            notFound()
            return
        }

        IssueTicket ticket = issueTrackerService.getIssueTicket(issue)

        if (ticket) {
            def template = isSmall ? '/issue/issueHeader' : '/issue/issue'
            def htmlToRender = g.render(template: template, model: [issue: ticket, key: issue.key])
            render HelpTagLib.toSingleLine(htmlToRender)
        }
        else {
            notFound()
        }
    }

    @Transactional
    def assignReviewWithAnIssue(Issue issueInstance) {
        if (issueInstance == null) {
            notFound()
            return
        }

        if (issueInstance.hasErrors()) {
            response.status = 500
            render(issueInstance.errors as JSON)
            return
        }

        def reviewId = params.getProperty("assignToReview")
        if (reviewId == null) {
            response.status = 500
            render "The review ID must be specified"
            return
        }

        issueInstance.save flush:true

        def reviewInstance = Review.get(reviewId)
        reviewInstance?.addToIssueTickets(issueInstance)
        reviewInstance?.save(flush: true)

        // notify Issue Tracker(s) that user just closed a review
        ITracker issueTracker = issueInstance.tracker.initImplementation();
        issueTracker.before()
        issueTracker.notifyTrackerReviewCreated(issueInstance.key, reviewInstance)

        def htmlToRender = g.render(template: '/review/issueTicketLoading', model: [issueId: issueInstance.ident()])
        render ([HelpTagLib.toSingleLine(htmlToRender), issueInstance.ident()] as JSON)
    }

    @Transactional
    def update(Issue issueInstance) {
        if (issueInstance == null) {
            notFound()
            return
        }

        if (issueInstance.hasErrors()) {
            respond issueInstance.errors, view:'edit'
            return
        }

        issueInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Issue.label', default: 'Issue'), issueInstance.id])
                redirect issueInstance
            }
            '*'{ respond issueInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(Issue issueInstance) {

        if (issueInstance == null) {
            notFound()
            return
        }

        issueInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Issue.label', default: 'Issue'), issueInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'issueInstance.label', default: 'Issue'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
