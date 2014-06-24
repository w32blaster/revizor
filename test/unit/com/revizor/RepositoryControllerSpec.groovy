package com.revizor



import com.revizor.repos.IRepository
import grails.test.mixin.*
import spock.lang.*

@TestFor(RepositoryController)
@Mock(Repository)
class RepositoryControllerSpec extends Specification {

 
    def populateValidParams(params) {
        assert params != null

        params["url"] = 'git://some/url/to.git'
        params["title"] = 'Test Repository'
        params["folderName"] = 'repo'
        params["type"] = RepositoryType.GIT
    }

    def "When an user navigates to the root app, it is asked to create a new repo"() {

        when:"User goes to the inder (website root)"
            controller.index()

        then:"he is redirected to the create page to clone new repo"
            response.redirectedUrl == '/repository/create'
    }

    def "When an user navigates to the root app having any repos, it is redirected to the dashboard"() {
        
        setup: "Prepare mock for the GIT/Mercurial implementation"
            def mock = [cloneRepository: {} ] as IRepository
            Repository.metaClass.initImplementation = { return mock }

        and: "system contains at least one repo"
            populateValidParams(params)
            def repository = new Repository(params)
            controller.save(repository)

        when: "An user goes to the index (website root)"
            controller.index()

        then: "he is redirected to the dashboard"
            response.redirectedUrl == '/repository/dashboard/1'
    }

    def "Test the create action returns the correct model"() {
        when:"The create action is executed"
            controller.create()

        then:"The model is correctly created"
            model.repositoryInstance!= null
    }

    void "Test the save action correctly persists an instance"() {
		
		setup: "Prepare mock for the GIT/Mercurial implementation"
			def mock = [cloneRepository: {} ] as IRepository
			Repository.metaClass.initImplementation = {	return mock }
		
        when:"The save action is executed with an invalid instance"
            request.contentType = FORM_CONTENT_TYPE
            def repository = new Repository()
            repository.validate()
            controller.save(repository)

        then:"The create view is rendered again with the correct model"
            model.repositoryInstance!= null
            view == 'create'

        when:"The save action is executed with a valid instance"
            response.reset()
            populateValidParams(params)
            repository = new Repository(params)
            controller.save(repository)

        then:"A redirect is issued to the show action"
            response.redirectedUrl == '/repository/show/1'
            controller.flash.message != null
            Repository.count() == 1
    }

    void "Test that the show action returns the correct model"() {
        when:"The show action is executed with a null domain"
            controller.show(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the show action"
            populateValidParams(params)
            def repository = new Repository(params)
            controller.show(repository)

        then:"A model is populated containing the domain instance"
            model.repositoryInstance == repository
    }

    void "Test that the edit action returns the correct model"() {
        when:"The edit action is executed with a null domain"
            controller.edit(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the edit action"
            populateValidParams(params)
            def repository = new Repository(params)
            controller.edit(repository)

        then:"A model is populated containing the domain instance"
            model.repositoryInstance == repository
    }

    void "Test the update action performs an update on a valid domain instance"() {
        
        when:"Update is called for a domain instance that doesn't exist"
            request.contentType = FORM_CONTENT_TYPE
            controller.update(null)

        then:"domain not found and user is redirected to the root of the site"
            response.redirectedUrl == '/'
            flash.message != null


        when:"An invalid domain instance is passed to the update action"
            response.reset()
            def repository = new Repository()
            repository.validate()
            controller.update(repository)

        then:"The edit view is rendered again with the invalid instance"
            view == 'edit'
            model.repositoryInstance == repository

        when:"A valid domain instance is passed to the update action"
            response.reset()
            populateValidParams(params)
            repository = new Repository(params).save(flush: true)
            controller.update(repository)

        then:"A redirect is issues to the show action"
            response.redirectedUrl == "/repository/show/$repository.id"
            flash.message != null
    }

    void "Test that the delete action deletes an instance if it exists"() {

        when:"The delete action is called for a null instance"
            request.contentType = FORM_CONTENT_TYPE
            controller.delete(null)

        then:"domain not found and user is redirected to the root of the site"
            response.redirectedUrl == '/'
            flash.message != null

        when:"A domain instance is created"
            response.reset()
            populateValidParams(params)
            def repository = new Repository(params).save(flush: true)

        then:"It exists"
            Repository.count() == 1

        when:"The domain instance is passed to the delete action"
            controller.delete(repository)

        then:"The instance is deleted"
            Repository.count() == 0
            response.redirectedUrl == '/'
            flash.message != null
    }
}
