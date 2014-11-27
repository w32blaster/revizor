package com.revizor.issuetracker

import com.revizor.Review

/**
 * Represents an abstract Issue Tracker.
 *
 */
interface ITracker {

    /**
     * Perform some optional actions before making any queries.
     * Typically, it is authentication.
     *
     */
    def before();

    /**
     * Request a data from an Issue Tracker implementation and
     * returns filled IssueTracker object
     *
     * @param key
     * @return
     */
    IssueTicket getIssueByKey(String key);

    /**
     * Notify tracker that a review is created in a Tracker's specific way.
     * Some trackers have section "reviews", so it could be saved there.
     * Otherwise this message could be appended to the description body or
     * left in the comments.
     *
     * @param key
     * @return
     */
    def notifyTrackerReviewCreated(String key, Review review);

    /**
     * Notify tracker that a review was closed in a specific tracker's way.
     * If API allows to get access to Ticket's history, it could be saved there.
     * Otherwise, any other place, like comments.
     * @param key
     * @return
     */
    def notifyTrackerReviewClosed(String key, Review review);

}