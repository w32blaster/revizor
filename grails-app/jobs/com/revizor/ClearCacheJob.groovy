package com.revizor

/**
 * Job to clear a cache.
 *
 * Since there is no way to configure "time to live" with Grails plugin,
 * all cached items have no timeout and remain cached. We need to clear them using job.
 *
 * @see http://grails-plugins.github.io/grails-cache/guide/usage.html#dsl
 */
class ClearCacheJob {

    def issueTrackerService

    static triggers = {
      simple repeatInterval: 1000l * 60 * 20 // execute job once in 20 minutes
    }

    def execute() {
        issueTrackerService.clearCacheIssueTickets()
    }
}
