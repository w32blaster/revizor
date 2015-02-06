package com.revizor



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class ChatController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Chat.list(params), model:[chatInstanceCount: Chat.count()]
    }

    def show(Chat chatInstance) {
        respond chatInstance
    }

    def create() {
        respond new Chat(params)
    }

    @Transactional
    def save(Chat chatInstance) {
        if (chatInstance == null) {
            notFound()
            return
        }

        if (chatInstance.hasErrors()) {
            respond chatInstance.errors, view:'create'
            return
        }

        chatInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'chatInstance.label', default: 'Chat'), chatInstance.id])
                redirect chatInstance
            }
            '*' { respond chatInstance, [status: CREATED] }
        }
    }

    def edit(Chat chatInstance) {
        respond chatInstance
    }

    @Transactional
    def update(Chat chatInstance) {
        if (chatInstance == null) {
            notFound()
            return
        }

        if (chatInstance.hasErrors()) {
            respond chatInstance.errors, view:'edit'
            return
        }

        chatInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Chat.label', default: 'Chat'), chatInstance.id])
                redirect chatInstance
            }
            '*'{ respond chatInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(Chat chatInstance) {

        if (chatInstance == null) {
            notFound()
            return
        }

        chatInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Chat.label', default: 'Chat'), chatInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'chatInstance.label', default: 'Chat'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
