package com.revizor

import grails.gsp.PageRenderer
import org.springframework.web.context.request.RequestContextHolder
import revizor.HelpTagLib
import revizor.NotificationTagLib

class NotificationService {

    def grailsApplication
    PageRenderer groovyPageRenderer

    static transactional = true

    /**
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

    /**
     * Create unread messages for all the users, except current one.
     */
    def makeUnreadEventsForAllUsers(Notification notification, ObjectType type, long id) {
        def session = RequestContextHolder.currentRequestAttributes().getSession()

        User.list().each { User user ->
            if (user.ident() != session.user.ident()) {
                new UnreadEvent(notification: notification, type: type, objectId: id, user: user)
                        .save()
            }
        }
    }

    /**
     * Returns list of IDs of those objects, that user has not read yet.
     */
    def getNewUnreadItemsForMe(ObjectType type, User user) {
        return UnreadEvent.findAllByTypeAndUser(type, user)
                                        .collect { it.objectId }
    }

    def markReadEvent(ObjectType type, User user) {
        UnreadEvent.executeUpdate("delete UnreadEvent c where c.type = :type and c.user = :user",
                [type: type, user: user])
    }

    def markReadCommentsByType(unreadCommentIds, CommentType commentType) {

        if (unreadCommentIds) {

            def unreadNewCommentEventsToBeDeleted = Comment.getAll(unreadCommentIds)
                    .findAll { it.getType() == commentType }
                    .collect { it.ident() }

            def delCnt = UnreadEvent.where {
                eq("type", ObjectType.COMMENT)
                inList("objectId", unreadNewCommentEventsToBeDeleted)
            }
            .deleteAll()

            return delCnt
        }
    }

    /**
     * Returns list of notification IDs, that are not read yet for current user.
     *
     * For example, if there is an unread message that an user created an review, then
     * the notification about this event will be in the returned collection
     *
     * @param user
     * @return
     */
    def getAllNewUnreadItemsForMe(User user) {
        return UnreadEvent.findAllByUser(user)
                .collect { it.notification.ident() }
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
     * @param header
     * @param toAddress - if NULL, then the notification will be sent to actors
     */
    def sendNotificationViaEmail(Notification notification, header, toAddress) {

        if (grailsApplication.config.grails.allowed.email.notifications.asBoolean()) {
            def session = RequestContextHolder.currentRequestAttributes().getSession()

            def ntl = grailsApplication.mainContext.getBean(NotificationTagLib.class.getName());
            def unreadAllItems = getAllNewUnreadItemsForMe(session.user);
            def html = ntl.oneNotification([notification: notification, 'unreadNotifications': unreadAllItems])
            def emailHtml = groovyPageRenderer.render(template: "/email", model: [header: header, message: html])

            if (toAddress) {
                this.sendEmail(header, toAddress, HelpTagLib.toSingleLine(emailHtml))
            } else {
                // send this notification to all actors (except currently logged user)
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
