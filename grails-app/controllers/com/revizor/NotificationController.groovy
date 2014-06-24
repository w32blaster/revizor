package com.revizor

import com.revizor.cmd.LoginCommand
import com.revizor.utils.Constants

class NotificationController {

	def notificationService

    /**
     * Loads notifications with a given offset for a feed.
     */
    def loadMore = {
        def offset = params.int('offset')
        def notificationsHtml = notification.feed(offset: offset)

        render notificationsHtml
    }
}