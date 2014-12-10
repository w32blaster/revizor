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

        /*
         * Build each message. Please refer to the JavaDocs in the file NotificaionObject.groovy
         * for details.
         */
        notifications.each { notification ->

            def actors = notification.actors.sort { it.idx }

            def messageParams = actors.collect { getHtmlMessage(it) }
            def msg = g.message(code: notification.action.value(), args: messageParams, encodeAs: 'None')
            def details = (notification.detailedActorIndex > -1) ? getDetailedHtmlBlock(actors.get(notification.detailedActorIndex)) : null
            def isItShownForMe = notificationService.isNotificationForMe(notification)

            out << g.render(template: "/notification/notification", model: [
                'mainActor': notification.object, 
                'message': msg,
                'forMe':isItShownForMe,
                'details': details])
        }

    }

    /**
     * Returns the HTML for the detailes. Some actors need to be added to the notification as detailed view
     */
    private String getDetailedHtmlBlock(object) {
        def actorObject = object.resoreInstance();
        return actorObject.getDetailsAsHtml()
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
