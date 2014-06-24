package com.revizor

import grails.util.GrailsNameUtils

import com.revizor.repos.GitRepository
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

    static belongsTo = User
    static hasMany = [members: User]

    static constraints = {
        url(nullable: false)
        title(nullable: false)
        folderName(nullable: false, matches: '[a-zA-Z0-9]+')
        members(nullable: true)
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
	public String getNotificationLink() {
		return g.createLink(controller: GrailsNameUtils.getShortName(this.class).toLowerCase(), action: 'show', id: this.ident());
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getNotificationName() {
		return this.title;
	}
}

enum RepositoryType {
    GIT,
    MERCURIAL
}