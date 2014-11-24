package com.revizor.issuetracker.impl

import com.revizor.IssueTracker
import com.revizor.IssueTrackerType
import com.revizor.issuetracker.ITracker
import com.revizor.issuetracker.IssueTicket
import grails.plugins.rest.client.RestBuilder
import org.codehaus.groovy.grails.web.json.JSONObject

/**
 * Created on 24/11/14.
 *
 * @author w32blaster
 */
class GitHubIssueTracker implements ITracker {

    private IssueTracker tracker;

    def rest = new RestBuilder(connectTimeout:1000, readTimeout:20000)

    public GitHubIssueTracker(IssueTracker issueTracker) {
        this.tracker = issueTracker;
    }

    @Override
    def before() {
        // there is no need to authenticate for public GutHub accounts.
        // Private premium accounts are not supported yet
    }

    /**
     * Example of URL: https://api.github.com/repos/w32blaster/revizor/issues/2
     *
     * @param key
     * @return
     */
    @Override
    IssueTicket getIssueByKey(String key) {

        def resp = rest.get(tracker.url + "/${key}")
        resp.json instanceof JSONObject

        return new IssueTicket(
                title: resp.json.title,
                tags: resp.json.labels.collect { it.name },
                status: resp.json.state,
                isClosed: resp.json.state == "closed",
                issueUrl: resp.json.html_url,
                authorImgUrl: resp.json.user.avatar_url,
                authorName: resp.json.user.login,
                trackerLogoUrl: IssueTrackerType.GITHUB.imageUrl
        )
    }
}
