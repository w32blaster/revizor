package revizor

import grails.test.mixin.*
import spock.lang.IgnoreRest
import spock.lang.Specification

import com.revizor.utils.Constants
import com.revizor.repos.Commit

/**
 * 
 */
@TestFor(CommitSelectorTagLib)
class CommitSelectorTagLibUnitSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    /*
		B
		|
		A
    */
    def "two commits are connected with single line"() {
		
		given: 
			def commits = [new Commit(id: 'A'),
						   new Commit(id: 'B', parents: ['A'] )]	
		when:
			def result = tagLib.prepareHistoryGraph(commits, [])
			
		then: 'root does not have any curves because it does not have any parents'
			result[0].curves.size == 0

		and:
			result[1].curves.size == 1
			result[1].curves[0] == Constants.CURVE_VERTICAL
    }

    /*
		C
		|    
		B
		|
		A
    */
    def "three commits connected with single line"() {
		
		given: 
			def commits = [new Commit(id: 'A'),
						   new Commit(id: 'B', parents: ['A'] ),
						   new Commit(id: 'C', parents: ['B'] )]	
		when:
			def result = tagLib.prepareHistoryGraph(commits, [])
			
		then: 
			result[0].curves.size == 0

		and:
			result[1].curves.size == 1
			result[2].curves.size == 1
			result[1].curves[0] == Constants.CURVE_VERTICAL
			result[2].curves[0] == Constants.CURVE_VERTICAL
    }

    /*
    	D
    	|
		| C
		| |   
		B/
		|
		A
    */
    def "one additional branch from the B commit"() {
		
		given: 
			def commits = [new Commit(id: 'A'),
						   new Commit(id: 'B', parents: ['A'] ),
						   new Commit(id: 'C', parents: ['B'] ),
						   new Commit(id: 'D', parents: ['B'] )]

			def lstMaster = ['A', 'B', 'D']
		when:
			def result = tagLib.prepareHistoryGraph(commits, lstMaster)
			
		then: 'root does not have parents'
         
			result[0].curves.size == 0
			
		and:
			result[1].curves.size == 1
			result[2].curves.size == 1
			result[3].curves.size == 1
			result[1].curves[0] == Constants.CURVE_VERTICAL
			result[2].curves[0] == Constants.CURVE_VERTICAL
    }
}