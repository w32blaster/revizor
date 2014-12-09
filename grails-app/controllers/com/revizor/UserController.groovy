package com.revizor

import com.revizor.security.BCrypt

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class UserController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def uploadService

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond User.list(params), model:[userInstanceCount: User.count()], view: "index"
    }

    def list(Integer max) {
        index(max)
    }

    def show(User userInstance) {
        respond userInstance
    }

    def create() {
        respond new User(params)
    }

    @Transactional
    def save(User userInstance) {
        if (userInstance == null) {
            notFound()
            return
        }

        if (userInstance.hasErrors()) {
            respond userInstance.errors, view:'create'
            return
        }

        // hash and salt the password
        userInstance.password = BCrypt.hashpw(userInstance.password, BCrypt.gensalt());

        userInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'userInstance.label', default: 'User'), userInstance.id])
                redirect userInstance
            }
            '*' { respond userInstance, [status: CREATED] }
        }
    }

    def edit(User userInstance) {
        respond userInstance
    }

    @Transactional
    def update() {
        User userInstance = User.get(params.id)
        if(params.password) {
            params.password = BCrypt.hashpw(params.password, BCrypt.gensalt());
        }
        else {
            params.remove('password')
        }
        // apply data binding
        userInstance.properties = params

        if (userInstance == null) {
            notFound()
            return
        }

        if (userInstance.hasErrors()) {
            respond userInstance.errors, view:'edit'
            return
        }

        userInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'User.label', default: 'User'), userInstance.id])
                redirect userInstance
            }
            '*'{ respond userInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(User userInstance) {

        if (userInstance == null) {
            notFound()
            return
        }

        userInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'User.label', default: 'User'), userInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }
	
	
	/**
	 * Upload user avatar
	 * 
	 * @return
	 */
	@Transactional
	def upload() {
		
		if (!params.id) {
			flash.message = "User ID is not specified"
			redirect(action:'index')
			return
		}
		
		def user = User.get(params.id)
		if (user == null) {
			notFound()
			return
		}
		
		user = uploadService.saveUploadedImage(user, request);

		// Validation works, will check if the image is too big
		if (!user.save(flush:true)) {
			render(view:'_select_avatar', model:[user:user])
			return
		}

		flash.message = "Avatar (${user.imageType}, ${user.image.size()} bytes) uploaded."
		redirect(action:'show', id: user.getId())
	}
	
	/**
	 * Get user avatar
	 * @return
	 */
	def avatar_image() {
		def user = User.get(params.id)
        uploadService.getImage(user, response);
	}

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'userInstance.label', default: 'User'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
