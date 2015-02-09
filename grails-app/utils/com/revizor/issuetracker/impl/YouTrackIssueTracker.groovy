package com.revizor.issuetracker.impl

import com.revizor.IssueTracker
import com.revizor.IssueTrackerType
import com.revizor.Review
import com.revizor.Reviewer
import com.revizor.issuetracker.ITracker
import com.revizor.issuetracker.IssueTicket
import grails.plugins.rest.client.RestBuilder
import groovy.util.slurpersupport.GPathResult
import org.springframework.context.ApplicationContext
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.context.request.RequestContextHolder

/**
 * Realisation oj YouTrack
 *
 * https://www.jetbrains.com/youtrack/
 */
class YouTrackIssueTracker implements ITracker{

    private IssueTracker tracker;
    private cookies
    def rest = new RestBuilder(connectTimeout:1000, readTimeout:20000)
    private ApplicationContext context
    private Locale locale

    public YouTrackIssueTracker(IssueTracker issueTracker, ApplicationContext ctx, Locale locale) {
        this.tracker = issueTracker;
        this.context = ctx;
        this.locale = locale;
    }

    @Override
    def before() {
        def url = "${tracker.url}/rest/user/login/"

        MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>()
        form.add("login", tracker.username)
        form.add("password", tracker.password)
        def resp = rest.post(url) {
            contentType("application/x-www-form-urlencoded")
            body(form)
        }

        cookies = resp.headers.get("Set-Cookie");
    }

    @Override
    IssueTicket getIssueByKey(String key) {

        def resp = rest.get(tracker.url + "/rest/issue/${key}") {
            getHeaders().add("Cookie", cookies[0])
        }

        def issue = resp.xml as GPathResult
        def tags = issue.tag.list().collect { it.text() }
        def mapFields = issue.children().list()
                .groupBy { it.@name.text() }
                .collectEntries { k,v -> [k, v.value[0].text()] }

        return new IssueTicket(
                title: mapFields.get("summary"),
                tags: tags,
                status: mapFields.get("State"),
                isClosed: mapFields.get("State") == "Fixed",
                issueUrl: "${tracker.url}/issue/${key}",
                authorName: mapFields.get("ReporterFullName"),
                trackerLogoUrl: IssueTrackerType.YOUTRACK.imageUrl
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

        def session = RequestContextHolder.currentRequestAttributes().getSession()
        Object[] args = [ review.ident(),
                          review.getTitle(),
                          session.baseUrl + "/review/show/" + review.ident(),
                          review.author.username ] as Object[]

        def commentText = this.context.getMessage("youtrack.review.created.wiki.markup",
                args,
                this.locale)

        MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>()
        form.add("comment", commentText)
        def resp = rest.post("${tracker.url}/rest/issue/${key}/execute") {
            contentType("application/x-www-form-urlencoded")
            header("Cookie", cookies[0])
            body(form)
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

        def session = RequestContextHolder.currentRequestAttributes().getSession()

        def reviewersResultInWikiMarkup = "\n\n"
        review.reviewers.each { Reviewer reviewer ->
            reviewersResultInWikiMarkup += " * '''${reviewer.reviewer.username}''': ${reviewer.status}\n"
        }

        Object[] args = [ review.author.username,
                          review.ident(),
                          review.getTitle(),
                          session.baseUrl + "/review/show/" + review.ident(),
                          reviewersResultInWikiMarkup ] as Object[]

        def commentText = this.context.getMessage("youtrack.review.closed.wiki.markup",
                args,
                this.locale)

        MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>()
        form.add("comment", commentText)
        def resp = rest.post("${tracker.url}/rest/issue/${key}/execute") {
            contentType("application/x-www-form-urlencoded")
            header("Cookie", cookies[0])
            body(form)
        }
    }
}
