package com.revizor

import grails.transaction.Transactional
import revizor.HelpTagLib

class NotificationController {

	def notificationService

    /**
     * Loads notifications with a given offset for a feed.
     */
    def loadMore = {
        def offset = params.int('offset')
        def notificationsHtml = ntl.feed(offset: offset)

        render HelpTagLib.toSingleLine(notificationsHtml);
    }

    /**
     * method is called via ajax-request. Just mark all the events as "read" for current user
     *
     * @return
     */
    @Transactional
    def markAllReadEvents() {
        def cnt = UnreadEvent.executeUpdate("delete UnreadEvent c where c.user = :user",
                [user: session.user])

        render cnt
    }
}