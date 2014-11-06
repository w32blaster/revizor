package com.revizor

import grails.transaction.Transactional
import com.revizor.Notification
import com.revizor.NotificationObject

class NotificationService {

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
        def u = new Notification(
                object: who,
                time: new Date(),
                action: action,
                detailedActorIndex: actorIndexToBeUsedForDetails
            );

        actors.eachWithIndex { actor, i ->
            u.addToActors(NotificationObject.saveObject(actor, i));   
        }

        u.save(flush: true, failOnError: true);
    }

    def feed(max, offset) {
        
        def all = Notification.withCriteria{
            maxResults max
            firstResult offset
            order("time", "desc")
        }

        return all
    }
}
