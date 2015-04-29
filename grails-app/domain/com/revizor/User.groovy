package com.revizor

import revizor.Alias

class User extends HasImage implements INotifiable {

    String email // we use it as an user ID, it must be unique
    String username // <-- this could be either nick name or real name
	String password
    String position // <-- job position within a team: Developer, Manager, CEO...
    Role role
	
    static hasMany = [
            comments: Comment,
            repositories: Repository,
            asReviewer: Reviewer,
            reviewsAsAuthor: Review,
            aliases: Alias,
            unreadEvents: UnreadEvents
    ]

    static constraints = {
        email(email: true, nullable: false, unique: true)
        username(nullable: false)
		password(blank:false, minSize: 6)
		role(nullable: false)
        reviewsAsAuthor(nullable: true)
    }
	
	/**
	 * {@inheritDoc}
	 */
	public String getDetailsAsHtml() {
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getNotificationName() {
		return this.username;
	}
}

enum Role {
    ADMIN,
    USER
}
