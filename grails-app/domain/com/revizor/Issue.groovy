package com.revizor

/**
 * Issue represent an issue ticket from the selected Tracker.
 * One review may be associated with one (or few) issues in a registered
 * Trackers. One record in this table means one association (when a review
 * is linked to any issue ticket)
 */
class Issue {

    String key

    static belongsTo = [
            tracker: IssueTracker
    ]

    static constraints = {
    }
}
