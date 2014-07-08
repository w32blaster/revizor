package com.revizor

import com.revizor.NotificationObject
import com.revizor.User

class Notification {

    // who performed an action (main actor)
    User object

    // when it happened
    Date time

    // what happened
    Action action

    // which actor is used for details (this actor should inplement getDetailsAsHtml() method)
    int detailedActorIndex = -1

    // with whom/what (could be any object with any type)
    static hasMany = [
        actors: NotificationObject
    ]

    // show only to the main actor or to everybody? (for example, the message "you have 3 replies" is private)
    boolean showOnlyToReciepent

    static constraints = {
        
    }
}

/**
 * Action, that holds message code to be translated to the human-readable message.
 */
enum Action {
    CREATE_COMMENT_TO_REVIEW("action.comment.review.created"),
    CREATE_COMMENT_TO_LINE_OF_CODE("action.comment.code.created"),
    CREATE_COMMENT_REPLY_TO("action.comment.reply.created"),
    REVIEW_CLOSE("action.review.close"),
    REVIEW_INVITED_REVIEWER("action.review.invite.reviewer"),
    REVIEW_FINISH_WITH_DECISION("action.review.reviewer.finished.reviewing"),
    REVIEW_REVIEWER_CHANGED_HIS_MIND("action.review.reviewer.changed.his.mind"),
    REVIEW_START("action.review.start"),

    Action(String val) {
        this.value = val;
    }

    private final String value;
    public String value() { return value; }
}