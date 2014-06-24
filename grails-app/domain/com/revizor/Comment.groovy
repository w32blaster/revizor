package com.revizor

import grails.util.GrailsNameUtils

/**
 * One comment. Could be comment to a line of code or to the whole review
 */
class Comment implements INotifiable {

    String text
    // in case the current comment is for a specific commit, this field should be specified (SHA value)
    String commit
	// fullName - name with full path
    String fileName
    int lineOfCode
	LineType typeOfLine
    CommentType type
    Comment replyTo

    static belongsTo = [
            review: Review,
            author: User
    ]

    static constraints = {
        author(nullable: false)
        text(nullable: false, blank: false)

		// could be null, because they are filled only if a comment is left for a line of a code
		commit(nullable: true)
		fileName(nullable: true)
		typeOfLine(nullable:true)
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
		return this.text;
	}
}

enum CommentType {
    REVIEW,
    LINE_OF_CODE
}

/**
 * We need to differ different types of line where an user left his comment: old version, new version or 
 * unmodified line. Let's say, a new line of code was added during the commit and a reviewer left a comment to 
 * this line. So, the type will be NEW, because this line is not presented on the old version of a file as long
 * as this line was just added.
 *  
 * But when we count unmodified lines of code, we should count the ORIGINAL file. Let's say we have a file
 *  1 1 
 *  2 2 
 *    3 + added line
 *    4 + added line
 *  3   - deleted line
 *  4 5
 *  5 6
 * as you can see, original and new files have different count of lines. Thus, we assume, that UNMODIFIED line no 5 is
 * the last line from the original file, but not the second from the bottom of the new line
 * @author ilja
 *
 */
enum LineType {
	ORIGINAL,
	NEW,
	UNMODIFIED	
}