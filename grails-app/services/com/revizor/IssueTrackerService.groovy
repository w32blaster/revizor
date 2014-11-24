package com.revizor

import com.revizor.issuetracker.ITracker
import com.revizor.issuetracker.IssueTicket
import grails.transaction.Transactional

@Transactional
class IssueTrackerService {

    IssueTicket serviceMethod(Issue issue) {
        ITracker issueTracker = issue.tracker.initImplementation()

        // make a remote call to Tracker's API and retrieve the details about the given issue
        issueTracker.before()
        return issueTracker.getIssueByKey(issue.key)
    }
}
