package com.revizor

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
 * Action, that holds message codes to be translated to the human-readable message.
 * There are three variations of each action: general, for me and by me.
 *
 * For example, action is comment reply. The same action will have three
 * variations depending on the currently logged user:
 *
 * - general: "User X replied to User Y with comment ..."
 * - forMe: "User X replied to you with comment..."
 * - byMe: "You replied to User Y with comment..."
 *
 */
enum Action {
    CREATE_COMMENT_TO_REVIEW(
            "action.comment.review.created",
            "action.comment.review.you.created",
            "action.comment.your.review.created"),
    CREATE_COMMENT_TO_LINE_OF_CODE(
            "action.comment.code.created",
            "action.comment.code.you.created",
            "action.comment.your.code.created"),
    CREATE_COMMENT_REPLY_TO(
            "action.comment.reply.created",
            "action.comment.you.replied",
            "action.comment.reply.to.you.created"),
    CREATE_REPOSITORY(
            "action.comment.new.repo",
            "action.comment.new.repo",
            "action.comment.new.repo"),
    REVIEW_CLOSE(
            "action.review.close",
            "action.review.you.closed",
            null),
    REVIEW_INVITED_REVIEWER(
            "action.review.invite.reviewer",
            "action.review.you.invited.reviewer",
            "action.review.invite.you.as.reviewer"),
    REVIEW_FINISH_WITH_DECISION(
            "action.review.reviewer.finished.reviewing",
            "action.review.you.finished.reviewing",
            null),
    REVIEW_REVIEWER_CHANGED_HIS_MIND(
            "action.review.reviewer.changed.his.mind",
            "action.review.you.changed.his.mind",
            null),
    REVIEW_START(
            "action.review.start",
            "action.review.you.started",
            null);

    // general message
    private final String value;
    // message when one of the actors is me
    private final String valueForMe;
    // message when I initiated this action
    private final String valueByMe;

    public Action(String val, String valByMe,  String valForMe) {
        this.value = val;
        this.valueByMe = valByMe;
        this.valueForMe = valForMe;
    }

    public String message() { return value; }
    public String messageByMe() { return valueByMe; }
    public String messageForMe() { return this.valueForMe ? valueForMe : value; }
}