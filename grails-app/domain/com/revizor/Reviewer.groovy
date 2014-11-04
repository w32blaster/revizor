package com.revizor

import com.revizor.utils.Id

/*
 * Incapsulates the term "reviewer". in other words, this is realisation of many-to-many.
 * One user can be as a reviewer for many reviews and have a different decisions ('statuses',
 * 'oppinions' or 'resolution') for each review.
 */
class Reviewer {

    ReviwerStatus status
    // reviewer's final opinion about finished review
    // String resolution

    static belongsTo = [
        reviewer: User,
        review: Review 
    ]

    static mapping = {
        reviewer lazy: false
        review lazy: false
    }

    static constraints = {
    }
}

/**
 * Means status of each reviewer for the current review
 */
public enum ReviwerStatus {
    INVITED,
    APPROVE,
    DISAPPROVE

    static ReviwerStatus valueOfName(name) {
        values().find { 
            it.toString() == name
        }
    }

    /**
     * Returns the CSS style for the icon "thumb-up" or "thumb-down" depending on the reviewer resolution
     */
    public String getThumbIconStyle() {
        switch(this) {
            case APPROVE:                
                return Id.THUMB_UP;

            case DISAPPROVE:
                return Id.THUMB_DOWN;
            
            default:
                return "";
        }
    }
}