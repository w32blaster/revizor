package com.revizor

import com.revizor.utils.Constants
import org.springframework.context.i18n.LocaleContextHolder as LCH

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
    Date date = new Date()

    def grailsLinkGenerator
    static transients = [ "grailsLinkGenerator" ]

    static belongsTo = [
            review: Review,
            author: User,
            replyTo: Comment
    ]

    static hasMany = [
            replies: Comment
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
    @Override
	public String getDetailsAsHtml() {
		def grailsApplication = this.domainClass.grailsApplication
        def ctx = grailsApplication.mainContext
        def markdown = ctx.getBean('com.naleid.grails.MarkdownTagLib')
        return markdown.renderHtml(text: this.text);
	}
	
	/**
	 * {@inheritDoc}
	 */
    @Override
	public String getNotificationName() {
        def grailsApplication = this.domainClass.grailsApplication
        def ctx = grailsApplication.mainContext
        return ctx.getMessage("comments.one", null, LCH.getLocale())
	}

    /**
     * {@inheritDoc}
     */
    @Override
    String getLinkHref() {
        switch (this.getType()) {
            case CommentType.LINE_OF_CODE:
                /*
                    The link for a review looks like this:
                        http//domain/review/show/{id}/single/?fileName={file}#{comment-id}

                    The 'comment-id' is build of three parts: prefix, line of code and type. It is done to
                    correctly display comments tree in HTML using JS. Please refer to the DiffTagLib.renderRowForSingleView function

                    Pattern of a root comment:
                        {prefix}-{lineOfCode}-{typeOfLine}

                    Pattern of a reply:
                        {prefix}-{id}

                    URL example:
                        http://localhost:8080/revizor/review/show/1/single?fileName=src/main/java/org/jbake/launcher/LaunchOptions.java#comment-container-24-UNMODIFIED

                 */
                def linkToReview = grailsLinkGenerator.link(controller: 'review', action: 'show', id: this.getReview().ident(), absolute: true);

                def commentAnchorId = this.replyTo ?
                        Constants.CONTAINER_ID_PREFIX + this.ident() :
                        Constants.CONTAINER_ID_PREFIX + this.lineOfCode + "-" + this.typeOfLine;

                return "${linkToReview}/${Constants.REVIEW_SINGLE_VIEW}?${Constants.PARAM_FILE_NAME}=${this.fileName.toString()}#${commentAnchorId}"

            case CommentType.REVIEW:
                def commentReviewAnchorId = Constants.CONTAINER_ID_PREFIX + this.ident()
                return grailsLinkGenerator.link(controller: 'review', action: 'show', id: this.getReview().ident(), fragment: commentReviewAnchorId, absolute: true)
        }
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