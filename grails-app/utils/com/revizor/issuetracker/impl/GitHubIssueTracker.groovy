package com.revizor.issuetracker.impl

import com.revizor.IssueTracker
import com.revizor.IssueTrackerType
import com.revizor.Review
import com.revizor.issuetracker.ITracker
import com.revizor.issuetracker.IssueTicket
import grails.plugins.rest.client.RestBuilder
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.web.json.JSONObject
import org.springframework.context.ApplicationContext

/**
 * Created on 24/11/14.
 *
 * @author w32blaster
 */
class GitHubIssueTracker implements ITracker {

    private IssueTracker tracker;

    def rest = new RestBuilder(connectTimeout:1000, readTimeout:20000)
    private ApplicationContext context
    private GrailsApplication grailsApplication
    private Locale locale

    public GitHubIssueTracker(IssueTracker issueTracker, GrailsApplication grailsApplication, Locale locale) {
        this.tracker = issueTracker;
        this.grailsApplication = grailsApplication
        this.context = grailsApplication.mainContext
        this.locale = locale;
    }

    @Override
    def before() {
        // get access token using OAuth
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

        def url = "${tracker.url}/${key}/comments"

        Object[] args = [ review.getTitle(), "url", review.author.username ] as Object[]
        def commentText = this.context.getMessage("github.review.created.markdown",
                                    args,
                                    this.locale)

        println(url)

        /*

        Authentication is needed;

        Issue: https://github.com/w32blaster/revizor/issues/17

        MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>()
        form.add("body", commentText)
        def resp = rest.post(url) {
            contentType("application/x-www-form-urlencoded")
            header("Accept", "application/vnd.github.v3.full+json")
            body(form)
        }
        if ((resp.responseEntity.statusCode.value as Integer) != 200) {
            throw new RuntimeException(resp.responseEntity.statusCode.reasonPhrase)
        }

        */
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
