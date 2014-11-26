package com.revizor

import com.revizor.issuetracker.ITracker
import com.revizor.issuetracker.impl.GitHubIssueTracker
import com.revizor.issuetracker.impl.JiraIssueTracker
import com.revizor.issuetracker.impl.YouTrackIssueTracker
import org.springframework.context.i18n.LocaleContextHolder as LCH

class IssueTracker {

    IssueTrackerType type
    String title
    String url
    String issueKeyPattern // <-- regex pattern for issue keys
    String username
    String password

    static hasMany = [
            usedIssueTickets: Issue // <-- list of issue tickets used in stored reviews
    ]

    static constraints = {

    }

    /**
     * Initiates correct implementation for the currently used repo
     */
    public ITracker initImplementation() {
        def grailsApplication = this.domainClass.grailsApplication
        def ctx = grailsApplication.mainContext
        def currentLocale = LCH.getLocale()

        switch(this.type) {
            case IssueTrackerType.JIRA:
                return new JiraIssueTracker(this, ctx, currentLocale);

            case IssueTrackerType.YOUTRACK:
                return new YouTrackIssueTracker(this, ctx, currentLocale);

            case IssueTrackerType.GITHUB:
                return new GitHubIssueTracker(this, ctx, currentLocale);

            default:
                throw new RuntimeException("the type of current issue tracker is not detected. " +
                        "Supported values: ${IssueTrackerType.values()}")
        }
    }


    @Override
    public String toString() {
        return "$title ($type)"
    }
}

enum IssueTrackerType {
    JIRA("jira.jpg"),
    YOUTRACK("youtrack.jpg"),
    GITHUB("github.jpg"),
    BITBUCKET("bitbucket.jpg")

    private String imgUrl;

    public IssueTrackerType(String url) {
        this.imgUrl = url;
    }

    public String getImageUrl() {
        return this.imgUrl;
    }
}
