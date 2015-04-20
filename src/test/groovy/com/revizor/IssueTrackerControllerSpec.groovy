package com.revizor



import grails.test.mixin.*
import spock.lang.*

@TestFor(IssueTrackerController)
@Mock(IssueTracker)
class IssueTrackerControllerSpec extends Specification {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void "Test the index action returns the correct model"() {

        when:"The index action is executed"
            controller.index()

        then:"The model is correct"
            !model.issueTrackerInstanceList
            model.issueTrackerInstanceCount == 0
    }

    void "Test the create action returns the correct model"() {
        when:"The create action is executed"
            controller.create()

        then:"The model is correctly created"
            model.issueTrackerInstance!= null
    }

    void "Test the save action correctly persists an instance"() {

        when:"The save action is executed with an invalid instance"
            request.contentType = FORM_CONTENT_TYPE
            def issueTracker = new IssueTracker()
            issueTracker.validate()
            controller.save(issueTracker)

        then:"The create view is rendered again with the correct model"
            model.issueTrackerInstance!= null
            view == 'create'

        when:"The save action is executed with a valid instance"
            response.reset()
            populateValidParams(params)
            issueTracker = new IssueTracker(params)

            controller.save(issueTracker)

        then:"A redirect is issued to the show action"
            response.redirectedUrl == '/issueTracker/show/1'
            controller.flash.message != null
            IssueTracker.count() == 1
    }

    void "Test that the show action returns the correct model"() {
        when:"The show action is executed with a null domain"
            controller.show(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the show action"
            populateValidParams(params)
            def issueTracker = new IssueTracker(params)
            controller.show(issueTracker)

        then:"A model is populated containing the domain instance"
            model.issueTrackerInstance == issueTracker
    }

    void "Test that the edit action returns the correct model"() {
        when:"The edit action is executed with a null domain"
            controller.edit(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the edit action"
            populateValidParams(params)
            def issueTracker = new IssueTracker(params)
            controller.edit(issueTracker)

        then:"A model is populated containing the domain instance"
            model.issueTrackerInstance == issueTracker
    }

    void "Test the update action performs an update on a valid domain instance"() {
        when:"Update is called for a domain instance that doesn't exist"
            request.contentType = FORM_CONTENT_TYPE
            controller.update(null)

        then:"A 404 error is returned"
            response.redirectedUrl == '/issueTracker/index'
            flash.message != null


        when:"An invalid domain instance is passed to the update action"
            response.reset()
            def issueTracker = new IssueTracker()
            issueTracker.validate()
            controller.update(issueTracker)

        then:"The edit view is rendered again with the invalid instance"
            view == 'edit'
            model.issueTrackerInstance == issueTracker

        when:"A valid domain instance is passed to the update action"
            response.reset()
            populateValidParams(params)
            issueTracker = new IssueTracker(params).save(flush: true)
            controller.update(issueTracker)

        then:"A redirect is issues to the show action"
            response.redirectedUrl == "/issueTracker/show/$issueTracker.id"
            flash.message != null
    }

    void "Test that the delete action deletes an instance if it exists"() {
        when:"The delete action is called for a null instance"
            request.contentType = FORM_CONTENT_TYPE
            controller.delete(null)

        then:"A 404 is returned"
            response.redirectedUrl == '/issueTracker/index'
            flash.message != null

        when:"A domain instance is created"
            response.reset()
            populateValidParams(params)
            def issueTracker = new IssueTracker(params).save(flush: true)

        then:"It exists"
            IssueTracker.count() == 1

        when:"The domain instance is passed to the delete action"
            controller.delete(issueTracker)

        then:"The instance is deleted"
            IssueTracker.count() == 0
            response.redirectedUrl == '/issueTracker/index'
            flash.message != null
    }
}
