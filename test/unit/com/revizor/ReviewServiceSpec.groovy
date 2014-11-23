package com.revizor

import com.revizor.repos.Commit
import com.revizor.utils.Constants
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@Mock(IssueTracker)
@TestFor(ReviewService)
class ReviewServiceSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "test extracting header where smart commit is on the first line"() {

        given:
            def multiline = """This is the first line of commit message ${Constants.SMART_COMMIT_CREATE_REVIEW}

                Here goes the rest text of a commit.
            """
        when:
            def arr = service.getHeaderAndMessage(new Commit(fullMessage: multiline))

        then: "the header is the first line without smart commit taf"
            arr[0] == "This is the first line of commit message"
            arr[1] == "Here goes the rest text of a commit."
    }

    void "test extracting header where smart commit is on the last line"() {

        given:
            def multiline = """This is the first line of commit message.

Here goes the rest text of a commit. But the
commit tag is placed on the last line,
namely here: ${Constants.SMART_COMMIT_CREATE_REVIEW}
                """
        when:
            def arr = service.getHeaderAndMessage(new Commit(fullMessage: multiline))

        then: "the header is the first line"
            arr[0] == "This is the first line of commit message."
            arr[1] == "Here goes the rest text of a commit. But the\ncommit tag is placed on the last line,\nnamely here: "
    }




    void "test extracting header where commit message in only one line"() {

        given:
            def singleline = "This is the single line commit message ${Constants.SMART_COMMIT_CREATE_REVIEW}"
        when:
            def arr = service.getHeaderAndMessage(new Commit(fullMessage: singleline))

        then: "the header is the message without smart commit tag"
            arr[0] == "This is the single line commit message"

        and: "default message because the commit has only one line"
            arr[1] == "no description"
    }

    void "test extracting emails of reviewers"() {

        given:
            def singleline = "This is the single line commit message ${Constants.SMART_COMMIT_CREATE_REVIEW} max@email.com,alice@email.com"
        when:
            def arr = service.getHeaderAndMessage(new Commit(fullMessage: singleline))

        then:
            arr[0] == "This is the single line commit message"

        and:
            arr[1] == "no description"

        and: 'both emails are extracted to list'
            arr[2] == ['max@email.com', 'alice@email.com']
    }

    void "test extracting emails of reviewers separated with comma and blank"() {

        given: ""
            def singleline = "This is the single line commit message ${Constants.SMART_COMMIT_CREATE_REVIEW} max@email.com, alice@email.com"
        when:
            def arr = service.getHeaderAndMessage(new Commit(fullMessage: singleline))

        then:
            arr[0] == "This is the single line commit message"

        and:
            arr[1] == "no description"

        and: 'both emails are extracted to list'
            arr[2] == ['max@email.com', 'alice@email.com']
    }

    void "detects one issue ticket added as smart commit"() {
        given:
            def issueTracker = new IssueTracker(
                    type: IssueTrackerType.YOUTRACK,
                    title: "Testing issue tracker",
                    url: "https://localhost",
                    issueKeyPattern: "TRCKER-{1}\\d+",
                    username: "user",
                    password: "password"
                )
            issueTracker.save()

        and:
            def commit = new Commit(fullMessage: "The test message #TRCKER-9000")

        when:
            def issues = service.getIssueTickets(commit)

        then:
            issues.size() == 1
            issues[0].key == "TRCKER-9000"
            issues[0].tracker == issueTracker
    }

    void "detects few issues added as smart commit"() {
        given:
            def issueTracker = new IssueTracker(
                    type: IssueTrackerType.YOUTRACK,
                    title: "Testing issue tracker",
                    url: "https://localhost",
                    issueKeyPattern: "TRCKR-{1}\\d+",
                    username: "user",
                    password: "password"
            )
            issueTracker.save()

        and:
            def commit = new Commit(fullMessage: "The test message #TRCKR-9000 #TRCKR-9001")

        when:
            def issues = service.getIssueTickets(commit)

        then: 'two tickets are found'
            issues.size() == 2

        and:
            issues[0].key == "TRCKR-9000"
            issues[0].tracker == issueTracker

        and:
            issues[1].key == "TRCKR-9001"
            issues[1].tracker == issueTracker
    }

    void "detects few issues belongoin to different trackers added as smart commit"() {

        given: 'one internal issue tracker'
            def issueTrackerYouTrack = new IssueTracker(
                    type: IssueTrackerType.YOUTRACK,
                    title: "YouTrack issue tracker",
                    url: "https://localhost",
                    issueKeyPattern: "TRCKR-{1}\\d+",
                    username: "user",
                    password: "password"
            )
            issueTrackerYouTrack.save()

        and: 'one public'
            def issueTrackerGithub = new IssueTracker(
                    type: IssueTrackerType.GITHUB,
                    title: "GitHub issue tracker",
                    url: "https://github.com/w32blaster/revizor/issues",
                    issueKeyPattern: "\\d+", // <-- only number
                    username: "user",
                    password: "password"
            )
            issueTrackerGithub.save()

        and:
            def commit = new Commit(fullMessage: "Added new feature as described in #TRCKR-47 and related to #85 at github")

        when:
            def issues = service.getIssueTickets(commit)

        then: 'two tickets are found'
            issues.size() == 2

        and: 'from Youtrack issue tracker'
            issues[0].key == "TRCKR-47"
            issues[0].tracker == issueTrackerYouTrack

        and: 'from github'
            issues[1].key == "85"
            issues[1].tracker == issueTrackerGithub
    }

    void "detects few issues belonging to different trackers added as smart commit in several lines"() {

        given: 'one internal issue tracker'
            def issueTrackerYouTrack = new IssueTracker(
                    type: IssueTrackerType.YOUTRACK,
                    title: "YouTrack issue tracker",
                    url: "https://localhost",
                    issueKeyPattern: "TRCKR-{1}\\d+",
                    username: "user",
                    password: "password"
            )
            issueTrackerYouTrack.save()

        and: 'one public'
            def issueTrackerGithub = new IssueTracker(
                    type: IssueTrackerType.GITHUB,
                    title: "GitHub issue tracker",
                    url: "https://github.com/w32blaster/revizor/issues",
                    issueKeyPattern: "\\d+", // <-- only number
                    username: "user",
                    password: "password"
            )
            issueTrackerGithub.save()

        and:

        def multiline = """This is the first line of commit message.

Here goes the rest text of a commit. And on the last
line we can find tags for issue keys
#TRCKR-47 #85
                """
        and:
            def commit = new Commit(fullMessage: multiline)

        when:
            def issues = service.getIssueTickets(commit)

        then: 'two tickets are found'
            issues.size() == 2

        and: 'from Youtrack issue tracker'
            issues[0].key == "TRCKR-47"
            issues[0].tracker == issueTrackerYouTrack

        and: 'from github'
            issues[1].key == "85"
            issues[1].tracker == issueTrackerGithub
    }


    void "detects few issues belongoin to different trackers added as smart commit with similag patterns"() {

        given: 'one internal issue tracker'
            def issueTrackerYouTrack = new IssueTracker(
                    type: IssueTrackerType.YOUTRACK,
                    title: "YouTrack issue tracker",
                    url: "https://localhost",
                    issueKeyPattern: "\\d{4}-{1}\\d{2}",
                    username: "user",
                    password: "password"
            )
            issueTrackerYouTrack.save()

        and: 'one public'
            def issueTrackerGithub = new IssueTracker(
                    type: IssueTrackerType.GITHUB,
                    title: "GitHub issue tracker",
                    url: "https://github.com/w32blaster/revizor/issues",
                    issueKeyPattern: "\\d+", // <-- only number
                    username: "user",
                    password: "password"
            )
            issueTrackerGithub.save()

        and:
            def commit = new Commit(fullMessage: "Added new feature as described in #1402-68 and related to #85 at github")

        when:
            def issues = service.getIssueTickets(commit)

        then: 'two tickets are found'
            issues.size() == 2

        and: 'from Youtrack issue tracker'
            issues[0].key == "1402-68"
            issues[0].tracker == issueTrackerYouTrack

        and: 'from github'
            issues[1].key == "85"
            issues[1].tracker == issueTrackerGithub
    }

}
