package com.revizor

import revizor.CommentTagLib

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
                def htmlToRender = cmt.printOneComment(['comment': commentInstance, 'indent': params.indent as Integer])
                render HelpTagLib.toSingleLine(htmlToRender)
            }
            '*' {
                flash.message = message(code: 'default.created.message', args: [message(code: 'comments.one', default: 'Comment'), commentInstance.id])
                redirect (controller: 'review', action: 'show', id: params.redirectTo)
            }
        }
    }

    /**
     * Used to retrieve a HTML template to display a form "add new comment" in Ajax call.
     *
     * @return HTML of a form
     */
    def getAddNewFormLayout() {
        if(params.replyTo) {
            def replyToComment = Comment.get(params.replyTo);
            params.replyToComment = replyToComment
        }
        def htmlToRender = g.render(template: '/comment/addNewCommentForm' , model: params)
        render HelpTagLib.toSingleLine(htmlToRender)
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
                flash.message = message(code: 'default.updated.message', args: [message(code: 'comments.one', default: 'Comment'), commentInstance.id])
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
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'comments.one', default: 'Comment'), commentInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'comments.one', default: 'Comment'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }

    def saveNotification(comment) {
        if (comment.replyTo) {
            def to = (comment.author.ident() == comment.replyTo.author.ident()) ? g.message(code: "reply.to.himself") : comment.replyTo.author;
            notificationService.create(session.user, Action.CREATE_COMMENT_REPLY_TO, [comment.author, to, comment, comment.review], 2)
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
    REPLIES_TO_ME("comments.replies");

    CommentsFilter(String val) { this.value = val; }

    private final String value
    public String value() { return value }
}