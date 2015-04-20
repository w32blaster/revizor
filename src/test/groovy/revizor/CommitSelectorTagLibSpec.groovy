package revizor

import com.revizor.Review
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * Created on 17/03/15.
 *
 * @author w32blaster
 */
@TestFor(CommitSelectorTagLib)
class CommitSelectorTagLibSpec extends Specification {

    def "method creates map of reviews grouped by used commit revisions" () {
        given:
            def reviews = [
                new Review(title: "Review1", commits: ['SHA-1']),
                new Review(title: "Review2", commits: ['SHA-1', 'SHA-2']),
                new Review(title: "Review3", commits: ['SHA-1', 'SHA-3']),
            ]

        when:
            def map = tagLib._getMapOfUsedReviews(reviews)

        then:
        println(map)
            map.size() == 3

        and:
            map.get('SHA-1').size() == 3

        and:
            map.get('SHA-2').size() == 1

        and:
            map.get('SHA-3').size() == 1

    }

    def "method creates map of reviews grouped by used commit revisions unique commits" () {
        given:
            def reviews = [
                    new Review(title: "Review1", commits: []),
                    new Review(title: "Review2", commits: ['SHA-1', 'SHA-2', 'SHA-3']),
                    new Review(title: "Review3", commits: ['SHA-4', 'SHA-5']),
            ]

        when:
            def map = tagLib._getMapOfUsedReviews(reviews)

        then:
            map.size() == 5

        and:
            map.get('SHA-1').size() == 1
            map.get('SHA-2').size() == 1
            map.get('SHA-3').size() == 1
            map.get('SHA-4').size() == 1
            map.get('SHA-5').size() == 1
    }

    def "method creates map of reviews grouped by used commit revisions one commit" () {
        given:
            def reviews = [
                    new Review(title: "Review1", commits: ['SHA-1']),
                    new Review(title: "Review2", commits: ['SHA-1']),
                    new Review(title: "Review3", commits: ['SHA-1']),
            ]

        when:
            def map = tagLib._getMapOfUsedReviews(reviews)

        then:
            map.size() == 1

        and:
            map.get('SHA-1').size() == 3
    }
}
