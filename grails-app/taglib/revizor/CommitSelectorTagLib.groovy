package revizor

import com.revizor.repos.Commit
import com.revizor.utils.Constants
import com.revizor.utils.Utils

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

            prepareHistoryGraph(list, listOfMasterIds, mapBranches.keySet())

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
                if (parentNodeIndex) lstCommits[parentNodeIndex].children.add(commit.id);
            }
        }

        def tipFromLastLine;
        def masterTipIdx = 0;

        // draw a history graph
        lstCommits.eachWithIndex { commit, i ->

            // remember the master tip index
            if (lstMaster.contains(commit.id) && lstTips.contains(commit.id)) masterTipIdx = i;

            if (commit.parents.size() == 1) {
                def parentNodeIndex = mapIds.get(commit.parents[0])

                // draw a line (edge) between current node and its parent
                def isBelongingToMaster = lstMaster.contains(commit.id);
                def isTip = lstTips.contains(commit.id);
                tipFromLastLine = _addLinesBetweenTwoNodes(parentNodeIndex, i, lstCommits, isBelongingToMaster, isTip, tipFromLastLine);

                if (Constants.IS_TREE_LOG_ENABLED) {
                    println "draw from ${lstCommits[parentNodeIndex].id} to ${lstCommits[i].id}"
                    Utils.printTree(lstCommits)
                    println "------------->"
                }
            }
        }

        // decorate tree
        moveNonMasterBranchesToRight(lstCommits, masterTipIdx, lstMaster)

        return lstCommits;
    }

    /**
     * Decorate master branch: if there are some branches, that goes after
     * a master tip, then move them to the right.
     * It looks more natural.
     *
     * @param lstCommits
     * @param masterTipIdx
     * @param lstMaster
     */
    private void moveNonMasterBranchesToRight(lstCommits, int masterTipIdx, lstMaster) {
        def lastCommitIdx = lstCommits.size() - 1;
        def isFirstTime = true;
        if (masterTipIdx != 0 && masterTipIdx < lastCommitIdx) {
            for (i in masterTipIdx..lastCommitIdx) {
                if (lstCommits[i].currentCurveIdx == 0 && !lstMaster.contains(lstCommits[i].id)) {
                    // shift this layer in decorative purposes
                    lstCommits[i].curves.add(0, Constants.CURVE_BLANK);
                    if (isFirstTime) {
                        _shiftLayer(lstCommits[i])
                        isFirstTime = false
                    }
                    else {
                        lstCommits[i].currentCurveIdx++
                    }
                }
            }
        }
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
     */
    def _addLinesBetweenTwoNodes(from, to, lstCommits, isBelongingToMaster, isTip, tipFromLastLine) {

        for (i in from+1..to) {

            def isFirstIteration = i == (from+1)
            _copyEdgesFromPreviousLine(lstCommits, i, to, tipFromLastLine, isBelongingToMaster)

            /*
                isSameSlotOnNextRowTaken - flag showing that the same slot on the next row is free or not.
                This returns TRUE (asterisk is the current iteration):
                | | |
                | | * <-- current iteration

                but this returns FALSE (the same slot on next row is free)
                | |
                | | * <-- current iteration

             */
            def isSameSlotOnNextRowTaken = (lstCommits[from+1]?.curves?.size() != 0 && lstCommits[i].curves.size() >= lstCommits[i-1].curves.size())
            def isNewBranch = (lstCommits[i-1].children.size() != 0 && isSameSlotOnNextRowTaken)

            // current iteration goes directly to a target node, not curves at the middle
            def isCurrentIterationGoesToNode = (i == to)

            if (isNewBranch) {
                _drawRowForNewBranch(isBelongingToMaster, lstCommits, i, isCurrentIterationGoesToNode, isFirstIteration)
            }
            else {
                _drawRow(isBelongingToMaster, lstCommits, i, isCurrentIterationGoesToNode, isFirstIteration)
            }

            if (isCurrentIterationGoesToNode) {
                // save the row number of current node
                lstCommits[i].currentCurveIdx = isBelongingToMaster ? 0 : (lstCommits[i].curves.size() - 1)
            }

        }

        tipFromLastLine = null

        if (isTip) {
            // remember, that on this line we have a tip. On the next iteration we will put there a blank space
            tipFromLastLine = (isBelongingToMaster ? 0 : lstCommits[to].currentCurveIdx)
        }

        return tipFromLastLine
    }

    /**
     * Draws a row in case if current row has the same amount of branches, as previous (no new branches here).
     *
     * @param isBelongingToMaster
     * @param lstCommits
     * @param i
     * @param isCurrentLineActive
     */
    private void _drawRow(isBelongingToMaster, lstCommits, i, boolean isCurrentLineActive, boolean isFirstIteration) {
        if (isBelongingToMaster) {
            lstCommits[i].curves.add(0, _getVerticalCurve(isCurrentLineActive));
            // as long as it is a new branch to the left, we need to "move" all the
            // lines on this row and make them curve slash (only on the first iteration)
            if (isFirstIteration) {
                _shiftLayer(lstCommits[i])
            }
            else {
                lstCommits[i].currentCurveIdx++;
            }
        } else {

            def isPreviousLineHavingMoreCurves = (lstCommits[i-1].curves.size() > (lstCommits[i].curves.size() + 1) )

            if (isPreviousLineHavingMoreCurves) {
                // if nearest curve is empty (it was "ended"), then turn left on the graph
                lstCommits[i].curves.add(_getBackSlashCurve(isCurrentLineActive));
                if (isCurrentLineActive) lstCommits[i].currentCurveIdx = lstCommits[i].curves.size() - 1;
            }
            else {
                lstCommits[i].curves.add(_getVerticalCurve(isCurrentLineActive));
            }
        }
    }

    /**
     * Draw one row in the point, where a new branch is started.
     *
     * For example:
     *  | / <-- this "slash" curve will be drawn by this method
     *  |
     *  |
     *
     * @param isBelongingToMaster
     * @param lstCommits
     * @param i
     * @param isCurrentLineActive
     * @param isFirstIteration
     */
    private void _drawRowForNewBranch(isBelongingToMaster, lstCommits, i, isCurrentLineActive, isFirstIteration) {

        if (isBelongingToMaster) {

            if (_isFirstColumnEmpty(lstCommits[i].curves)) {
                lstCommits[i].curves[0] = _getVerticalCurve(isCurrentLineActive);
            }
            else {
                lstCommits[i].curves.add(0, _getVerticalCurve(isCurrentLineActive));
                if (isFirstIteration) {
                    // as long as it is a new branch to the left, we need to "move" all the lines on this row and make them curve slash
                    _shiftLayer(lstCommits[i])
                }
            }

        } else {
            lstCommits[i].curves.add(_getSlashCurve(isCurrentLineActive));
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
    def _copyEdgesFromPreviousLine(lstCommits, i, toIndex, idxOfTip, isBelongingToMaster) {

        def prevCurvesCount = lstCommits[i-1].curves.size()
        def currentCurvesCount = lstCommits[i].curves.size() + 1
        def isNearestNodeIsSlash = isNearestNodeTypeEquals(Constants.CURVE_BACK_SLASH, lstCommits[i]) ||
                                        isNearestNodeTypeEquals(Constants.CURVE_BACK_SLASH_ACT, lstCommits[i])

        if (prevCurvesCount > 1 && prevCurvesCount > currentCurvesCount && !isNearestNodeIsSlash) {

            // copy all the curves except current one from the previous line
            def subArray
            if (isBelongingToMaster) {
                subArray = lstCommits[i-1].curves[1..(prevCurvesCount-1)]
            }
            else {
                subArray = lstCommits[i-1].curves[0..(prevCurvesCount-2)]
            }

            // but if a copied branch has a node, then replace it with a blank space
            if (i == toIndex) {
                subArray = subArray.collect {
                    if (it in [Constants.CURVE_VERTICAL_ACT, Constants.CURVE_SLASH_ACT, Constants.CURVE_BACK_SLASH_ACT]) {
                        return Constants.CURVE_BLANK
                    }
                    else {
                        return it;
                    }
                }
            }

            // put blank edges after all the tips, because tips/refs do not have any outgoing edges
            if (null != idxOfTip) {
                //subArray[idxOfTip] = Constants.CURVE_BLANK
            }

            def isSubArrayHasOnlyBlank = (subArray.size() == 1 && subArray[0] == Constants.CURVE_BLANK)
            if (!isSubArrayHasOnlyBlank) {
                lstCommits[i].curves = subArray
            }
        }

        // trim
        while(lstCommits[i].curves.size() > 1 && lstCommits[i].curves.last() == Constants.CURVE_BLANK) {
            lstCommits[i].curves = lstCommits[i].curves[0..-2]
        }

    }

    /**
     * Shift the layer to the left.
     *
     * It means, if we added a new node A to the left of this line:
     *    | | |
     * then others neighbors should be shifted to the left:
     *    A / / /
     *
     * @param commit
     * @return
     */
    def _shiftLayer(commit) {
        if (commit.curves.size() > 1) {
            for (ii in 1..commit.curves.size() - 1) {
                commit.curves[ii] = _rotateRightEdge(commit.curves[ii])
            }
            commit.currentCurveIdx++;
        }
    }

    /**
     * "Rotate" curve. It means, that we move right the layer and want to change curve types.
     *
     * For example, we had the next structure:
     *
     *          | | |
     *          | | |
     *
     * And we want to move (shift) the upper layer to the right:
     *
     * shift->>  | | |
     *          / / /
     *
     * As you can see, all the curves from lower layer were modified ("rotated to the right").
     * This rotation should be done using this method.
     *
     * @param edge
     * @return rotated edge
     */
    def _rotateRightEdge(edge) {
        switch (edge) {
            case Constants.CURVE_VERTICAL_ACT:
                return Constants.CURVE_SLASH_ACT;
            case Constants.CURVE_VERTICAL:
                return Constants.CURVE_SLASH;
            case Constants.CURVE_BACK_SLASH_ACT:
                return Constants.CURVE_VERTICAL_ACT;
            case Constants.CURVE_BACK_SLASH:
                return Constants.CURVE_VERTICAL;
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

    def _getBackSlashCurve(isCurrentCommit) {
        return isCurrentCommit ?  Constants.CURVE_BACK_SLASH_ACT : Constants.CURVE_BACK_SLASH;
    }

    private boolean isNearestNodeTypeEquals(byte type, Commit commit) {
        def nearestCurveIdx = commit.curves.size() - 1
        return (nearestCurveIdx >= 0 && commit.curves[nearestCurveIdx] == type);
    }
    /**
     * Returns TRUE if only the first column is blank.
     *
     * @param arrCurves
     * @return
     */
    private boolean _isFirstColumnEmpty(arrCurves) {
        return arrCurves[0] == Constants.CURVE_BLANK
    }
}
