package revizor

import com.revizor.Notification
import com.revizor.NotificationObject
import grails.util.GrailsNameUtils
import com.revizor.utils.Constants

class NotificationTagLib {

    static namespace = "notification"
    
    def notificationService

    /**
     * Prints out notification's feed for currently logged in user
     */
    def feed = { attrs, body ->

        def offset = attrs.offset ?: 0
        def notifications = notificationService.feed(Constants.MAX_PER_REQUEST, offset)

        notifications.each { notification ->

            def messageParams = notification.actors.sort { it.idx }.collect { getHtmlMessage(it) }
            def msg = g.message(code: notification.action.value(), args: messageParams, encodeAs: 'None')

            out << g.render(template: "/notification/notification", model: ['mainActor': notification.object, 'message': msg])
        }

    }

    private String getHtmlMessage(object) {
		def actorObject = object.resoreInstance();
		
		if (!actorObject) {
			return "";	
		}
		else if (actorObject instanceof String) {
			return actorObject
		}
		else {
			def classSimpleName = GrailsNameUtils.getShortName(actorObject.class).toLowerCase();
			def href = g.createLink(controller: classSimpleName, action: 'show', id: actorObject.ident())
			return "<a href='${href}' class='notification-link'>${actorObject.getNotificationName()}</a>"
		}
    }
}
