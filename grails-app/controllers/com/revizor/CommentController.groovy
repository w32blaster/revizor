package com.revizor



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import revizor.HelpTagLib
import com.revizor.Action
import com.revizor.CommentType

@Transactional(readOnly = true)
class CommentController {

    def notificationService;

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)

        def list
        def commentsFilter = params.filter as CommentsFilter
        switch(commentsFilter) {
            
            case CommentsFilter.ALL:
                list = Comment.list(params);
                break;

            case CommentsFilter.ONLY_MINE:
                list = Comment.findAllByAuthor(session.user);
                break;

            case CommentsFilter.REPLIES_TO_ME:
                // to be implemented: https://github.com/w32blaster/revizor/issues/7
                list = Comment.list(params);
                break;

            default:
                list = Comment.list(params);
                break;                
        }

        respond list, model:[commentInstanceCount: Comment.count()]
    }

    def show(Comment commentInstance) {
        respond commentInstance
    }

    def create() {
        respond new Comment(params)
    }

    @Transactional
    def save(Comment commentInstance) {

		if (commentInstance == null) {
            notFound()
            return
        }

        if (commentInstance.hasErrors()) {
            respond commentInstance.errors, view:'create'
            return
        }

        commentInstance.text = commentInstance.text.encodeAsHTML()
        commentInstance.save flush:true

        saveNotification(commentInstance)

        withFormat {
            html {
                def htmlToRender = g.render(template: '/comment/comment' , model: ['comment': commentInstance, 'indent': 0])
                render HelpTagLib.toSingleLine(htmlToRender)
            }
            '*' {
                flash.message = message(code: 'default.created.message', args: [message(code: 'commentInstance.label', default: 'Comment'), commentInstance.id])
                redirect (controller: 'review', action: 'show', id: params.redirectTo)
            }
        }
    }

    def edit(Comment commentInstance) {
        respond commentInstance
    }

    @Transactional
    def update(Comment commentInstance) {
        if (commentInstance == null) {
            notFound()
            return
        }

        if (commentInstance.hasErrors()) {
            respond commentInstance.errors, view:'edit'
            return
        }

        commentInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Comment.label', default: 'Comment'), commentInstance.id])
                redirect commentInstance
            }
            '*'{ respond commentInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(Comment commentInstance) {

        if (commentInstance == null) {
            notFound()
            return
        }

        commentInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Comment.label', default: 'Comment'), commentInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'commentInstance.label', default: 'Comment'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }

    def saveNotification(comment) {
        if (comment.replyTo) {
            notificationService.create(session.user, Action.CREATE_COMMENT_REPLY_TO, [comment.author, comment.replyTo.author, comment], 2)
        }
        else if (comment.type == CommentType.REVIEW) {
            notificationService.create(session.user, Action.CREATE_COMMENT_TO_REVIEW, [comment.author, comment, comment.review], 1)
        }
        else if (comment.type == CommentType.LINE_OF_CODE) {
            notificationService.create(session.user, Action.CREATE_COMMENT_TO_LINE_OF_CODE, [comment.author, comment, comment.fileName, comment.review], 1)
        }
    }
}

public enum CommentsFilter {
    ALL("comments.all"),
    ONLY_MINE("comments.mine"),
    REPLIES_TO_ME("comments.replies"),

    CommentsFilter(String value) { this.value = value }

    private final String value
    public String value() { return value }
}