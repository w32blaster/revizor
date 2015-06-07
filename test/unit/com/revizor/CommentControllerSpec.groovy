package com.revizor

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.*

@TestFor(CommentController)
@Mock([Comment, Review, User])
class CommentControllerSpec extends Specification {

    def populateValidParams(params) {
        params["text"] = 'This is a comment'
		params["commit"] = '3bf8af1cd6db619882827940e4efc2fd4a884b07'
		params["fileName"] = "any/dir/file.groovy"
		params["lineOfCode"] = 123
		params["type"] = CommentType.LINE_OF_CODE
		params["author"] = new User(email: "test@test.com").save(validate: false)
		params["review"] = new Review(title: "test@test.com").save(validate: false)
    }

    def setup() {
        controller.notificationService = [create: {who, action, args -> }]
    }

    def "Test the index action returns the correct model"() {

        when:"The index action is executed"
            controller.index()

        then:"The model is correct"
            !model.commentInstanceList
            model.commentInstanceCount == 0
    }

    def "Test the create action returns the correct model"() {
        when:"The create action is executed"
            controller.create()

        then:"The model is correctly created"
            model.commentInstance!= null
    }

    def "Test the save action correctly persists an instance"() {

        when:"The save action is executed with an invalid instance"
            request.contentType = FORM_CONTENT_TYPE
            def comment = new Comment()
            comment.validate()
            controller.save(comment)

        then:"The create view is rendered again with the correct model"
            model.commentInstance!= null
            view == 'create'

        when:"The save action is executed with a valid instance"
            response.reset()
            populateValidParams(params)
            comment = new Comment(params)

            controller.save(comment)

        then:"A redirect is issued to the show action"
            response.redirectedUrl == '/comment/show/1'
            controller.flash.message != null
            Comment.count() == 1
    }

    void "Test that the show action returns the correct model"() {
        when:"The show action is executed with a null domain"
            controller.show(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the show action"
            populateValidParams(params)
            def comment = new Comment(params)
            controller.show(comment)

        then:"A model is populated containing the domain instance"
            model.commentInstance == comment
    }

    void "Test that the edit action returns the correct model"() {
        when:"The edit action is executed with a null domain"
            controller.edit(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the edit action"
            populateValidParams(params)
            def comment = new Comment(params)
            controller.edit(comment)

        then:"A model is populated containing the domain instance"
            model.commentInstance == comment
    }

    void "Test the update action performs an update on a valid domain instance"() {
        when:"Update is called for a domain instance that doesn't exist"
            request.contentType = FORM_CONTENT_TYPE
            controller.update(null)

        then:"A 404 error is returned"
            response.redirectedUrl == '/comment/index'
            flash.message != null


        when:"An invalid domain instance is passed to the update action"
            response.reset()
            def comment = new Comment()
            comment.validate()
            controller.update(comment)

        then:"The edit view is rendered again with the invalid instance"
            view == 'edit'
            model.commentInstance == comment

        when:"A valid domain instance is passed to the update action"
            response.reset()
            populateValidParams(params)
            comment = new Comment(params).save(flush: true, validate: false)
            controller.update(comment)

        then:"A redirect is issues to the show action"
            response.redirectedUrl == "/comment/show/$comment.id"
            flash.message != null
    }

    void "Test that the delete action deletes an instance if it exists"() {
        when:"The delete action is called for a null instance"
            request.contentType = FORM_CONTENT_TYPE
            controller.delete(null)

        then:"A 404 is returned"
            response.redirectedUrl == '/comment/index'
            flash.message != null

        when:"A domain instance is created"
            response.reset()
            populateValidParams(params)
            def comment = new Comment(params).save(flush: true)

        then:"It exists"
            Comment.count() == 1

        when:"The domain instance is passed to the delete action"
            controller.delete(comment)

        then:"The instance is deleted"
            Comment.count() == 0
            response.redirectedUrl == '/comment/index'
            flash.message != null
    }
}
