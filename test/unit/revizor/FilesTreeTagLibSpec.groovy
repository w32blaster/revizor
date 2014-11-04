package revizor

import com.revizor.utils.Constants
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.GroovyPageUnitTestMixin} for usage instructions
 */
@TestFor(FilesTreeTagLib)
class FilesTreeTagLibSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }


    def "type of each file record is correctly recognyzed" () {
        when:
        def status = tagLib.getTypeOfLine(fileDiffLine)
        then:
        status == expectedStatus
        where:
        fileDiffLine                                          | expectedStatus
        '--- a/grails-app/taglib/revizor/DiffTagLib.groovy'   | Constants.ACTION_MODIFIED
        '--- a/grails-app/i18n/messages.properties'           | Constants.ACTION_MODIFIED
        '+++ b/grails-app/taglib/revizor/Comment.groovy'      | Constants.ACTION_MODIFIED
        '+++ b/grails-app/i18n/messages.properties'           | Constants.ACTION_MODIFIED
        '--- /dev/null'                                       | Constants.ACTION_ADDED
        '+++ /dev/null'                                       | Constants.ACTION_DELETED
    }

    def "builds tree with only one added file"() {

        given: 'only one file that was added'
            def files = [
                    '--- /dev/null',
                    '+++ b/grails-app/views/review/_comments.gsp'
            ]
        when:
            def result = tagLib.buildMapOfFiles(files)

        then: 'expect to see nested map and the file marked as "added"'

            // root
            NodeFile root = new NodeFile('name': 'root', 'parent': null, 'children': [])

            // level 1
            NodeFile grailsapp = new NodeFile('name': 'grails-app', 'parent': root, 'children': [])

            // level 2
            NodeFile view = new NodeFile('name': 'views', 'parent': grailsapp, 'children': [])

            // level 3
            NodeFile review = new NodeFile('name': 'review', 'parent': view, 'children': [])

            // file
            NodeFile file = new NodeFile('name': '_comments.gsp', 'isLeaf': true, 'parent': review,
                    'status': Constants.ACTION_ADDED, 'fullPath': 'grails-app/views/review/_comments.gsp')

            review.children.add(file)
            view.children.add(review)
            grailsapp.children.add(view)
            root.children.add(grailsapp)

            root.equals(result)
    }

    def "builds tree with only one removed file"() {

        given: 'only one file that was removed'
            def files = [
                    '--- a/grails-app/views/review/_comments.gsp',
                    '+++ /dev/null'
            ]
        when:
            def result = tagLib.buildMapOfFiles(files)

            then: 'expect to see nested map and the file marked as "deleted"'

            // root
            NodeFile root = new NodeFile('name': 'root', 'parent': null, 'children': [])

            // level 1
            NodeFile grailsapp = new NodeFile('name': 'grails-app', 'parent': root, 'children': [])

            // level 2
            NodeFile view = new NodeFile('name': 'views', 'parent': grailsapp, 'children': [])

            // level 3
            NodeFile review = new NodeFile('name': 'review', 'parent': view, 'children': [])

            // file
            NodeFile file = new NodeFile('name': '_comments.gsp', 'isLeaf': true, 'parent': review,
                    'status': Constants.ACTION_DELETED, 'fullPath': 'grails-app/views/review/_comments.gsp')

            review.children.add(file)
            view.children.add(review)
            grailsapp.children.add(view)
            root.children.add(grailsapp)

            root.equals(result)
    }

    def "builds tree with only one modified file"() {

        given: 'only one file that was modified'
            def files = [
                    '--- a/grails-app/taglib/revizor/DiffTagLib.groovy',
                    '+++ b/grails-app/taglib/revizor/DiffTagLib.groovy'
            ]
        when:
            def result = tagLib.buildMapOfFiles(files)

        then: 'expect to see nested map and the file marked as "modified"'

            NodeFile root = new NodeFile('name': 'root', 'parent': null, 'children': [])

            // level 1
            NodeFile grailsapp = new NodeFile('name': 'grails-app', 'parent': root, 'children': [])

            // level 2
            NodeFile taglib = new NodeFile('name': 'taglib', 'parent': grailsapp, 'children': [])

            // level 3
            NodeFile revizor = new NodeFile('name': 'revizor', 'parent': taglib, 'children': [])

            // file
            NodeFile file = new NodeFile('name': 'DiffTagLib.groovy', 'isLeaf': true, 'parent': revizor,
                    'status': Constants.ACTION_MODIFIED, 'fullPath': 'grails-app/taglib/revizor/DiffTagLib.groovy')

            revizor.children.add(file)
            taglib.children.add(revizor)
            grailsapp.children.add(taglib)
            root.children.add(grailsapp)

            root.equals(result)
    }

    def "builds tree with only two modified files and one added"() {

        given: 'three files in the list'
            def files = [
                    '--- a/grails-app/taglib/revizor/DiffTagLib.groovy',
                    '+++ b/grails-app/taglib/revizor/DiffTagLib.groovy',
                    '--- a/grails-app/domain/com/revizor/Comment.groovy',
                    '+++ b/grails-app/domain/com/revizor/Comment.groovy',
                    '--- /dev/null',
                    '+++ b/grails-app/taglib/revizor/CommentsTagLib.groovy'
            ]
        when:
            def result = tagLib.buildMapOfFiles(files)

        then: 'expect to see nested map, files have common parent node "grails-app" and two have common parent "tagLib"'

            NodeFile root = new NodeFile('name': 'root', 'parent': null, 'children': [])

            // level 1
            NodeFile grailsapp = new NodeFile('name': 'grails-app', 'parent': root, 'children': [])

            // level 2
            NodeFile taglib = new NodeFile('name': 'taglib', 'parent': grailsapp, 'children': [])
            NodeFile domain = new NodeFile('name': 'domain', 'parent': grailsapp, 'children': [])

            // level 3
            NodeFile revizor = new NodeFile('name': 'revizor', 'parent': taglib, 'children': [])
            NodeFile com = new NodeFile('name': 'com', 'parent': domain, 'children': [])

            // Level 4
            NodeFile revizor2 = new NodeFile('name': 'revizor', 'parent': com, 'children': [])

            // files
            NodeFile file = new NodeFile('name': 'DiffTagLib.groovy', 'isLeaf': true, 'parent': revizor,
                    'status': Constants.ACTION_MODIFIED, 'fullPath': 'grails-app/taglib/revizor/DiffTagLib.groovy')
            NodeFile file2 = new NodeFile('name': 'Comment.groovy', 'isLeaf': true, 'parent': revizor2,
                    'status': Constants.ACTION_MODIFIED, 'fullPath': 'grails-app/domain/com/revizor/Comment.groovy')
            NodeFile file3 = new NodeFile('name': 'CommentsTagLib.groovy', 'isLeaf': true, 'parent': revizor,
                    'status': Constants.ACTION_ADDED, 'fullPath': 'grails-app/taglib/revizor/CommentsTagLib.groovy')

            revizor.children.add(file)
            revizor2.children.add(file2)
            revizor.children.add(file3)

            taglib.children.add(revizor)
            grailsapp.children.add(taglib)

            com.children.add(revizor2)
            domain.children.add(com)
            grailsapp.children.add(domain)

            root.children.add(grailsapp)

            and: 'result must be equal the tree'
            root.equals(result)

    }

    def "build tree where two files in the different directory belong to the different map level"() {

        given: 'two files in the same directory'
            def files = [
                    '--- a/grails-app/taglib/revizor/DiffTagLib.groovy',
                    '+++ b/grails-app/taglib/revizor/DiffTagLib.groovy',
                    '--- a/grails-app/utils/com/revizor/utilities/Constants.java',
                    '+++ b/grails-app/utils/com/revizor/utilities/Constants.java'
            ]
        when:
            def result = tagLib.buildMapOfFiles(files)

        then: ' expect to see nested map, files have only one common parent node "grails-app" and different subtrees'

            NodeFile root = new NodeFile('name': 'root', 'parent': null, 'children': [])

            // level 1
            NodeFile grailsapp = new NodeFile('name': 'grails-app', 'parent': root, 'children': [])

            // level 2
            NodeFile taglib = new NodeFile('name': 'taglib', 'parent': grailsapp, 'children': [])
            NodeFile utils = new NodeFile('name': 'utils', 'parent': grailsapp, 'children': [])

            // level 3
            NodeFile revizor = new NodeFile('name': 'revizor', 'parent': taglib, 'children': [])
            NodeFile com = new NodeFile('name': 'com', 'parent': utils, 'children': [])

            // Level 4
            NodeFile revizor2 = new NodeFile('name': 'revizor', 'parent': com, 'children': [])

            // Level 5
            NodeFile utilities = new NodeFile('name': 'utilities', 'parent': revizor2, 'children': [])

            // files
            NodeFile file = new NodeFile('name': 'DiffTagLib.groovy', 'isLeaf': true, 'parent': revizor,
                    'status': Constants.ACTION_MODIFIED, 'fullPath': 'grails-app/taglib/revizor/DiffTagLib.groovy')
            NodeFile file2 = new NodeFile('name': 'Constants.java', 'isLeaf': true, 'parent': utilities,
                    'status': Constants.ACTION_MODIFIED, 'fullPath': 'grails-app/utils/com/revizor/utilities/Constants.java')

            revizor.children.add(file)
            taglib.children.add(revizor)
            grailsapp.children.add(taglib)

            utilities.children.add(file2)
            revizor2.children.add(utilities)
            com.children.add(revizor2)
            utils.children.add(com)
            grailsapp.children.add(utils)

            root.children.add(grailsapp)

        and: 'result must be equal the tree'
            root.equals(result)
    }

    def "build tree where two files in the same directory belong to the same map level"() {

        given: 'two files in the same directory'
            def files = [
                    '--- a/grails-app/taglib/revizor/DiffTagLib.groovy',
                    '+++ b/grails-app/taglib/revizor/DiffTagLib.groovy',
                    '--- a/grails-app/taglib/revizor/Comment.groovy',
                    '+++ b/grails-app/taglib/revizor/Comment.groovy'
            ]
        when:
            def result = tagLib.buildMapOfFiles(files)

        then: 'expect to see nested map, files have common parent node "grails-app" and two have common parent "tagLib"'

            // root
            NodeFile root = new NodeFile('name': 'root', 'parent': null, 'children': [])

            // level 1
            NodeFile grailsapp = new NodeFile('name': 'grails-app', 'parent': root, 'children': [])

            // level 2
            NodeFile taglib = new NodeFile('name': 'taglib', 'parent': grailsapp, 'children': [])

            // level 3
            NodeFile revizor = new NodeFile('name': 'revizor', 'parent': taglib, 'children': [])

            // file
            NodeFile file = new NodeFile('name': 'DiffTagLib.groovy', 'isLeaf': true, 'parent': revizor,
                    'status': Constants.ACTION_MODIFIED, 'fullPath': 'grails-app/taglib/revizor/DiffTagLib.groovy')
            NodeFile file2 = new NodeFile('name': 'Comment.groovy', 'isLeaf': true, 'parent': revizor,
                    'status': Constants.ACTION_MODIFIED, 'fullPath': 'grails-app/taglib/revizor/Comment.groovy')

            revizor.children.add(file)
            revizor.children.add(file2)
            taglib.children.add(revizor)
            grailsapp.children.add(taglib)
            root.children.add(grailsapp)

        and:
            root.equals(result)
    }
}
