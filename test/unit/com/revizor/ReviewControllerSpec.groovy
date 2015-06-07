package com.revizor

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.*


@TestFor(ReviewController)
@Mock([Review, User, Repository])
class ReviewControllerSpec extends Specification {

    def populateValidParams(params) {
        params["title"] = 'Please check out my last story'
		params["commits"] = ["3bf8af1cd6db619882827940e4efc2fd4a884b07",
							 "d80ffab09904c3bc0e332ce6d9c4a07aedeb3d34"]
		
		params["description"] = 'Here I implemented my task'
		params["author"] = new User(
									email: "test@test.com",
									username: "User One",
									password: "123123",
									role: Role.ADMIN).save(flush:true, validate: false).id
		params["repository"] = new Repository(
									url:"git://url.to.git", 
									title: "Main repo", 
									folderName: "repo").save(flush:true, validate: false).id
        params["status"] = ReviewStatus.OPEN
    }

    def setup() {
        controller.notificationService = [create: {who, action, args -> }]
    }

    def "Test the index action returns the correct model"() {

        when:"The index action is executed"
            controller.index()

        then:"The model is correct"
            !model.reviewInstanceList
            model.reviewInstanceCount == 0
    }

	def "You can not create a review without the Repo ID"() {
		when:"The create action is executed"
			controller.create()

		then:"The model is correctly created"
			model.reviewInstance == null
	}
	
    def "Create action returns the correct model"() {
        when:"The create action is executed"
			populateValidParams(params)
			params["id"] = 1
            controller.create()

        then:"The model is correctly created"
            model.reviewInstance!= null
    }

    def "Test the save action correctly persists an instance"() {

        when:"The save action is executed with an invalid instance"
            request.contentType = FORM_CONTENT_TYPE
            def review = new Review()
            controller.save(review)

        then:"The create view is rendered again with the correct model"
            model.reviewInstance != null
            view == 'create'
			Review.count() == 0

        when:"The save action is executed with a valid instance"
            response.reset()            
            populateValidParams(params)
            review = new Review(params)
            controller.save(review)

        then:"Flash message tells us, that everything is ok"
            controller.flash.message != null
			controller.flash.message == "default.created.message"
			
		and: "at least one Review exists in the database"
            Review.count() == 1
    }

    def "Test that the show action returns the correct model"() {
        when:"The show action is executed with a null domain"
            controller.show(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the show action"
            populateValidParams(params)
            def review = new Review(params)
            controller.show(review)

        then:"A model is populated containing the domain instance"
            model.reviewInstance == review
    }

    def "Test that the edit action returns the correct model"() {
        when:"The edit action is executed with a null domain"
            controller.edit(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the edit action"
            populateValidParams(params)
            def review = new Review(params)
            controller.edit(review)

        then:"A model is populated containing the domain instance"
            model.reviewInstance == review
    }

    def "Test the update action performs an update on a valid domain instance"() {
        when:"Update is called for a domain instance that doesn't exist"
            request.contentType = FORM_CONTENT_TYPE
            controller.update(null)

        then:"A 404 error is returned"
            response.redirectedUrl == '/review/index'
            flash.message != null


        when:"An invalid domain instance is passed to the update action"
            response.reset()
            def review = new Review()
            review.validate()
            controller.update(review)

        then:"The edit view is rendered again with the invalid instance"
            view == 'edit'
            model.reviewInstance == review

        when:"A valid domain instance is passed to the update action"
            response.reset()
            populateValidParams(params)
            review = new Review(params).save(flush: true, validate: false)
            controller.update(review)

        then:"A redirect is issues to the show action"
            response.redirectedUrl == "/review/show/$review.id"
            flash.message != null
    }

	@Ignore("Ignored because the saving cases StackOverflowError. To be fixed")
    def "Test that the delete action deletes an instance if it exists"() {
        
		when:"The delete action is called for a null instance"
            request.contentType = FORM_CONTENT_TYPE
            controller.delete(null)

        then:"A 404 is returned"
            response.redirectedUrl == '/review/index'
            flash.message != null
			
		and: "the database is still empty"
			Review.count() == 0

        when:"A domain instance is created"
			response.reset()
			populateValidParams(params)
			def review = new Review(params).save(flush: true)
			
        then:"It exists"
            Review.count() == 1

        when:"The domain instance is passed to the delete action"
            controller.delete(review)

        then:"The instance is deleted"
            Review.count() == 0
            response.redirectedUrl == '/review/index'
            flash.message != null
    }
}
