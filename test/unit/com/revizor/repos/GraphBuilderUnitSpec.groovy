package com.revizor.repos

import com.revizor.utils.Constants
import com.revizor.utils.Utils
import spock.lang.Ignore
import spock.lang.Specification

/**
 * test-app -echoOut unit: com.revizor.repos.GraphBuilderUnitSpec
 */
class GraphBuilderUnitSpec extends Specification {

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
						   new Commit(id: 'B', parents: ['A'] )].reverse()
            def lstMaster = ['A', 'B', 'C']
		when:
			def result = new GraphBuilder().prepareHistoryGraph(commits, lstMaster, [])
			//_print(result)

		then: 'root does not have any curves because it does not have any parents'
			result[1].curves.size == 1
            result[1].curves[0] == Constants.CURVE_ROOT

		and:
			result[0].curves.size == 1
			result[0].curves[0] == Constants.CURVE_VERTICAL_ACT
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
						   new Commit(id: 'C', parents: ['B'] )].reverse()
            def lstMaster = ['A', 'B', 'C']
		when:
			def result = new GraphBuilder().prepareHistoryGraph(commits, lstMaster, [])

		then: 
			result[2].curves.size == 1
            result[2].curves[0] == Constants.CURVE_ROOT

		and:
			result[1].curves.size == 1
			result[1].curves[0] == Constants.CURVE_VERTICAL_ACT

        and:
            result[0].curves.size == 1
			result[0].curves[0] == Constants.CURVE_VERTICAL_ACT
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
    def "new branch is displayed near the master"() {

        given:
        def commits = [new Commit(id: 'A'),
                       new Commit(id: 'B', parents: ['A'] ),
                       new Commit(id: 'C', parents: ['B'] ),
                       new Commit(id: 'D', parents: ['B'] )].reverse()

            def lstMaster = ['A', 'B', 'D']
        when:
            def result = new GraphBuilder().prepareHistoryGraph(commits, lstMaster, [])
            //Utils.printTree(result)

        then: 'root does not have parents'
            result[3].curves.size == 1
            result[3].curves[0] == Constants.CURVE_ROOT

        and: 'B node has two edge to node A'
            result[2].curves.size == 1
            result[2].curves[0] == Constants.CURVE_VERTICAL_ACT
            result[2].currentCurveIdx == 0

        and: 'node C is a tip of new branch on the right of master branch'
            result[1].curves.size == 2
            result[1].curves[0] == Constants.CURVE_VERTICAL
            result[1].curves[1] == Constants.CURVE_SLASH_ACT
            result[1].currentCurveIdx == 1

        and: 'node D is alone'
            result[0].curves.size == 1
            result[0].curves[0] == Constants.CURVE_VERTICAL_ACT
            result[0].currentCurveIdx == 0
    }

    /*
        2.  C
            |
        1.  | B
            |/
        0.  A
    */
    def "new branch runs directly from root A"() {

        given:
            def commits = [new Commit(id: 'A'),
                           new Commit(id: 'B', parents: ['A'] ),
                           new Commit(id: 'C', parents: ['A'] )].reverse()

            def lstMaster = ['A', 'C']
        when:
            def result = new GraphBuilder().prepareHistoryGraph(commits, lstMaster, [])
            //Utils.printTree(result)

        then: 'root does not have parents'
            result[2].curves.size == 1
            result[2].curves[0] == Constants.CURVE_ROOT

        and: 'line 1 has two edges'
            result[1].curves.size == 2
            result[1].curves[0] == Constants.CURVE_VERTICAL
            result[1].curves[1] == Constants.CURVE_SLASH_ACT
            result[1].currentCurveIdx == 1

        and: 'node D is alone'
            result[0].curves.size == 1
            result[0].curves[0] == Constants.CURVE_VERTICAL_ACT
            result[0].currentCurveIdx == 0

    }

    /*
        0.    D
              |
        1.  C |
            |/    
        2.  B
            |
        3.  A
    */
    def "one additional branch from the B commit where B-C is master"() {
        
        given: 
            def commits = [new Commit(id: 'A'),
                           new Commit(id: 'B', parents: ['A'] ),
                           new Commit(id: 'C', parents: ['B'] ),
                           new Commit(id: 'D', parents: ['B'] )].reverse()

            def lstMaster = ['A', 'B', 'C']
            def lstTips = ['C', 'D']
        when:
            def result = new GraphBuilder().prepareHistoryGraph(commits, lstMaster, lstTips)
            //Utils.printTree(result)

        then: 'root does not have parents'
            result[3].curves.size == 1
            result[3].curves[0] == Constants.CURVE_ROOT
            
        and: 'B node has only one edge to node A'
            result[2].curves.size == 1
            result[2].curves[0] == Constants.CURVE_VERTICAL_ACT
            result[2].currentCurveIdx == 0

        and: 'under the node C graph has two edges: C is master and goes up and the branch to D that is a feature branch'
            result[1].curves.size == 2
            result[1].curves[0] == Constants.CURVE_VERTICAL_ACT
            result[1].curves[1] == Constants.CURVE_SLASH
            result[1].currentCurveIdx == 0

        and: 'node D is alone, because tip for C is behind the D'
            result[0].curves.size == 2
            result[0].curves[0] == Constants.CURVE_BLANK
            result[0].curves[1] == Constants.CURVE_VERTICAL_ACT // <-- this curve is rotated, thus not "back slash" but "vertical"
            result[0].currentCurveIdx == 1
    }

    /*
       6.    G
             |
       5.    F
             |
       4.    E
             |
       3.    D
             |
       2.  C |
           |/
       1.  B
           |
       0.  A
   */
    def "non master branch is decorated by moving to the right in case if they are displayed above the master tip"() {

        given:
            def commits = [new Commit(id: 'A'),
                           new Commit(id: 'B', parents: ['A'] ),
                           new Commit(id: 'C', parents: ['B'] ),
                           new Commit(id: 'D', parents: ['B'] ),
                           new Commit(id: 'E', parents: ['D'] ),
                           new Commit(id: 'F', parents: ['E'] ),
                           new Commit(id: 'G', parents: ['F'] )].reverse()

            def lstMaster = ['A', 'B', 'C']
            def lstTips = ['C', 'G']
        when:
            def result = new GraphBuilder().prepareHistoryGraph(commits, lstMaster, lstTips)
            //Utils.printTree(result)

        then: 'root does not have parents'
            result[6].curves.size == 1
            result[6].curves[0] == Constants.CURVE_ROOT

        and: 'B node has only one edge to node A'
            result[5].curves.size == 1
            result[5].curves[0] == Constants.CURVE_VERTICAL_ACT
            result[5].currentCurveIdx == 0

        and: 'under the node C graph has two edges: C is master and goes up and the branch to D that is a feature branch'
            result[4].curves.size == 2
            result[4].curves[0] == Constants.CURVE_VERTICAL_ACT
            result[4].curves[1] == Constants.CURVE_SLASH
            result[4].currentCurveIdx == 0

        and: 'all other nodes are displayed with the indent because they are placed above the master tip'
            // node D
            result[3].curves.size == 2
            result[3].curves[0] == Constants.CURVE_BLANK
            result[3].curves[1] == Constants.CURVE_VERTICAL_ACT
            result[3].currentCurveIdx == 1

            // node E
            result[2].curves.size == 2
            result[2].curves[0] == Constants.CURVE_BLANK
            result[2].curves[1] == Constants.CURVE_VERTICAL_ACT
            result[2].currentCurveIdx == 1

            // node F
            result[1].curves.size == 2
            result[1].curves[0] == Constants.CURVE_BLANK
            result[1].curves[1] == Constants.CURVE_VERTICAL_ACT
            result[1].currentCurveIdx == 1

            // node G
            result[0].curves.size == 2
            result[0].curves[0] == Constants.CURVE_BLANK
            result[0].curves[1] == Constants.CURVE_VERTICAL_ACT
            result[0].currentCurveIdx == 1
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
    def "one additional branch from the B commit where B-D is master"() {
		
		given: 
			def commits = [new Commit(id: 'A'),
						   new Commit(id: 'B', parents: ['A'] ),
						   new Commit(id: 'C', parents: ['B'] ),
						   new Commit(id: 'D', parents: ['B'] )].reverse()

			def lstMaster = ['A', 'B', 'D']
		when:
			def result = new GraphBuilder().prepareHistoryGraph(commits, lstMaster, [])
            //Utils.printTree(result)

		then: 'root does not have parents'
			result[3].curves.size == 1
            result[3].curves[0] == Constants.CURVE_ROOT
			
		and: 'B node has only one edge to node A'
			result[2].curves.size == 1
            result[2].curves[0] == Constants.CURVE_VERTICAL_ACT
            result[2].currentCurveIdx == 0

        and: 'under the node C graph has two edges: vertical that goes past the current node towards D and curve edge to C'
			result[1].curves.size == 2
            result[1].curves[0] == Constants.CURVE_VERTICAL
            result[1].curves[1] == Constants.CURVE_SLASH_ACT
        and: 'active node C is places on the second row in graph (index is 0-based)'
            result[1].currentCurveIdx == 1

        and: 'node D is alone'
			result[0].curves.size == 1
			result[0].curves[0] == Constants.CURVE_VERTICAL_ACT
            result[0].currentCurveIdx == 0
    }

    /*
        5.  F
            |
        4.  | E
            |  \
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
                           new Commit(id: 'F', parents: ['C'] )].reverse()

            def lstMaster = ['A', 'B', 'C', 'F']
            def lstTips = ['D', 'E', 'F']
        when:
            def result = new GraphBuilder().prepareHistoryGraph(commits, lstMaster, lstTips)
            //Utils.printTree(result)

        then: 'A root does not have parents'
            result[5].curves.size == 1
            result[5].curves[0] == Constants.CURVE_ROOT
            
        and: 'B node has only one edge to node A'
            result[4].curves.size == 1
            result[4].curves[0] == Constants.CURVE_VERTICAL_ACT
            result[4].currentCurveIdx == 0

        and: 'line 2 has two curves - node C and left branch'
            result[3].curves.size == 2
            result[3].curves[0] == Constants.CURVE_VERTICAL_ACT
            result[3].curves[1] == Constants.CURVE_SLASH

        and: 'line 3 has three curves - master, node D and the right branch (last two edges are slashes)'
            result[2].curves.size == 3
            result[2].curves[0] == Constants.CURVE_VERTICAL
            result[2].curves[1] == Constants.CURVE_SLASH_ACT
            result[2].curves[2] == Constants.CURVE_SLASH

        and: 'line 4 has three vertical curves - master, node D and the right branch'
            result[1].curves.size == 2
            result[1].curves[0] == Constants.CURVE_VERTICAL
            result[1].curves[1] == Constants.CURVE_BACK_SLASH_ACT

        and: 'line 5 has three curves - node F, and two empty spaces'
            result[0].curves.size == 1
            result[0].curves[0] == Constants.CURVE_VERTICAL_ACT
    }

    /*
        5.  F
            |
        4.  | E
            |  \
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
                           new Commit(id: 'F', parents: ['C'] )].reverse()

            def lstMaster = ['A', 'B', 'C', 'F']
            def lstTips = ['D', 'E', 'F']
        when:
            def result = new GraphBuilder().prepareHistoryGraph(commits, lstMaster, lstTips)
            //Utils.printTree(result)

        then: 'A root does not have parents'
            result[5].curves.size == 1
            result[5].curves[0] == Constants.CURVE_ROOT

        and: 'B node has only one edge to node A'
            result[4].curves.size == 1
            result[4].curves[0] == Constants.CURVE_VERTICAL_ACT

        and: 'line 2 has two curves - node C and left branch'
            result[3].curves.size == 2
            result[3].curves[0] == Constants.CURVE_VERTICAL_ACT
            result[3].curves[1] == Constants.CURVE_SLASH

        and: 'line 3 has three curves - master, node D and the right branch (last two edges are slashes)'
            result[2].curves.size == 3
            result[2].curves[0] == Constants.CURVE_VERTICAL
            result[2].curves[1] == Constants.CURVE_SLASH_ACT
            result[2].curves[2] == Constants.CURVE_SLASH

        and: 'line 4 has two curves - master and the right branch, node D is a tip and it does not have any outgoing edges'
            result[1].curves.size == 2
            result[1].curves[0] == Constants.CURVE_VERTICAL
            result[1].curves[1] == Constants.CURVE_BACK_SLASH_ACT

        and: 'line 5 has only one curve - node F. Other are tips so they do not have any edges'
            result[0].curves.size == 1
            result[0].curves[0] == Constants.CURVE_VERTICAL_ACT
    }

   /*
        5.  F
            |
        4.  | E
            |  \
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
                           new Commit(id: 'F', parents: ['C'] )].reverse()

            def lstMaster = ['A', 'B', 'C', 'F']
            def lstTips = ['D', 'F']
        when:
            def result = new GraphBuilder().prepareHistoryGraph(commits, lstMaster, lstTips)
            //Utils.printTree(result)

        then: 'A root does not have parents'
            result[5].curves.size == 1
            result[5].curves[0] == Constants.CURVE_ROOT

        and: 'B node has only one edge to node A'
            result[4].curves.size == 1
            result[4].curves[0] == Constants.CURVE_VERTICAL_ACT

        and: 'line 2 has two curves - node C and left branch'
            result[3].curves.size == 2
            result[3].curves[0] == Constants.CURVE_VERTICAL_ACT
            result[3].curves[1] == Constants.CURVE_SLASH

        and: 'line 3 has three curves - master, node D and the right branch (last two edges are slashes)'
            result[2].curves.size == 3
            result[2].curves[0] == Constants.CURVE_VERTICAL
            result[2].curves[1] == Constants.CURVE_SLASH_ACT
            result[2].curves[2] == Constants.CURVE_SLASH

        and: 'line 4 has two curves - master and the right branch, node D is a tip and it does not have any outgoing edges'
            result[1].curves.size == 2
            result[1].curves[0] == Constants.CURVE_VERTICAL
            result[1].curves[1] == Constants.CURVE_BACK_SLASH_ACT

        and: 'line 5 has only one curve - node F. Other are tips so they do not have any edges'
            result[0].curves.size == 1
            result[0].curves[0] == Constants.CURVE_VERTICAL_ACT
    }


   /*
        5.  F
            |
        4.  | E
            |  \
        3.  | D |
            | |/
        2.  C |
            |/
        1.  B
            |
        0.  A
    */
    def "the graph displays one tip without outgoing edges the most right branch"() {

        given:
            def commits = [new Commit(id: 'A'),
                           new Commit(id: 'B', parents: ['A'] ),
                           new Commit(id: 'C', parents: ['B'] ),
                           new Commit(id: 'D', parents: ['B'] ),
                           new Commit(id: 'E', parents: ['C'] ),
                           new Commit(id: 'F', parents: ['C'] )].reverse()

            def lstMaster = ['A', 'B', 'C', 'F']
            def lstTips = ['D', 'F']
        when:
            def result = new GraphBuilder().prepareHistoryGraph(commits, lstMaster, lstTips)
            //Utils.printTree(result)

        then: 'A root does not have parents'
            result[5].curves.size == 1
            result[5].curves[0] == Constants.CURVE_ROOT

        and: 'B node has only one edge to node A'
            result[4].curves.size == 1
            result[4].curves[0] == Constants.CURVE_VERTICAL_ACT

        and: 'line 2 has two curves - node C and left slash branch'
            result[3].curves.size == 2
            result[3].curves[0] == Constants.CURVE_VERTICAL_ACT
            result[3].curves[1] == Constants.CURVE_SLASH

        and: 'line 3 has three curves - master, node D and two vertical lines'
            result[2].curves.size == 3
            result[2].curves[0] == Constants.CURVE_VERTICAL
            result[2].curves[1] == Constants.CURVE_VERTICAL_ACT
            result[2].curves[2] == Constants.CURVE_SLASH

        and: 'line 4 has two curves - master and the right branch, node D is a tip and it does not have any outgoing edges'
            result[1].curves.size == 2
            result[1].curves[0] == Constants.CURVE_VERTICAL
            result[1].curves[1] == Constants.CURVE_BACK_SLASH_ACT

        and: 'line 5 has only one curve - node F. Other are tips so they do not have any edges'
            result[0].curves.size == 1
            result[0].curves[0] == Constants.CURVE_VERTICAL_ACT
    }
    

    /*
        5.  F
            |
        4.  | E
            | |  
        3.  D |
            |/ 
        2.  C 
            |
        1.  | B
            |/
        0.  A
    */
    def "the graph displays two branches from the master"() {

        given:
            def commits = [new Commit(id: 'A'),
                           new Commit(id: 'B', parents: ['A'] ),
                           new Commit(id: 'C', parents: ['A'] ),
                           new Commit(id: 'D', parents: ['C'] ),
                           new Commit(id: 'E', parents: ['C'] ),
                           new Commit(id: 'F', parents: ['D'] )].reverse()

            def lstMaster = ['A', 'C', 'D', 'F']
            def lstTips = ['B', 'E', 'F']
        when:
            def result = new GraphBuilder().prepareHistoryGraph(commits, lstMaster, lstTips)
            //Utils.printTree(result)

        then: 'A root does not have parents'
            result[5].curves.size == 1
            result[5].curves[0] == Constants.CURVE_ROOT

        and: 'B should be a tip of a branch'
            result[4].curves.size == 2
            result[4].curves[0] == Constants.CURVE_VERTICAL
            result[4].curves[1] == Constants.CURVE_SLASH_ACT

        and: 'node C is alone on the line 2'
            result[3].curves.size == 1
            result[3].curves[0] == Constants.CURVE_VERTICAL_ACT

        and: 'line 3 contains node D and a new branch going up to node E'
            result[2].curves.size == 2
            result[2].curves[0] == Constants.CURVE_VERTICAL_ACT
            result[2].curves[1] == Constants.CURVE_SLASH

        and: 'only vertical curve and node E'
            result[1].curves.size == 2
            result[1].curves[0] == Constants.CURVE_VERTICAL
            result[1].curves[1] == Constants.CURVE_VERTICAL_ACT

        and: 'the last node F is alone'
            result[0].curves.size == 1
            result[0].curves[0] == Constants.CURVE_VERTICAL_ACT
    }



  /*
        5.    F
               \
        4.    E |
               \ \
        3.    D | |
             / / /
        2.  C | |
            |/ /
        1.  B |
            |/
        0.  A
    */
    def "Each branch repeats the shape of their neighbours"() {

        given:
            def commits = [new Commit(id: 'A'),
                           new Commit(id: 'B', parents: ['A'] ),
                           new Commit(id: 'C', parents: ['B'] ),
                           new Commit(id: 'D', parents: ['C'] ),
                           new Commit(id: 'E', parents: ['B'] ),
                           new Commit(id: 'F', parents: ['A'] )].reverse()

            def lstMaster = ['A', 'B', 'C']
            def lstTips = ['C', 'D', 'E', 'F']
        when:
            def result = new GraphBuilder().prepareHistoryGraph(commits, lstMaster, lstTips)
            //Utils.printTree(result)

        then: 'A root does not have parents'
            result[5].curves.size == 1
            result[5].curves[0] == Constants.CURVE_ROOT

        and: 'node B and slash curve'
            result[4].curves.size == 2
            result[4].curves[0] == Constants.CURVE_VERTICAL_ACT
            result[4].curves[1] == Constants.CURVE_SLASH
            result[4].currentCurveIdx == 0

        and: 'node C is alone on the line 2'
            result[3].curves.size == 3
            result[3].curves[0] == Constants.CURVE_VERTICAL_ACT
            result[3].curves[1] == Constants.CURVE_SLASH
            result[3].curves[2] == Constants.CURVE_SLASH
            result[3].currentCurveIdx == 0

        and: 'line 3 is shifted to the right'
            result[2].curves.size == 4
            result[2].curves[0] == Constants.CURVE_BLANK
            result[2].curves[1] == Constants.CURVE_SLASH_ACT
            result[2].curves[2] == Constants.CURVE_SLASH
            result[2].curves[3] == Constants.CURVE_SLASH
            result[2].currentCurveIdx == 1

        and: 'only vertical curve and node E'
            result[1].curves.size == 3
            result[1].curves[0] == Constants.CURVE_BLANK
            result[1].curves[1] == Constants.CURVE_BACK_SLASH_ACT
            result[1].curves[2] == Constants.CURVE_BACK_SLASH
            result[1].currentCurveIdx == 1

        and: 'the last node F is alone'
            result[0].curves.size == 2
            result[0].curves[0] == Constants.CURVE_BLANK
            result[0].curves[1] == Constants.CURVE_BACK_SLASH_ACT
            result[0].currentCurveIdx == 1
    }



  /*
        5.  F
            |
        4.  | E
            |  \
        3.  | D |
            |  \ \
        2.  | C | |   
            |/ / /   <-- Four branches are going from the same node B
        1.  B
            |
        0.  A
    */
    def "Few branches are started from the same node B"() {

        given:
            def commits = [new Commit(id: 'A'),
                           new Commit(id: 'B', parents: ['A'] ),
                           new Commit(id: 'C', parents: ['B'] ),
                           new Commit(id: 'D', parents: ['B'] ),
                           new Commit(id: 'E', parents: ['B'] ),
                           new Commit(id: 'F', parents: ['B'] )].reverse()

            def lstMaster = ['A', 'B', 'F']
            def lstTips = ['C', 'D', 'E', 'F']
        when:
            def result = new GraphBuilder().prepareHistoryGraph(commits, lstMaster, lstTips)
            //Utils.printTree(result)

        then: 'A root does not have parents'
            result[5].curves.size == 1
            result[5].curves[0] == Constants.CURVE_ROOT

        and: 'only node B'
            result[4].curves.size == 1
            result[4].curves[0] == Constants.CURVE_VERTICAL_ACT
            result[4].currentCurveIdx == 0

        and: 'from this point we expect to have four starting branches'
            result[3].curves.size == 4
            result[3].curves[0] == Constants.CURVE_VERTICAL
            result[3].curves[1] == Constants.CURVE_SLASH_ACT
            result[3].curves[2] == Constants.CURVE_SLASH
            result[3].curves[3] == Constants.CURVE_SLASH
            result[3].currentCurveIdx == 1

        and: 'line 3'
            result[2].curves.size == 3
            result[2].curves[0] == Constants.CURVE_VERTICAL
            result[2].curves[1] == Constants.CURVE_BACK_SLASH_ACT
            result[2].curves[2] == Constants.CURVE_BACK_SLASH
            result[2].currentCurveIdx == 1

        and: 'line 4'
            result[1].curves.size == 2
            result[1].curves[0] == Constants.CURVE_VERTICAL
            result[1].curves[1] == Constants.CURVE_BACK_SLASH_ACT
            result[1].currentCurveIdx == 1

        and: 'the last node F is alone'
            result[0].curves.size == 1
            result[0].curves[0] == Constants.CURVE_VERTICAL_ACT
            result[0].currentCurveIdx == 0
    }

    /*
         5.  F
             |
         4.  E
             |\
         3.  | D
             | |
         2.  C |
             |/
         1.  B
             |
         0.  A
     */
    def "merge, where node E has two parents C and D"() {

        given:
            def commits = [new Commit(id: 'A'),
                           new Commit(id: 'B', parents: ['A'] ),
                           new Commit(id: 'C', parents: ['B'] ),
                           new Commit(id: 'D', parents: ['B'] ),
                           new Commit(id: 'E', parents: ['C', 'D'] ),
                           new Commit(id: 'F', parents: ['E'] )].reverse()

            def lstMaster = ['A', 'B', 'C', 'E', 'F']
            def lstTips = ['F']
        when:
            def result = new GraphBuilder().prepareHistoryGraph(commits, lstMaster, lstTips)
            //Utils.printTree(result)

        then: 'A root does not have parents'
            result[5].curves.size == 1
            result[5].curves[0] == Constants.CURVE_ROOT

        and: 'B node has only one edge to node A'
            result[4].curves.size == 1
            result[4].curves[0] == Constants.CURVE_VERTICAL_ACT
            result[4].currentCurveIdx == 0

        and: 'line 2 has two curves - node C and left branch'
            result[3].curves.size == 2
            result[3].curves[0] == Constants.CURVE_VERTICAL_ACT
            result[3].curves[1] == Constants.CURVE_SLASH
            result[3].currentCurveIdx == 0

        and: 'line 3 is usual, only D node on the second branch'
            result[2].curves.size == 2
            result[2].curves[0] == Constants.CURVE_VERTICAL
            result[2].curves[1] == Constants.CURVE_VERTICAL_ACT
            result[2].currentCurveIdx == 1

        and: 'here is the merging! The feature branch ends here, thus there is only one curve'
            result[1].curves.size == 1
            result[1].curves[0] == Constants.CURVE_MERGE
            result[1].currentCurveIdx == 0

        and: 'line 5 has three curves - node F, and two empty spaces'
            result[0].curves.size == 1
            result[0].curves[0] == Constants.CURVE_VERTICAL_ACT
            result[0].currentCurveIdx == 0
    }


    /*

    The third branch repeats shape correctly in case of merging

     6.  G
         |\
     5.  | F
         | |
     4.  E |  <-- correctly recognizes merging point and another branch passes around it
         |\ \
     3.  | D |
         | | |
     2.  C | |
         |/ /  <-- two branches from one node
     1.  B
         |
     0.  A

    */
    def "test two merges that starts from the same branch"() {

        given:
            def commits = [new Commit(id: 'A'),
                           new Commit(id: 'B', parents: ['A'] ),
                           new Commit(id: 'C', parents: ['B'] ),
                           new Commit(id: 'D', parents: ['B'] ),
                           new Commit(id: 'E', parents: ['C', 'D'] ),
                           new Commit(id: 'F', parents: ['B'] ),
                           new Commit(id: 'G', parents: ['E', 'F'] )].reverse()

            def lstMaster = ['A', 'B', 'C', 'E', 'G']
            def lstTips = ['G']
        when:
            def result = new GraphBuilder().prepareHistoryGraph(commits, lstMaster, lstTips)
            //Utils.printTree(result)

        then: 'A root does not have parents'
            result[6].curves.size == 1
            result[6].curves[0] == Constants.CURVE_ROOT

        and: 'B node has only one edge to node A'
            result[5].curves.size == 1
            result[5].curves[0] == Constants.CURVE_VERTICAL_ACT
            result[5].currentCurveIdx == 0

        and: 'line 2 is full of curves, because two new branches are started here'
            result[4].curves.size == 3
            result[4].curves[0] == Constants.CURVE_VERTICAL_ACT
            result[4].curves[1] == Constants.CURVE_SLASH
            result[4].curves[2] == Constants.CURVE_SLASH
            result[4].currentCurveIdx == 0

        and: 'line 3, D in the middle'
            result[3].curves.size == 3
            result[3].curves[0] == Constants.CURVE_VERTICAL
            result[3].curves[1] == Constants.CURVE_VERTICAL_ACT
            result[3].curves[2] == Constants.CURVE_VERTICAL
            result[3].currentCurveIdx == 1

        and: 'here is the merging! The last branch moves the the left'
            result[2].curves.size == 2
            result[2].curves[0] == Constants.CURVE_MERGE
            result[2].curves[1] == Constants.CURVE_BACK_SLASH
            result[2].currentCurveIdx == 0

        and: 'line 5 has two curves'
            result[1].curves.size == 2
            result[1].curves[0] == Constants.CURVE_VERTICAL
            result[1].curves[1] == Constants.CURVE_VERTICAL_ACT
            result[1].currentCurveIdx == 1

        and: 'line 6 is merge'
            result[0].curves.size == 1
            result[0].curves[0] == Constants.CURVE_MERGE
            result[0].currentCurveIdx == 0
    }

    /*

     TODO: re-think that case. Merging between is displayed correctly

     6.  G
         |\
     5.  | F  <-- Merge with non-master branch
         | |\
     4.  | | E
         | | |
     3.  | D |
         |/ /
     2.  C |
         | |
     1.  B |
         |/
     0.  A

    */
    @Ignore
    def "test merges in non master branch"() {

        given:
        def commits = [new Commit(id: 'A'),
                       new Commit(id: 'B', parents: ['A'] ),
                       new Commit(id: 'C', parents: ['B'] ),
                       new Commit(id: 'D', parents: ['C'] ),
                       new Commit(id: 'E', parents: ['A'] ),
                       new Commit(id: 'F', parents: ['D', 'E'] ),
                       new Commit(id: 'G', parents: ['C', 'F'] )]

            def lstMaster = ['A', 'B', 'C', 'G']
            def lstTips = ['G']
        when:
            def result = new GraphBuilder().prepareHistoryGraph(commits, lstMaster, lstTips)
            Utils.printTree(result)

        then: 'A root does not have parents'
            result[0].curves.size == 1
            result[0].curves[0] == Constants.CURVE_ROOT

        and: 'B node with slash'
            result[1].curves.size == 2
            result[1].curves[0] == Constants.CURVE_VERTICAL_ACT
            result[1].curves[1] == Constants.CURVE_SLASH
            result[1].currentCurveIdx == 0

        and: 'line 2 is full of curves, because two new branches are started here'
            result[2].curves.size == 2
            result[2].curves[0] == Constants.CURVE_VERTICAL_ACT
            result[2].curves[1] == Constants.CURVE_VERTICAL
            result[2].currentCurveIdx == 0

        and: 'line 3, D in the middle'
            result[3].curves.size == 3
            result[3].curves[0] == Constants.CURVE_VERTICAL
            result[3].curves[1] == Constants.CURVE_SLASH_ACT
            result[3].curves[2] == Constants.CURVE_SLASH
            result[3].currentCurveIdx == 1

        and: 'E is the most right node'
            result[4].curves.size == 3
            result[4].curves[0] == Constants.CURVE_VERTICAL
            result[4].curves[1] == Constants.CURVE_VERTICAL
            result[4].curves[2] == Constants.CURVE_VERTICAL_ACT
            result[4].currentCurveIdx == 2

        and: 'line 5 has one vertical and one merge. The current curve index is 1'
            result[5].curves.size == 2
            result[5].curves[0] == Constants.CURVE_VERTICAL
            result[5].curves[1] == Constants.CURVE_MERGE
            result[5].currentCurveIdx == 1

        and: 'line 6 is merge'
            result[6].curves.size == 1
            result[6].curves[0] == Constants.CURVE_MERGE
            result[6].currentCurveIdx == 0
    }
}