package revizor

import com.revizor.repos.Commit
import com.revizor.utils.Constants
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

class CommitSelectorTagLib {

    static namespace = "sc"

    /**
     *  Prints flat list of all the commits with a checkbox. Allows user to select commits for a review
     */
    def selectCommitsForReview = {attrs, body ->

        _printCommits(attrs, { repo, list ->
            
            int count = 0;
            def isChecked = "";
            out << "<ul class='list-group'>"
            for (Commit rev : list) {
                isChecked = (rev.id.equals(attrs.selected)) ? "checked" : ""
                out << "<li class='list-group-item'><input type='checkbox' name='commits' value='${rev.id}' $isChecked />"
                out << "<span class='truncate'> ${rev.id.subSequence(0, 7)} ${rev.message }</span> "
                out << "<span class='label label-default'>${rev.author}</span></li>" /* rev.getId().getName() */
                count++;
                isChecked = ""
            }
            out << "</ul>"
            out << "<tr><td colspan='2'>Had " + count + " commits overall on current branch</td></tr>"

        })
    }

    /**
     * Prints the list of commits with graph. Intended to be displayed on the dashboard.
     *
     */
    def buildFlatListofCommits = { attrs, body ->

        _printCommits(attrs, { repo, list ->

            def listOfMasterIds = repo.getListOfMasterCommits();
            def mapBranches = repo.getMapBranchesReferences();

            prepareHistoryGraph(list, listOfMasterIds, mapBranches)

            out << "<ul class='list-group'>"
            for (Commit rev : list) {
                out << """
                        <li class="list-group-item truncate" title="${rev.id.subSequence(0, 7)}">
                            ${rev.message } 
                            <span class="label label-default">${rev.author}</span>
                            <a href="${createLink(controller: 'review', action: 'create', id: attrs.repo.ident(), params: [selected: rev.id])}" class="btn btn-default btn-xs tree-context-button">
                                <span class="glyphicon glyphicon-plus"></span>
                            </a>
                         </li>
                        """
            }
            out << "</ul>"
        })
    }

    def _printCommits(attrs, closurePrint) {
        if (null == attrs.repo) {
            out << "A repository is not specified"
        }
        else {
            def repo = attrs.repo.initImplementation();
            def commitList = repo.getListOfCommits();

            closurePrint(repo, commitList);
        }
    }

    /**
     * Prepares the list of commits to be displayed as history graph
     *
     * @param list of all the commits. This list will be updated
     * @param list of SHA keys of master/default branch commit
     * @param list of SHA keys of tips (references)
     */
    def prepareHistoryGraph(lstCommits, lstMaster, lstTips) {
          
        // prepare list of commits: build map "SHA key" <=> "index" and fill "children" collection for each commit
        def mapIds = [:]
        lstCommits.eachWithIndex {commit, i -> 
            mapIds.put(commit.id, i);
            if (commit.parents.size() == 1) {
                def parentNodeIndex = mapIds.get(commit.parents[0])
                lstCommits[parentNodeIndex].children.add(commit.id);
            }
        }

        Set<Integer> setAccumulatedIdx = []

        // draw a history graph
        lstCommits.eachWithIndex { commit, i ->
            if (commit.parents.size() == 1) {
                def parentNodeIndex = mapIds.get(commit.parents[0])

                // draw a line (edge) between current node and its parent
                def isBelongingToMaster = lstMaster.contains(commit.id);
                def isTip = lstTips.contains(commit.id);
                _addLinesBetweenTwoNodes(parentNodeIndex, i, lstCommits, isBelongingToMaster, isTip, setAccumulatedIdx);
            }
            else if (commit.parents.size() > 1) {
                // when a node has more than one parents, this is a merging of two branches/revisions

                // TODO: implement this
            }
        }

        return lstCommits;
    }

