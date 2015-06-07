package com.revizor



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class IssueTrackerController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond IssueTracker.list(params), model:[issueTrackerInstanceCount: IssueTracker.count()], view: "index"
    }

    def list(Integer max) {
        index(max)
    }

    def show(IssueTracker issueTrackerInstance) {
        respond issueTrackerInstance
    }

    def create() {
        respond new IssueTracker(params)
    }

    @Transactional
    def save(IssueTracker issueTrackerInstance) {
        if (issueTrackerInstance == null) {
            notFound()
            return
        }

        if (issueTrackerInstance.hasErrors()) {
            respond issueTrackerInstance.errors, view:'create'
            return
        }

        issueTrackerInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'issueTrackerInstance.label', default: 'IssueTracker'), issueTrackerInstance.id])
                redirect issueTrackerInstance
            }
            '*' { respond issueTrackerInstance, [status: CREATED] }
        }
    }

    def edit(IssueTracker issueTrackerInstance) {
        respond issueTrackerInstance
    }

    @Transactional
    def update(IssueTracker issueTrackerInstance) {
        if (issueTrackerInstance == null) {
            notFound()
            return
        }

        if (issueTrackerInstance.hasErrors()) {
            respond issueTrackerInstance.errors, view:'edit'
            return
        }

        issueTrackerInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'IssueTracker.label', default: 'IssueTracker'), issueTrackerInstance.id])
                redirect issueTrackerInstance
            }
            '*'{ respond issueTrackerInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(IssueTracker issueTrackerInstance) {

        if (issueTrackerInstance == null) {
            notFound()
            return
        }

        issueTrackerInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'IssueTracker.label', default: 'IssueTracker'), issueTrackerInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'issueTrackerInstance.label', default: 'IssueTracker'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
