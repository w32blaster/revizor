package com.revizor

public class UnreadEvent {

    ObjectType type;
    Long objectId;

    static belongsTo = [
            user: User,
            notification: Notification
    ]

    static constraints = {
        type(nullable: false)
        objectId(nullable: false)
        user(nullable: false)
    }
}
