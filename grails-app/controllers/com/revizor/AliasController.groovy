package com.revizor

import grails.converters.JSON
import revizor.Alias

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class AliasController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Alias.list(params), model:[aliasInstanceCount: Alias.count()]
    }

    def show(Alias aliasInstance) {
        respond aliasInstance
    }

    def create() {
        respond new Alias(params)
    }

    @Transactional
    def createAjax() {
        if (!params.aliasEmail && !params.user.id) {
            render status: NO_CONTENT
            return
        }

        def alias = new Alias(params)
        alias.save(flush: true, failOnError: true)
        render alias as JSON
    }

    @Transactional
    def save(Alias aliasInstance) {
        if (aliasInstance == null) {
            notFound()
            return
        }

        if (aliasInstance.hasErrors()) {
            respond aliasInstance.errors, view:'create'
            return
        }

        aliasInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'aliasInstance.label', default: 'Alias'), aliasInstance.id])
                redirect aliasInstance
            }
            '*' { respond aliasInstance, [status: CREATED] }
        }
    }

    def edit(Alias aliasInstance) {
        respond aliasInstance
    }

    @Transactional
    def update(Alias aliasInstance) {
        if (aliasInstance == null) {
            notFound()
            return
        }

        if (aliasInstance.hasErrors()) {
            respond aliasInstance.errors, view:'edit'
            return
        }

        aliasInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Alias.label', default: 'Alias'), aliasInstance.id])
                redirect aliasInstance
            }
            '*'{ respond aliasInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(Alias aliasInstance) {

        if (aliasInstance == null) {
            notFound()
            return
        }

        aliasInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Alias.label', default: 'Alias'), aliasInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'aliasInstance.label', default: 'Alias'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
