package revizor

import grails.test.mixin.*
import spock.lang.IgnoreRest
import spock.lang.Specification

import com.revizor.utils.Constants
import com.revizor.utils.Utils
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
			def result = tagLib.prepareHistoryGraph(commits, [], [])
			//_print(result)

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
			def result = tagLib.prepareHistoryGraph(commits, [], [])
			//_print(result)

		then: 
			result[0].curves.size == 0

		and:
			result[1].curves.size == 1
			result[2].curves.size == 1
			result[1].curves[0] == Constants.CURVE_VERTICAL_ACT
			result[2].curves[0] == Constants.CURVE_VERTICAL_ACT
    }

    /*
        3.  | D
            | |
        2.  C | 
            |/    
        1.  B
            |
        0.  A
    */
    def "one additional branch from the B commit where B-C is master"() {
        
        given: 
            def commits = [new Commit(id: 'A'),
                           new Commit(id: 'B', parents: ['A'] ),
                           new Commit(id: 'C', parents: ['B'] ),
                           new Commit(id: 'D', parents: ['B'] )]

            def lstMaster = ['A', 'B', 'C']
        when:
            def result = tagLib.prepareHistoryGraph(commits, lstMaster, [])
            //_print(result)

        then: 'root does not have parents'
            result[0].curves.size == 0
            
        and: 'B node has only one edge to node A'
            result[1].curves.size == 1
            result[1].curves[0] == Constants.CURVE_VERTICAL_ACT

        and: 'under the node C graph has two edges: C is master and goes up and the branch to D that is a feature branch'
            result[2].curves.size == 2
            result[2].curves[0] == Constants.CURVE_VERTICAL_ACT
            result[2].curves[1] == Constants.CURVE_SLASH

        and: 'node D is alone, because tip for C is behind the D'
            result[3].curves.size == 2
            result[3].curves[0] == Constants.CURVE_VERTICAL
            result[3].curves[1] == Constants.CURVE_VERTICAL_ACT
    }

    /*
    	3.  D |
    	    | |
		2.  | C
		    |/    
		1.  B
		    |
		0.  A
    */
    def "one additional branch from the B commit where B-D is master"() {
		
		given: 
			def commits = [new Commit(id: 'A'),
						   new Commit(id: 'B', parents: ['A'] ),
						   new Commit(id: 'C', parents: ['B'] ),
						   new Commit(id: 'D', parents: ['B'] )]

			def lstMaster = ['A', 'B', 'D']
		when:
			def result = tagLib.prepareHistoryGraph(commits, lstMaster, [])
            //Utils.printTree(result)

		then: 'root does not have parents'
			result[0].curves.size == 0
			
		and: 'B node has only one edge to node A'
			result[1].curves.size == 1
            result[1].curves[0] == Constants.CURVE_VERTICAL_ACT

        and: 'under the node C graph has two edges: vertical that goes past the current node towords D and curlve edge to C'
			result[2].curves.size == 2
            result[2].curves[0] == Constants.CURVE_VERTICAL
            result[2].curves[1] == Constants.CURVE_SLASH_ACT

        and: 'node D is alone, but the edge still goes from C node'
			result[3].curves.size == 2
			result[3].curves[0] == Constants.CURVE_VERTICAL_ACT
            result[3].curves[1] == Constants.CURVE_VERTICAL
    }

    /*
        5.  F | |
            | | |
        4.  | | E
            | | |
        3.  | D |
            |/ /
        2.  C | 
            |/    
        1.  B
            |
        0.  A
    */
    def "the branch B-E should bend out two nearest branches repeating the same shape"() {
        
        given: 
            def commits = [new Commit(id: 'A'),
                           new Commit(id: 'B', parents: ['A'] ),
                           new Commit(id: 'C', parents: ['B'] ),
                           new Commit(id: 'D', parents: ['C'] ),
                           new Commit(id: 'E', parents: ['B'] ),
                           new Commit(id: 'F', parents: ['C'] )]

            def lstMaster = ['A', 'B', 'C', 'F']
        when:
            def result = tagLib.prepareHistoryGraph(commits, lstMaster, [])
            //_print(result)

        then: 'A root does not have parents'
            result[0].curves.size == 0
            
        and: 'B node has only one edge to node A'
            result[1].curves.size == 1
            result[1].curves[0] == Constants.CURVE_VERTICAL_ACT

        and: 'line 2 has two curves - node C and left branch'
            result[2].curves.size == 2
            result[2].curves[0] == Constants.CURVE_VERTICAL_ACT
            result[2].curves[1] == Constants.CURVE_SLASH

        and: 'line 3 has three curves - master, node D and the right branch (last two edges are slashes)'
            result[3].curves.size == 3
            result[3].curves[0] == Constants.CURVE_VERTICAL
            result[3].curves[1] == Constants.CURVE_SLASH_ACT
            result[3].curves[2] == Constants.CURVE_SLASH

        and: 'line 4 has three vertical curves - master, node D and the right branch'
            result[4].curves.size == 3
            result[4].curves[0] == Constants.CURVE_VERTICAL
            result[4].curves[1] == Constants.CURVE_VERTICAL
            result[4].curves[2] == Constants.CURVE_VERTICAL_ACT

        and: 'line 5 has three curves - node F, and two vertical branches'
            result[5].curves.size == 3
            result[5].curves[0] == Constants.CURVE_VERTICAL_ACT
            result[5].curves[1] == Constants.CURVE_VERTICAL
            result[5].curves[2] == Constants.CURVE_VERTICAL
    }

    /*
        5.  F
            |
        4.  |   E
            |   |
        3.  | D |
            |/ /
        2.  C |
            |/
        1.  B
            |
        0.  A
    */
    def "the graph displays tips without outgoing edges"() {

        given:
            def commits = [new Commit(id: 'A'),
                           new Commit(id: 'B', parents: ['A'] ),
                           new Commit(id: 'C', parents: ['B'] ),
                           new Commit(id: 'D', parents: ['C'] ),
                           new Commit(id: 'E', parents: ['B'] ),
                           new Commit(id: 'F', parents: ['C'] )]

            def lstMaster = ['A', 'B', 'C', 'F']
            def lstTips = ['D', 'E', 'F']
        when:
            def result = tagLib.prepareHistoryGraph(commits, lstMaster, lstTips)
            //_print(result)

        then: 'A root does not have parents'
            result[0].curves.size == 0

        and: 'B node has only one edge to node A'
            result[1].curves.size == 1
            result[1].curves[0] == Constants.CURVE_VERTICAL_ACT

        and: 'line 2 has two curves - node C and left branch'
            result[2].curves.size == 2
            result[2].curves[0] == Constants.CURVE_VERTICAL_ACT
            result[2].curves[1] == Constants.CURVE_SLASH

        and: 'line 3 has three curves - master, node D and the right branch (last two edges are slashes)'
            result[3].curves.size == 3
            result[3].curves[0] == Constants.CURVE_VERTICAL
            result[3].curves[1] == Constants.CURVE_SLASH_ACT
            result[3].curves[2] == Constants.CURVE_SLASH

        and: 'line 4 has two curves - master and the right branch, node D is a tip and it does not have any outgoing edges'
            result[4].curves.size == 3
            result[4].curves[0] == Constants.CURVE_VERTICAL
            result[4].curves[1] == Constants.CURVE_BLANK
            result[4].curves[2] == Constants.CURVE_VERTICAL_ACT

        and: 'line 5 has only one curve - node F. Other are tips so they do not have any edges'
            result[5].curves.size == 3
            result[5].curves[0] == Constants.CURVE_VERTICAL_ACT
            result[5].curves[1] == Constants.CURVE_BLANK
            result[5].curves[2] == Constants.CURVE_BLANK
    }

   /*
        5.  F   |
            |   |
        4.  |   E
            |   |
        3.  | D |
            |/ /
        2.  C |
            |/
        1.  B
            |
        0.  A
    */
    def "the graph displays one tip without outgoing edges"() {

        given:
            def commits = [new Commit(id: 'A'),
                           new Commit(id: 'B', parents: ['A'] ),
                           new Commit(id: 'C', parents: ['B'] ),
                           new Commit(id: 'D', parents: ['C'] ),
                           new Commit(id: 'E', parents: ['B'] ),
                           new Commit(id: 'F', parents: ['C'] )]

            def lstMaster = ['A', 'B', 'C', 'F']
            def lstTips = ['D', 'F']
        when:
            def result = tagLib.prepareHistoryGraph(commits, lstMaster, lstTips)
            //Utils.printTree(result)

        then: 'A root does not have parents'
            result[0].curves.size == 0

        and: 'B node has only one edge to node A'
            result[1].curves.size == 1
            result[1].curves[0] == Constants.CURVE_VERTICAL_ACT

        and: 'line 2 has two curves - node C and left branch'
            result[2].curves.size == 2
            result[2].curves[0] == Constants.CURVE_VERTICAL_ACT
            result[2].curves[1] == Constants.CURVE_SLASH

        and: 'line 3 has three curves - master, node D and the right branch (last two edges are slashes)'
            result[3].curves.size == 3
            result[3].curves[0] == Constants.CURVE_VERTICAL
            result[3].curves[1] == Constants.CURVE_SLASH_ACT
            result[3].curves[2] == Constants.CURVE_SLASH

        and: 'line 4 has two curves - master and the right branch, node D is a tip and it does not have any outgoing edges'
            result[4].curves.size == 3
            result[4].curves[0] == Constants.CURVE_VERTICAL
            result[4].curves[1] == Constants.CURVE_BLANK
            result[4].curves[2] == Constants.CURVE_VERTICAL_ACT

        and: 'line 5 has only one curve - node F. Other are tips so they do not have any edges'
            result[5].curves.size == 3
            result[5].curves[0] == Constants.CURVE_VERTICAL_ACT
            result[5].curves[1] == Constants.CURVE_BLANK
            result[5].curves[2] == Constants.CURVE_VERTICAL
    }


   /*
        5.  F |  
            | |  
        4.  | E  
            | |  
        3.  | | D
            |/ /
        2.  C |
            |/
        1.  B
            |
        0.  A
    */
    

  /*
        5.  F |  
            | |  
        4.  | E  
            | |  
        3.  | |
            |/ 
        2.  C 
            |
        1.  | B
            |/
        0.  A
    */


  /*
        5.    F 
              |  
        4.    E  
              |  
        3.    |
              | 
        2.  C |
            | |
        1.  | B
            |/
        0.  A
    */

  /*
        5.  |   F
            |   |
        4.  |   | E 
            |   | |  
        3.  | D | |
            |/ / /
        2.  C | |
            |/ /
        1.  B |
            |/
        0.  A
    */

  /*
        5.  F
            |
        4.  |   E
            |   |      
        3.  |   | D  
            |   | |  
        2.  | C | |   
            | | | |  
        1.  B/ / /   
            |
        0.  A
    */

}