package com.revizor

import grails.gsp.PageRenderer
import org.springframework.web.context.request.RequestContextHolder
import revizor.HelpTagLib
import revizor.NotificationTagLib

class NotificationService {

    def grailsApplication
    PageRenderer groovyPageRenderer

    static transactional = true

    /*
    * Creates one record to the notification feed
    *
    * @param who - the main actor
    * @param action - the Action enum instance
    * @params actors - parameters to be replaced in translated message (collection)
    * @params actorIndexToBeUsedForDetails - if any "expanded" message follows a notification, then this 
    * parameter specifies the index in the 'actors' collection.
    *
    * Please refer to the comments in files Notification.groovy and NotificationObject.groovy
    */
    def create(who, action, actors, actorIndexToBeUsedForDetails = -1) {
        Notification u = new Notification (
                object: who,
                time: new Date(),
                action: action,
                detailedActorIndex: actorIndexToBeUsedForDetails
            );

        actors.eachWithIndex { actor, i ->
            u.addToActors(NotificationObject.saveObject(actor, i));
        }

        u.save(flush: true);

        return u
    }

    def feed(max, offset) {

        def all = Notification.withCriteria{
            maxResults max
            firstResult offset
            order("time", "desc")
        }

        return all
    }

    /**
     * Detects, is this notification is related to currently logged user
     * in order to mark is as "to me".
     *
     * For example, "user X (main actor) replied to user Y (actor1) with a comment: blabla".
     * This notification is show for user Y but not to the X. Thus, current
     * method returns TRUE only if actor1 type is USER and user Y is currently logged in.
     *
     * @param notification
     */
    def isNotificationForMe(Notification notification) {
        def session = RequestContextHolder.currentRequestAttributes().getSession()

        // find whether current notification is about an action with actors, where these actors contain current user
        def me = notification.actors.find { NotificationObject no ->
            (no.type == ObjectType.USER && no.objectId == session.user.ident() && no.objectId != notification.object.ident())
        }

        return (me != null)
    }

    /**
     * Sends email for the given notification
     *
     * @param notification
     */
    def sendNotificationViaEmail(Notification notification, header, toAddress) {

        def ntl = grailsApplication.mainContext.getBean(NotificationTagLib.class.getName());
        def html = ntl.oneNotification([notification: notification])
        def emailHtml = groovyPageRenderer.render(template: "/email", model: [header: header, message: html])

        if (toAddress) {
            this.sendEmail(header, toAddress, HelpTagLib.toSingleLine(emailHtml))
        }
        else {
            def session = RequestContextHolder.currentRequestAttributes().getSession()
            notification
                    .actors
                    .findAll { NotificationObject no ->
                (no.type == ObjectType.USER && no.objectId != session.user.ident() && no.objectId != notification.object.ident())
            }
            .each { NotificationObject no ->
                User userToBeNotified = no.resoreInstance()
                this.sendEmail(header, userToBeNotified.email, HelpTagLib.toSingleLine(emailHtml))
            }
        }
    }

    def sendEmail(header, toAddress, emailHtml) {
        sendMail {
            async true
            from "no-reply@revizor.com"
            to toAddress
            subject header
            html " ${emailHtml} "
        }
    }

    def sendPlainEmail(header, toAddress, text) {
        sendMail {
            async true
            from "no-reply@revizor.com"
            to toAddress
            subject header
            body text
        }
    }
}
