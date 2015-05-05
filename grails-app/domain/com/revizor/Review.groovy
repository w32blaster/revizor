package com.revizor
/**
 * One separate review by one or few commits
 */
class Review implements INotifiable {

    String title
    // SHA-1 of the reviewed commits
    String[] commits = []
    String description
    ReviewStatus status
    // SHA of smart commit, that current review was created
    String smartCommitId

    def grailsLinkGenerator
    static transients = [ "grailsLinkGenerator" ]

    static belongsTo = [
            repository: Repository,
            author: User
    ]

    static hasMany = [
            reviewers: Reviewer,
            comments: Comment,
            issueTickets: Issue
    ]

    static constraints = {
        title(nullable: false)
        repository(nullable: false)
        author(nullable: false)
        reviewers(nullable: true)
        smartCommitId(nullable: true)
    }

    def findReviewerByUser(user) {
        return reviewers?.find{
            it.reviewer.id == user.id
        }
    }
	
	/**
	 * {@inheritDoc}
	 */
    @Override
	public String getDetailsAsHtml() {
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
    @Override
	public String getNotificationName() {
		return "${this.id}: ${this.title}";
	}

    /**
     * Returns URL for current instance
     * @return
     */
    @Override
    String getLinkHref() {
        return grailsLinkGenerator.link(controller: 'review', action: 'show', id: this.ident(), absolute: true)
    }

    @Override
    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        Review review = (Review) o

        if (author != review.author) return false
        if (description != review.description) return false
        if (id != review.id) return false
        if (repository != review.repository) return false
        if (smartCommitId != review.smartCommitId) return false
        if (status != review.status) return false
        if (title != review.title) return false
        if (version != review.version) return false

        return true
    }

    @Override
    int hashCode() {
        int result
        result = title.hashCode()
        result = 31 * result + (description != null ? description.hashCode() : 0)
        result = 31 * result + status.hashCode()
        result = 31 * result + (smartCommitId != null ? smartCommitId.hashCode() : 0)
        result = 31 * result + id.hashCode()
        result = 31 * result + version.hashCode()
        return result
    }
}

enum ReviewStatus {
    OPEN("review.status.open"),
    CLOSED("review.status.closed")

    ReviewStatus(String value) { this.value = value }

    private final String value
    public String value() { return value }
}