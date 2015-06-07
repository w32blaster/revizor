package revizor

import com.revizor.Reviewer
import com.revizor.User
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * 
 */
@Mock([Reviewer, User])
@TestFor(HelpTagLib)
class HelpTagLibSpec extends Specification {

    void "taglib renders the simple content"() {
        when:
            views['/simple/_snippet.gsp'] = 'trololo'

        and:
            def output = tagLib.renderInOneLine(['template': '/simple/snippet'])

        then: 'as long as the content is very simple and has only one line, the output will not be changed'
            output == 'trololo'
    }

    void "taglib renders the correctly simple html"() {
        when:
            views['/simple/_snippet.gsp'] = '<div class="some-class"></div>'

        and:
            def output = tagLib.renderInOneLine(['template': '/simple/snippet'])

        then: 'output will be the same, because it is written only in one line'
            output == '<div class="some-class"></div>'
    }

    void "taglib cuts trailing whitespaces"() {
        when:
            views['/simple/_snippet.gsp'] = '    <div class="some-class"></div>    '

        and:
            def output = tagLib.renderInOneLine(['template': '/simple/snippet'])

        then:
            output == '<div class="some-class"></div>'
    }

    void "taglib renders the correctly multiline html to one line"() {
        when:
            views['/simple/_snippet.gsp'] = """

                    <div id="some-id" class="some-class">
                        <span class="span-class">
                            <h1>Header<h2>
                            <p>The content</p>
                        </span>
                    </div>
                    
                """

        and:
            def output = tagLib.renderInOneLine(['template': '/simple/snippet'])

        then: 'output will be the same, because it is written only in one line'
            output == '<div id="some-id" class="some-class"><span class="span-class"><h1>Header<h2><p>The content</p></span></div>'
    }
}
