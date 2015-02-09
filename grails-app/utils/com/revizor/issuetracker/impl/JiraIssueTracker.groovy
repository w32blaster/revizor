package com.revizor.issuetracker.impl

import com.revizor.IssueTracker
import com.revizor.IssueTrackerType
import com.revizor.Review
import com.revizor.issuetracker.ITracker
import com.revizor.issuetracker.IssueTicket
import grails.plugins.rest.client.RestBuilder
import groovy.util.slurpersupport.GPathResult
import org.springframework.context.ApplicationContext
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap

/**
 * Created on 19/11/14.
 *
 * @author w32blaster
 */
class JiraIssueTracker implements ITracker {

    private IssueTracker tracker;
    private ApplicationContext context
    private Locale locale

    def rest = new RestBuilder(connectTimeout:1000, readTimeout:20000)
    def authzBase64

    public JiraIssueTracker(IssueTracker issueTracker, ApplicationContext ctx, Locale locale) {
        this.tracker = issueTracker;
        this.context = ctx;
        this.locale = locale;
    }

    @Override
    def before() {
        this.authzBase64 = "${this.tracker.getUsername()}:${this.tracker.getPassword()}".bytes.encodeBase64().toString()
    }

    @Override
    IssueTicket getIssueByKey(String key) {

        def resp = rest.get(tracker.url + "/rest/api/latest/issue/${key}") {
            getHeaders().add("Authorization", "Basic ${this.authzBase64}")
        }

        return new IssueTicket(
                title: resp.json.fields.summary,
                tags: resp.json.fields.labels,
                status: resp.json.fields.issuetype.name,
                statusImgUrl: resp.json.fields.status.iconUrl,
                isClosed: resp.json.fields.issuetype.name == "Done",
                issueUrl: "${tracker.url}/browse/${key}",
                authorName: resp.json.fields.creator.displayName,
                authorImgUrl: resp.json.fields.creator.avatarUrls["32x32"],
                trackerLogoUrl: IssueTrackerType.JIRA.imageUrl
        )
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
