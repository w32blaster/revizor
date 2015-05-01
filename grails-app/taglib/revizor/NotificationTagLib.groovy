package revizor

import com.revizor.INotifiable
import com.revizor.NotificationObject
import com.revizor.utils.Constants

class NotificationTagLib {

    static namespace = "ntl"
    
    def notificationService

    /**
     * Prints out notification's feed for currently logged in user
     */
    def feed = { attrs, body ->

        def offset = attrs.offset ?: 0
        def notifications = notificationService.feed(Constants.MAX_PER_REQUEST, offset)
        def unreadAllItems = notificationService.getAllNewUnreadItemsForMe(session.user);

        /*
         * Build each message. Please refer to the JavaDocs in the file NotificaionObject.groovy
         * for details.
         */
        notifications.each { notification ->
            out << oneNotification(['notification': notification, 'unreadNotifications': unreadAllItems])
        }

    }

    /**
     * Prints only one notification
     */
    def oneNotification = { attrs, body ->

        def notification = attrs.notification
        def actors = notification.actors.sort { it.idx }

        def isItShownForMe = notificationService.isNotificationForMe(notification)
        def messageCode = (notification.object.ident() == session.user.ident()) ?
                notification.action.messageByMe() :
                isItShownForMe ? notification.action.messageForMe() : notification.action.message()
        def messageParams = actors.collect { this.getHtmlMessage(it) }
        def msg = g.message(code: messageCode, args: messageParams, encodeAs: 'None', default: "Notification message not found")
        def details = (notification.detailedActorIndex > -1) ? getDetailedHtmlBlock(actors.get(notification.detailedActorIndex)) : null

        out << g.render(template: "/notification/notification", model: [
                'mainActor': notification.object,
                'message': msg,
                'forMe':isItShownForMe,
                'details': details,
                'isUnread': (notification.ident() in attrs.unreadNotifications)])
    }

    /**
     * Returns the HTML for the details. Some actors need to be added to the notification as detailed view
     */
    private String getDetailedHtmlBlock(object) {
        def actorObject = object.resoreInstance();
        return actorObject.getDetailsAsHtml()
    }

    /**
     * Build HTML-link for the given object.
     *
     * @param object
     * @return
     */
    private String getHtmlMessage(NotificationObject object) {
		def actorObject = object.resoreInstance();

		if (!actorObject) {
			return "";
		}
		else if (actorObject instanceof String) {
			return actorObject
		}
		else {
            INotifiable actorNotifiable = (INotifiable) actorObject;
            return "<a href='${actorNotifiable.getLinkHref()}' class='notification-link'>${actorNotifiable.getNotificationName()}</a>"
		}
    }
}
