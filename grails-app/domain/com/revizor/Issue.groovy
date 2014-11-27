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
        key(nullable: false)
        tracker(nullable: false)
    }


    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        Issue issue = (Issue) o

        if (key != issue.key) return false
        if (tracker.ident() != issue.tracker.ident()) return false

        return true
    }

    int hashCode() {
        int result
        result = key.hashCode()
        result = 31 * result + tracker.ident().hashCode()
        return result
    }
}
