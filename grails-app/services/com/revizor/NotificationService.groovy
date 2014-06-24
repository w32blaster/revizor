package com.revizor

import grails.transaction.Transactional
import com.revizor.Notification
import com.revizor.NotificationObject

class NotificationService {

    def create(who, action, args) {
        def u = new Notification(
                object: who,
                time: new Date(),
                action: action
            );

        args.eachWithIndex { arg, i ->
            u.addToActors(NotificationObject.saveObject(arg, i));   
        }

        u.save();
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
