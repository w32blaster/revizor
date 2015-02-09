package com.revizor

import com.revizor.issuetracker.ITracker
import com.revizor.issuetracker.IssueTicket
import grails.plugin.cache.CacheEvict
import grails.plugin.cache.Cacheable
import grails.transaction.Transactional

@Transactional
class IssueTrackerService {

    /**
     * Gets remote information about a given issue.
     *
     * As long as on every invocation we make a remote call,
     * that is potentially expensive operation, we need to
     * cache results in the internal cache. Furthermore,
     * usually header and description are rarely changed,
     * so we can assume that cached tickets stay
     * actual for a long time.
     *
     * @param issue
     * @return
     */
    //@Cacheable('issueTrackerTickets')
    IssueTicket getIssueTicket(Issue issue) {
        ITracker issueTracker = issue.tracker.initImplementation()

        // make a remote call to Tracker's API and retrieve the details about the given issue
        issueTracker.before()
        return issueTracker.getIssueByKey(issue.key)
    }

    /**
     * Method is called by ClearCache Job
     *
     * @return
     */
    @CacheEvict(value='issueTrackerTickets', allEntries=true)
    def clearCacheIssueTickets() {
        log.info("Cache for Issue Tickets was cleaned")
    }

}
