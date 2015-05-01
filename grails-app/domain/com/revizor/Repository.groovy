package com.revizor

import com.revizor.repos.git.GitRepository
import com.revizor.repos.IRepository

/**
 * Represents a CVS-repository. It also means "team", because in real world one team usually work on one project.
 * Thus, when we open one repository and assign to it a bunch of users, than means that this is a "team" working
 * on the same project (repo).
 */
class Repository extends HasImage implements INotifiable {

    RepositoryType type;
    String url;
    String title;
    String folderName;
    String password;
    String username;

    def grailsLinkGenerator
    static transients = [ "grailsLinkGenerator" ]

    static belongsTo = User
    static hasMany = [members: User]

    static constraints = {
        url(nullable: false)
        title(nullable: false)
        folderName(nullable: false, matches: '[a-zA-Z0-9]+')
        members(nullable: true)
        password(nullable: true)
        username(nullable: true)
    }

    /**
     * Initiates correct implementation for the currently used repo
     */
    public IRepository initImplementation() {
        switch(this.type) {

           case RepositoryType.GIT:
               return new GitRepository(this.folderName);
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
		return this.title;
	}

    /**
     * Returns URL for current instance
     * @return
     */
    @Override
    String getLinkHref() {
        return grailsLinkGenerator.link(controller: 'repository', action: 'dashboard', id: this.ident(), absolute: true)
    }
}

enum RepositoryType {
    GIT,
    MERCURIAL
}