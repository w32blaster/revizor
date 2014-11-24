package com.revizor.issuetracker.impl

import com.revizor.IssueTracker
import com.revizor.issuetracker.ITracker
import com.revizor.issuetracker.IssueTicket

/**
 * Realisation oj YouTrack
 *
 * https://www.jetbrains.com/youtrack/
 */
class YouTrackIssueTracker implements ITracker{

    private IssueTracker tracker;

    public YouTrackIssueTracker(IssueTracker issueTracker) {
        this.tracker = issueTracker;
    }

    @Override
    def before() {
        return null
    }

    @Override
    IssueTicket getIssueByKey(String key) {
        return null
    }
}
