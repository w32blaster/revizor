package com.revizor

import com.revizor.repos.Commit
import com.revizor.utils.Constants
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
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
}
