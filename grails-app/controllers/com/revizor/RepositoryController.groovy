package com.revizor

import com.revizor.repos.IRepository
import com.revizor.utils.Constants
import grails.transaction.Transactional
import revizor.HelpTagLib

import static org.springframework.http.HttpStatus.*

@Transactional(readOnly = true)
class RepositoryController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def uploadService
    def reviewService

    /**
     * Main enter point of the app. The root URL "/" is going here. 
     */
    def index() {
        def repos = Repository.list()
        if (repos.size() > 0) {
            redirect(controller: "repository", action: "dashboard", id: repos.get(0).ident());
        }
        else {
            redirect(controller: "repository", action: "create")
        }
    }

    def dashboard() {
        if (!params.id) {
            _notFound()
            return
        }
        def id = params.id.toInteger()

        def repos = Repository.list()
        def selectedRepo = repos.find { it.ident() == id }

        if (!selectedRepo) {
            _notFound()
            return
        }

        render(view:'dashboard', model: [repos: repos, selectedRepo: selectedRepo])
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Repository.list(params), model:[repositoryInstanceCount: Repository.count()], view: "index"
    }

    def show(Repository repositoryInstance) {
        respond repositoryInstance
    }

    def create() {
        respond new Repository(params)
    }

    /**
     * Pulls the latest changes from the origin (remote repo) and
     * renders updated tree.
     *
     * @return history tree as HTML
     */
    @Transactional
    def refreshRepository() {
        if (!params.id && params.id.isInteger()) {
            _notFound()
            return
        }

        def repository = Repository.get(params.id)
        IRepository repoImpl = repository.initImplementation();
        def updatedCommits = repoImpl.updateRepo()

        reviewService.checkNewRevisionsForSmartCommits(updatedCommits, repository);

        def html = sc.buildFlatListofCommits(repo: repository)
        render HelpTagLib.toSingleLine(html)
    }

    @Transactional
    def save(Repository repositoryInstance) {
        if (repositoryInstance == null) {
            _notFound()
            return
        }
		
        if (!repositoryInstance.validate()) {
            respond repositoryInstance.errors, view:'create'
            return
        }

        repositoryInstance.save flush:true

        def impl = repositoryInstance.initImplementation();
        impl.cloneRepository(repositoryInstance.url, repositoryInstance.username, repositoryInstance.password);

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'repositoryInstance.label', default: 'Repository'), repositoryInstance.id])
                redirect repositoryInstance
            }
            '*' { respond repositoryInstance, [status: CREATED] }
        }
    }

    def edit(Repository repositoryInstance) {
        respond repositoryInstance
    }

    @Transactional
    def update(Repository repositoryInstance) {
        if (repositoryInstance == null) {
            _notFound()
            return
        }

        if (!repositoryInstance.validate()) {
            respond repositoryInstance.errors, view:'edit'
            return
        }

        repositoryInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Repository.label', default: 'Repository'), repositoryInstance.id])
                redirect repositoryInstance
            }
            '*'{ respond repositoryInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(Repository repositoryInstance) {

        if (repositoryInstance == null) {
            _notFound()
            return
        }

        def directoryPath = Constants.LOCAL_REPO_PATH + File.separator + repositoryInstance.getFolderName()
        repositoryInstance.delete flush:true

        // remove the directory, where old repository was hosted
        def isSuccess = new File(directoryPath).deleteDir()
        if (!isSuccess) {
            throw new RuntimeException("Cant delete directory ${directoryPath}")
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Repository.label', default: 'Repository'), repositoryInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    /**
     * Upload logo for a repository
     * 
     * @return
     */
    @Transactional
    def upload() {
        
        if (!params.id) {
            flash.message = "Repo ID is not specified"
            redirect(action:'index')
            return
        }
        
        def repo = Repository.get(params.id)
        if (repo == null) {
            _notFound()
            return
        }
        
        repo = uploadService.saveUploadedImage(repo, request);

        // Validation works, will check if the image is too big
        if (!repo.save(flush:true)) {
            render(view:'_select_avatar', model:[id : repo.ident()])
            return
        }

        flash.message = "Avatar (${repo.imageType}, ${repo.image.size()} bytes) uploaded."
        redirect(action:'show', id: repo.getId())
    }

    /**
     * Get user avatar
     * @return
     */
    def logo_image() {
        def repo = Repository.get(params.id)
        uploadService.getImage(repo, response);
    }

    protected void _notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'repositoryInstance.label', default: 'Repository'), params.id])
                // redirect to the root of site
                redirect(action: "index", method: "GET")
            }
            '*'{ 
                render status: NOT_FOUND }
        }
    }
}
