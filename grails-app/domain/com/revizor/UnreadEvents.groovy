package com.revizor

public class UnreadEvents {

    ObjectType type;
    Long objectId;

    static belongsTo = [
            user: User
    ]

    static hasOne = [
        notification: Notification
    ]

    static constraints = {
        type(nullable: false)
        objectId(nullable: false)
        user(nullable: false)
    }
}
