package com.revizor.issuetracker.impl

import com.revizor.IssueTracker
import com.revizor.Review
import com.revizor.issuetracker.ITracker
import com.revizor.issuetracker.IssueTicket
import org.springframework.context.ApplicationContext

/**
 * Created on 19/11/14.
 *
 * @author w32blaster
 */
class JiraIssueTracker implements ITracker {

    private IssueTracker tracker;
    private ApplicationContext context
    private Locale locale

    public JiraIssueTracker(IssueTracker issueTracker, ApplicationContext ctx, Locale locale) {
        this.tracker = issueTracker;
        this.context = ctx;
        this.locale = locale;
    }

    @Override
    def before() {
        return null
    }

    @Override
    IssueTicket getIssueByKey(String key) {
        return null
    }

    /**
     * Notify tracker that a review is created in a Tracker's specific way.
     * Some trackers have section "reviews", so it could be saved there.
     * Otherwise this message could be appended to the description body or
     * left in the comments.
     *
     * @param key
     * @return
     */
    @Override
    def notifyTrackerReviewCreated(String key, Review review) {
        return null
    }

    /**
     * Notify tracker that a review was closed in a specific tracker's way.
     * If API allows to get access to Ticket's history, it could be saved there.
     * Otherwise, any other place, like comments.
     * @param key
     * @return
     */
    @Override
    def notifyTrackerReviewClosed(String key, Review review) {
        return null
    }
}
