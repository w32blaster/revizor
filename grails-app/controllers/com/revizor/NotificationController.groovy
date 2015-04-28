package com.revizor

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
}