    /**
     * "Draws" the path (edge) between two nodes in the graph.
     * Function fills the array "curves" with curve types. Later we will display it with SVG.
     * The idea is a path should repeat "shape"
     * of already existing graph.
     *
     * @param from - index of node that we want draw line from (inclusive)
     * @param to - index of node that we want draw line to (exclusive)
     * @param lstCommits - list of all commits to be modified
     * @param isBelongingToMaster - (boolean) whether current commit ("to") belongs to master or not
     * @param isTip - (boolean) whether current commit ("to") is tip (last commit) on current branch or not
     * @param setTipsBranches - set accumulates all the indexes of tips to remember which edges we should keep blank (index of edge, not commit id)
     */
    def _addLinesBetweenTwoNodes(from, to, lstCommits, isBelongingToMaster, isTip, setTipsBranches) {
        for (i in from+1..to) {

            _copyEdgesFromPreviousLine(lstCommits, i, to, setTipsBranches)

            def isNewBranch = (lstCommits[i-1].children.size() > 1)

            if (isNewBranch) {
                // if parent has more than one children, here come(s) new branch(es)
                
                if (lstCommits[i].curves.size() == 0) {
                        // the very first branch should be vertical.
                        lstCommits[i].curves.add( _getVerticalCurve(i == to));
                }
                else {
                    // others branches should be curve, because here new branch "goes to the right" away of basic line
                    if (isBelongingToMaster) {
                        lstCommits[i].curves.add(0, _getVerticalCurve(i == to));
                        lstCommits[i].curves[1] = _rotateRightEdge(lstCommits[i].curves[1])
                    } else {
                        lstCommits[i].curves.add( _getSlashCurve(i == to));
                    }
                    
                }
            }
            else {
                if (isBelongingToMaster) {
                    lstCommits[i].curves.add(0, _getVerticalCurve(i == to) );
                }
                else {
                    lstCommits[i].curves.add( _getVerticalCurve(i == to) );
                }
            }
        }

        if (isTip) {
            setTipsBranches << (isBelongingToMaster ? 0 : lstCommits[to].curves.size() - 1)
        }
    }

    /**
     * Copies previous edges to current line.
     *
     * Let's say, previous line is
     *    previous -> | | |
     * and we need to prolong current branch (edge) which is third with slash bar ("/").
     * If we just added new edge to the next line, then the result will be like that:
     *    current  -> /
     *    previous -> | | |
     * because we forgot to prolong existed branches (the first and the second ones). Expected
     * result should be (where the first two bars are copied and slash is current curve):
     *    current  -> | | /
     *    previous -> | | |
     *     
     * This method copies previous existing edges to new line ("prolongs" existing edges).
     * 
     * While looping we need to 
     */
    def _copyEdgesFromPreviousLine(lstCommits, i, toIndex, setTipsBranches) {

        def prevCurvesCount = lstCommits[i-1].curves.size()
        def currentCurvesCount = lstCommits[i].curves.size() + 1

        if (prevCurvesCount > 1 && prevCurvesCount > currentCurvesCount) {

            def subArray = lstCommits[i-1].curves[0..(prevCurvesCount-2)]

            // but if a neighbour branch has a node, then replace it, when node is on current branch
            if (i == toIndex) {
                subArray = subArray.collect {
                    switch (it) {
                        case Constants.CURVE_VERTICAL_ACT:
                            return Constants.CURVE_VERTICAL;
                        case Constants.CURVE_SLASH_ACT:
                            return Constants.CURVE_SLASH;
                        case Constants.CURVE_BACK_SLASH_ACT:
                            return Constants.CURVE_BACK_SLASH;
                        default:
                            return it;
                    }
                }
            }

            // put blank edges after all the tips, because tips-refs do not have any outgoing edges
            setTipsBranches.each { idx ->
                println "current node is ${lstCommits[i].id} subArray = $subArray"
                subArray[idx] = Constants.CURVE_BLANK
            }

            lstCommits[i].curves = subArray
        }

    }

    def _rotateRightEdge(edge) {
        switch (edge) {
            case Constants.CURVE_VERTICAL_ACT:
                return Constants.CURVE_SLASH_ACT;
            case Constants.CURVE_VERTICAL:
                return Constants.CURVE_SLASH;
            default:
                return edge;
        }
    }

    def _getVerticalCurve(isCurrentCommit) {
        return isCurrentCommit ? Constants.CURVE_VERTICAL_ACT : Constants.CURVE_VERTICAL;
    }

    def _getSlashCurve(isCurrentCommit) {
        return isCurrentCommit ?  Constants.CURVE_SLASH_ACT : Constants.CURVE_SLASH;
    }
}
