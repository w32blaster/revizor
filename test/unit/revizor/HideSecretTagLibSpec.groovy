package revizor

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.GroovyPageUnitTestMixin} for usage instructions
 */
@TestFor(HideSecretTagLib)
class HideSecretTagLibSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "taglib has no effect in case if a password is not presented"() {
        given:
            def url = "ssh://username@10.10.10.10/home/git/some-repo.git"
        when:
            def output = tagLib.maskPassword() { url }
        then:
            output.equals(url)
    }

    void "taglib hides password from ssh url"() {
        given:
            def url = "ssh://username:1]G@'16~j~[dY@10.10.10.10/home/git/some-repo.git"
        when:
            def output = tagLib.maskPassword() { url }
        then:
            output.equals("ssh://username:****@10.10.10.10/home/git/some-repo.git")
    }

    void "taglib hides password from ssh url even it has special chars"() {
        given:
           def url = "ssh://username:1]:G@'1:6~j@~[dY@10.10.10.10/home/git/some-repo.git"
        when:
            def output = tagLib.maskPassword() { url }
        then:
            output.equals("ssh://username:****@10.10.10.10/home/git/some-repo.git")
    }
}
