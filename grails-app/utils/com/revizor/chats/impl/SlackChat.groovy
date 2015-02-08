package com.revizor.chats.impl

import com.revizor.Chat
import com.revizor.Review
import com.revizor.Reviewer
import com.revizor.ReviwerStatus
import com.revizor.chats.IChat
import grails.plugins.rest.client.RestBuilder
import org.codehaus.groovy.grails.commons.GrailsApplication

/**
 * Slack implementation.
 *
 * Please refer to the official API: https://api.slack.com/incoming-webhooks
 * Markup details are here: https://api.slack.com/docs/formatting
 *
 * Created on 08/02/15.
 *
 * @author w32blaster
 */
class SlackChat implements IChat {

    private GrailsApplication grailsApplication;
    private Locale locale;
    private Chat chat;

    def rest = new RestBuilder(connectTimeout:1000, readTimeout:20000)

    public SlackChat(Chat chat, GrailsApplication ga, Locale locale) {
        this.chat = chat
        this.grailsApplication = ga
        this.locale = locale
    }

    /**
     * Perform some optional actions before making any queries.
     * Typically, it is authentication.
     *
     */
    @Override
    def before() {
        // slack doesn't require authentication for Incoming Webhooks API
        return null
    }

    /**
     * Notify chat that a review was created
     *
     * @param review
     * @return
     */
    @Override
    def notifyReviewStarted(Review review) {

        Object[] args = [ review.getTitle(),
                          grailsApplication.config.grails.serverURL + "/review/show/" + review.ident(),
                          review.author.username ] as Object[]

        def commentText = grailsApplication.mainContext.getMessage("slack.review.created.slack.markup",
                args,
                this.locale)

        // send notification to Slack
        def logoUrl = grailsApplication.config.links.images.logo48x48
        this.rest.post(this.chat.getUrl()) {
            json {
                channel = this.chat.getChannel()
                username = "Revizor"
                icon_url = logoUrl
                text = commentText
            }
        }
    }

    /**
     * Notify chat that a review was closed
     *
     * @param review
     * @return
     */
    @Override
    def notifyReviewClosed(Review review) {

        def reviewersResultInWikiMarkup = "\n\n"
        review.reviewers.each { Reviewer reviewer ->
            reviewersResultInWikiMarkup += " * :bust_in_silhouette: ${reviewer.reviewer.username}: ${statusAsThumb(reviewer.status)}\n"
        }

        Object[] args = [ review.author.username,
                          review.ident(),
                          review.getTitle(),
                          grailsApplication.config.grails.serverURL + "/review/show/" + review.ident(),
                          reviewersResultInWikiMarkup ] as Object[]

        def commentText = grailsApplication.mainContext.getMessage("slack.review.closed.slack.markup",
                args,
                this.locale)

        // send notification to Slack
        def logoUrl = grailsApplication.config.links.images.logo48x48
        this.rest.post(this.chat.getUrl()) {
            json {
                channel = this.chat.getChannel()
                username = "Revizor"
                icon_url = logoUrl
                text = commentText
            }
        }
    }


    def statusAsThumb(status) {
        switch (status) {
            case ReviwerStatus.APPROVE:
                return ":thumbsup:"
            case ReviwerStatus.DISAPPROVE:
                return ":thumbsdown:"
            case ReviwerStatus.INVITED:
                return ":hourglass:"
        }

    }
}
