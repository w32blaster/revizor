package com.revizor.issuetracker.impl

import com.revizor.IssueTracker
import com.revizor.IssueTrackerType
import com.revizor.issuetracker.ITracker
import com.revizor.issuetracker.IssueTicket
import grails.plugins.rest.client.RestBuilder
import groovy.util.slurpersupport.GPathResult
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap

/**
 * Realisation oj YouTrack
 *
 * https://www.jetbrains.com/youtrack/
 */
class YouTrackIssueTracker implements ITracker{

    private IssueTracker tracker;
    private cookies
    def rest = new RestBuilder(connectTimeout:1000, readTimeout:20000)

    public YouTrackIssueTracker(IssueTracker issueTracker) {
        this.tracker = issueTracker;
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
        println cookies
    }

    @Override
    IssueTicket getIssueByKey(String key) {


        def resp = rest.get(tracker.url + "/rest/issue/${key}?wikifyDescription=true") {
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
}
