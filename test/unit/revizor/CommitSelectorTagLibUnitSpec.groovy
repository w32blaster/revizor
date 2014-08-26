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
			result[1].curves[0] == Constants.CURVE_VERTICAL_ACT
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
			result[1].curves[0] == Constants.CURVE_VERTICAL_ACT
			result[2].curves[0] == Constants.CURVE_VERTICAL_ACT
    }

    /*
    	3.  D
    	    |
		2.  | C
		    |/    
		1.  B
		    |
		0.  A
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
            _print(result)
			
		then: 'root does not have parents'
			result[0].curves.size == 0
			
		and: 'B node has only one edge to node A'
			result[1].curves.size == 1
            result[1].curves[0] == Constants.CURVE_VERTICAL_ACT

        and: 'under the node C graph has two edges: vertical that goes past the current node towords D and curlve edge to C'
			result[2].curves.size == 2
            result[2].curves[0] == Constants.CURVE_VERTICAL
            result[2].curves[1] == Constants.CURVE_SLASH_ACT

        and: 'node D is alone, because tip for C is behind the D'
			result[3].curves.size == 1
			result[3].curves[0] == Constants.CURVE_VERTICAL_ACT
    }

    // function for debuggin purposes: prints graph to the console
    def _print(commits) {
        commits.reverseEach { commit ->
            
            def line1 = ""
            def line2 = ""
            //println "commit ${commit.id} has curves ${commit.curves.size()}: ${commit.curves}"
            if (commit.curves.size() == 0) {
                // this is root
                line1 += " " + commit.id
            }
            else {
                commit.curves.each { curve ->
                    switch (curve) {
                        case Constants.CURVE_VERTICAL:
                            line1 += " |"
                            line2 += " |"
                            break;

                        case Constants.CURVE_SLASH:
                            line1 += " |"
                            line2 += "/ "
                            break;

                        case Constants.CURVE_BACK_SLASH:
                            line1 += "\\ "
                            line2 += " |"
                            break;

                        case Constants.CURVE_VERTICAL_ACT:
                            line1 += " " + commit.id
                            line2 += " |"
                            break;

                        case Constants.CURVE_SLASH_ACT:
                            line1 += " " + commit.id
                            line2 += "/ "
                            break;

                        case Constants.CURVE_BACK_SLASH_ACT:
                            line1 += " " + commit.id
                            line2 += "\\ "
                            break;

                        default:
                            line1 += " *"
                            line2 += " *"
                            break;                        
                    }
                }
            }
            
            println line1
            println line2
        }
    }
}