package com.revizor

import com.revizor.repos.IRepository
import com.revizor.utils.Constants
import grails.transaction.Transactional
import org.apache.commons.io.FileUtils
import revizor.HelpTagLib

import static org.springframework.http.HttpStatus.*

@Transactional(readOnly = true)
class RepositoryController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def uploadService
    def reviewService
    def notificationService

    /**
     * Main enter point of the app. The root URL "/" is going here. 
     */
    def index() {
        def repos = Repository.list()
        if (repos.size() > 0) {
            redirect(controller: "repository", action: "homePage");
        }
        else {
            redirect(controller: "repository", action: "create")
        }
    }

    def homePage() {
        // when we log in to the system, activate the very first repo
        def anyFirstRepository = Repository.list([max: 1])[0]
        session.activeRepository = anyFirstRepository.ident()

        def unreadReviewIds = notificationService.getNewUnreadItemsForMe(ObjectType.REVIEW, session.user)
        def activeReviews = Review.findAllByStatus(com.revizor.ReviewStatus.OPEN, [sort: 'id', order: 'desc'])
        def mapOfComments = Comment.list().groupBy { it.review.id }
        def unreadRepositoriesIds = notificationService.getNewUnreadItemsForMe(ObjectType.REPO, session.user)

        render view: "homePage", model: [
                activeReviews: activeReviews,
                unreadReviews: unreadReviewIds,
                unreadRepos: unreadRepositoriesIds,
                commentsGroupedByReview: mapOfComments]
    }

    def dashboard() {
        if (!params.id) {
            _notFound()
            return
        }
        def id = params.id.toInteger()
        session.activeRepository = id

        // mark "unread" the event about this review for current user
        notificationService.markReadEvent(ObjectType.REPO, session.user)

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
     * In case if user tries to create a new repository but the target
     * folder exists, then this action may potentially destroy some data.
     *
     * In this case we need to warn user. This method is supposed to be called
     * from AJAX request. It checks the folder existence.
     *
     */
    def checkFolderExistence() {
        def directoryPath = Constants.LOCAL_REPO_PATH + File.separator + params.folderName
        def dir = new File(directoryPath)
        render dir.exists() ? "1" : "0"
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

        def directoryPath = Constants.LOCAL_REPO_PATH + File.separator + params.folderName
        def targetDir =  new File(directoryPath)
        if (targetDir.exists()) FileUtils.deleteDirectory(targetDir)

        Repository repo = repositoryInstance.save flush:true

        def impl = repositoryInstance.initImplementation();
        impl.cloneRepository(repositoryInstance.url, repositoryInstance.username, repositoryInstance.password);

        // create new notification about that event...
        def n = notificationService.create(session.user, Action.CREATE_REPOSITORY, [session.user, repo])
        // ...and mark for all the users, that this event is unread yet
        notificationService.makeUnreadEventsForAllUsers(n, ObjectType.REPO, repo.ident())

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
