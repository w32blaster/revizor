package com.revizor.issuetracker.impl

import com.revizor.IssueTracker
import com.revizor.issuetracker.ITracker
import com.revizor.issuetracker.IssueTicket

/**
 * Created on 19/11/14.
 *
 * @author w32blaster
 */
class JiraIssueTracker implements ITracker {

    private IssueTracker tracker;

    public JiraIssueTracker(IssueTracker issueTracker) {
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
