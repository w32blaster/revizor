package com.revizor.issuetracker.impl

import com.revizor.IssueTracker
import com.revizor.IssueTrackerType
import com.revizor.Review
import com.revizor.ReviewStatus
import com.revizor.issuetracker.ITracker
import com.revizor.issuetracker.IssueTicket
import grails.plugins.rest.client.RestBuilder
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.springframework.context.ApplicationContext

/**
 * Implementation for Jira issue tracker.
 *
 * API Docs
 *
 * Remote link:
 * https://developer.atlassian.com/jiradev/jira-platform/other/guide-jira-remote-issue-links/jira-rest-api-for-remote-issue-links
 * https://developer.atlassian.com/jiradev/jira-platform/other/guide-jira-remote-issue-links/fields-in-remote-issue-links
 *
 * API:
 * https://developer.atlassian.com/static/rest/jira/6.1.html
 *
 * Created on 19/11/14.
 *
 * @author w32blaster
 */
class JiraIssueTracker implements ITracker {

    private IssueTracker tracker;
    private GrailsApplication grailsApplication
    private ApplicationContext context
    private Locale locale

    def rest = new RestBuilder(connectTimeout:1000, readTimeout:20000)
    def authzBase64

    public JiraIssueTracker(IssueTracker issueTracker, GrailsApplication grailsApplication, Locale locale) {
        this.tracker = issueTracker;
        this.grailsApplication = grailsApplication
        this.context = grailsApplication.mainContext
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

        if (resp.status == 404) {
            return null
        }
        else {
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

        def logoUrl = grailsApplication.config.links.images.logo16x16

        rest.post(tracker.url + "/rest/api/latest/issue/${key}/remotelink") {
            getHeaders().add("Authorization", "Basic ${this.authzBase64}")
            json """{
                "globalId": "system=${grailsApplication.config.grails.serverURL}/review/show/${review.id}",
                "application": {
                     "type":"com.revizor",
                     "name":"Revizor"
                },
                "relationship":"code review",
                "object": {
                    "url":"${grailsApplication.config.grails.serverURL}/review/show/${review.id}",
                    "title":"${review.title}",
                    "summary":"${review.description}",
                    "icon": {
                        "url16x16":"${logoUrl}",
                        "title":"Revizor"
                    },
                    "status": {
                        "resolved": ${review.status == ReviewStatus.CLOSED}
                    }
                }
            }"""

        }
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

        def logoUrl = grailsApplication.config.links.images.logo16x16

        rest.post(tracker.url + "/rest/api/latest/issue/${key}/remotelink") {
            getHeaders().add("Authorization", "Basic ${this.authzBase64}")
            json """{
                "globalId": "system=${grailsApplication.config.grails.serverURL}/review/show/${review.id}",
                "application": {
                     "type":"com.revizor",
                     "name":"Revizor"
                },
                "relationship":"code review",
                "object": {
                    "url":"${grailsApplication.config.grails.serverURL}/review/show/${review.id}",
                    "title":"${review.title}",
                    "summary":"${review.description}",
                    "icon": {
                        "url16x16":"${logoUrl}",
                        "title":"Revizor"
                    },
                    "status": {
                        "resolved": true,
                        "icon": {
                            "url16x16":"http://www.openwebgraphics.com/resources/data/47/accept.png",
                            "title":"Review is Closed",
                            "link":"${grailsApplication.config.grails.serverURL}/review/show/${review.id}"
                        }
                    }
                }
            }"""
        }

    }
}
