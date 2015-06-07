package com.revizor

import revizor.Alias

class User extends HasImage implements INotifiable {

    String email // we use it as an user ID, it must be unique
    String username // this could be either nick name or real name
	String password
    String position // job position within a team: Developer, Manager, CEO...
    Role role
    UserType type // where this user is stored (internal, LDAP, OAuth...)


    def grailsLinkGenerator
    static transients = [ "grailsLinkGenerator" ]

    static hasMany = [
            comments: Comment,
            repositories: Repository,
            asReviewer: Reviewer,
            reviewsAsAuthor: Review,
            aliases: Alias,
            unreadEvents: UnreadEvent
    ]

    static constraints = {
        email(email: true, nullable: false, unique: true)
        username(nullable: false, blank: false)
		password(nullable: true, blank: false, minSize: 6, validator: { val, obj ->
            // password may be empty for LDAP and other users, but inner users *must* have a password
            if (obj.type == UserType.INNER && null == val) {
                return "password for inner user can not be empty"
            }
        })
		role(nullable: false)
        reviewsAsAuthor(nullable: true)
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
		return this.username;
	}

    /**
     * Returns URL for current instance
     * @return
     */
    @Override
    String getLinkHref() {
        return grailsLinkGenerator.link(controller: 'user', action: 'show', id: this.ident(), absolute: true)
    }
}

enum Role {
    ADMIN,
    USER
}

enum UserType {
    INNER,
    LDAP
}