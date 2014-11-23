package com.revizor

/**
 * Issue represent an issue ticket from the selected Tracker.
 * One review may be associated with one (or few) issues in a registered
 * Trackers.
 */
class Issue {

    String key

    static belongsTo = [
            tracker: IssueTracker
    ]

    static constraints = {
    }
}
