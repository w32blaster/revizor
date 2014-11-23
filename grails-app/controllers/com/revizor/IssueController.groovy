package com.revizor



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class IssueController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

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
