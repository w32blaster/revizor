package com.revizor

import com.revizor.repos.IRepository
import grails.transaction.Transactional
import grails.util.Holders
import org.apache.commons.logging.LogFactory

/**
 * Job runs every few minutes and pulls the latest changes from an origin (remote repository).
 * It runs command like "git fetch" or "hg pull" (depending on realisation).
 *
 * The period of time is configurable
 *
 * @author w32blaster
 */
class PullCommitsJob {

    public static final int START_DELAY_MS = 30 * 1000;

    def log = LogFactory.getLog(this.class)
    def reviewService
    def grailsApplication

    static triggers = {
        simple startDelay: START_DELAY_MS,
               repeatInterval: Holders.config.grails.job.pull.period.time,
               repeatCount: -1
    }

    @Transactional
    def execute() {

        Repository.all.each { repo ->
            log.info("Pull job: update repo " + repo.getTitle())
            IRepository repoImpl = repo.initImplementation()
            def updatedCommits = repoImpl.updateRepo()
            reviewService.checkNewRevisionsForSmartCommits(updatedCommits, repo);
        }
    }
}
