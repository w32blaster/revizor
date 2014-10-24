package com.revizor.repos

import com.revizor.utils.Constants
import com.revizor.utils.Utils

/**
 * Helps to build a history graph.
 *
 * @see Wiki: https://github.com/w32blaster/revizor/wiki/History-graph-in-Revizor
 */
class GraphBuilder {

    /**
     * Public.
     *
     * Prepares the list of commits to be displayed as history graph
     *
     * @param list of all the commits. This list will be updated
     * @param list of SHA keys of master/default branch commit
     * @param list of SHA keys of tips (references)
     */
    def prepareHistoryGraph(lstCommits) {

        // prepare the map of commits: build map "SHA key" <=> "index"
        // and fill "children" collection for each commit
        def mapIds = [:]
        for (int i = lstCommits.size()-1; i > -1; i--) {
            def commit = lstCommits[i]
            mapIds.put(commit.id, i);
            if (commit.parents.size() > 0) {
                commit.parents.each { parent ->
                    def parentNodeIndex = mapIds.get(parent)
                    lstCommits[parentNodeIndex].children.add(commit.id);
                }
            }
        }

        // draw a history graph
        for (int i = lstCommits.size()-1; i > -1; i--) {
            def commit = lstCommits[i]

            if (commit.parents.size() > 0) {

                commit.parents.eachWithIndex { parent, ii ->
                    def parentNodeIndex = mapIds.get(parent)
                    def isMerge = (ii > 0) // ii is >1 when current node has few parents (=is merged)

                    // draw a line (edge) between current node and its parent
                    _addLinesBetweenTwoNodes(parentNodeIndex, i, lstCommits, isMerge);

                    // for debugging purposes
                    if (Constants.IS_TREE_LOG_ENABLED) {
                        println "draw from ${lstCommits[parentNodeIndex].id} to ${lstCommits[i].id}"
                        Utils.printTree(lstCommits)
                        println "------------->"
                    }
                }

            }
            else if (commit.parents.size() == 0) {
                // root
                lstCommits[i].curves.add(Constants.CURVE_ROOT);
                lstCommits[i].currentCurveIdx = 0;
            }
        }

        return lstCommits;
    }

    /**
     * Makes the path (edge) between two nodes in the graph.
     * Function fills the array "curves" with curve types. Later we will display it with SVG.
     * The idea is a path should repeat "shape"
     * of already existing graph.
     *
     * @param from - index of node that we want draw line from (inclusive)
     * @param to - index of node that we want draw line to (exclusive)
     * @param lstCommits - list of all commits to be modified
     * @param isBelongingToMaster - (boolean) whether current commit ("to") belongs to master or not
     * @param isMerge - if the current node is merged (has more than one parent)
     */
    def _addLinesBetweenTwoNodes(from, to, lstCommits, boolean isMerge) {

        for (i in from-1..to) {

            def isFirstIteration = i == (from-1)
            _copyEdgesFromPreviousLine(lstCommits, i, to)

            /*
                isNeededToGoAround - flag showing whether the same slot on the next row is free or not.
                This returns TRUE (asterisk is the current iteration), we need to turn right to walk around:
                | | |
                | | * <-- current iteration

                but this returns FALSE (the same slot on next row is free), we can proceed with vertical curve:
                | |
                | | * <-- current iteration

             */
            def isNeededToGoAround = (lstCommits[from-1]?.curves?.size() != 0
                    && lstCommits[i].curves.size() > lstCommits[i+1].curves.size()
                    && lstCommits[i].curves[lstCommits[i+1].currentCurveIdx] != Constants.CURVE_VERTICAL)
            def isNewBranch = (lstCommits[i+1].children.size() != 0 && isNeededToGoAround)
            def isTaken = _isNextSlotTaken(lstCommits, i, isFirstIteration)

            // current iteration goes directly to a target node, not curves at the middle
            def isCurrentIterationGoesToNode = (i == to)

            if (isMerge && isCurrentIterationGoesToNode) {
                // change the current node as "merged"
                lstCommits[i].curves[lstCommits[i].currentCurveIdx] = Constants.CURVE_MERGE_ACT
            }
            else if (isNewBranch || isTaken) {
                _drawRowForNewBranch(lstCommits, i, isCurrentIterationGoesToNode, isFirstIteration)
            }
            else {
                _drawRow(lstCommits, i, isCurrentIterationGoesToNode, isFirstIteration)
            }

            if (isCurrentIterationGoesToNode) {
                // save the row number of current node
                lstCommits[i].currentCurveIdx = 0
            }

        }
    }

    /**
     * Detects crossroads (when curves are crossing each other)
     *
     * @param lstCommits
     * @param i
     * @param isFirstIteration
     * @return
     */
    private boolean _isNextSlotTaken(lstCommits, int i, boolean isFirstIteration) {
        def currentIterationIndex = lstCommits[i+1].currentCurveIdx
        return isFirstIteration ? lstCommits[i].curves[currentIterationIndex] != null : false
    }

    /**
     * Draws a row in case if current row has the same amount of branches, as previous (no new branches here).
     *
     * @param isBelongingToMaster
     * @param lstCommits
     * @param i
     * @param isCurrentLineActive
     */
    private void _drawRow( lstCommits, i, boolean isCurrentLineActive, boolean isFirstIteration) {
        lstCommits[i].curves[lstCommits[i].currentCurveIdx] = _getVerticalCurve(isCurrentLineActive);
    }

    /**
     * Draw one row in the point, where a new branch is started.
     *
     * For example:
     *  | / <-- this "slash" curve will be drawn by this method
     *  |
     *  |
     *
     * @param lstCommits
     * @param i
     * @param isCurrentLineActive
     * @param isFirstIteration
     */
    private void _drawRowForNewBranch(lstCommits, i, isCurrentLineActive, isFirstIteration) {

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
    def _copyEdgesFromPreviousLine(lstCommits, i, toIndex) {

        def prevCurvesCount = lstCommits[i+1].curves.size()
        def currentCurvesCount = lstCommits[i].curves.size() + 1
        def isCurrentNodeMerged = (lstCommits[i].curves[lstCommits[i].currentCurveIdx] == Constants.CURVE_MERGE_ACT)
        def isNearestNodeIsSlash = isNearestNodeTypeEquals(Constants.CURVE_BACK_SLASH, lstCommits[i]) ||
                isNearestNodeTypeEquals(Constants.CURVE_BACK_SLASH_ACT, lstCommits[i])

        if (prevCurvesCount > 1 && prevCurvesCount > currentCurvesCount && !isNearestNodeIsSlash && !isCurrentNodeMerged) {

            // copy all the curves except current one from the previous line
            /*def subArray
            if (true ) {
                subArray = lstCommits[i+1].curves[1..(prevCurvesCount-1)]
            }
            else {
                subArray = lstCommits[i+1].curves[0..(prevCurvesCount-2)]
            }*/

            def subArray = lstCommits[i+1].curves[0..(prevCurvesCount-1)]

            // but if a copied branch has a node, then replace it with a blank space
            if (i == toIndex) {
                subArray = subArray.collect {
                    if (it in [Constants.CURVE_VERTICAL_ACT, Constants.CURVE_MERGE_ACT, Constants.CURVE_SLASH_ACT, Constants.CURVE_BACK_SLASH_ACT]) {
                        return Constants.CURVE_VERTICAL
                    }
                    else {
                        return it;
                    }
                }
            }

            def isSubArrayHasOnlyBlank = (subArray.size() == 1 && subArray[0] == Constants.CURVE_BLANK)
            if (!isSubArrayHasOnlyBlank) {
                lstCommits[i].curves.removeAll({ true })
                lstCommits[i].curves.addAll(subArray)
            }
        }

        // trim blanks
        while(lstCommits[i].curves.size() > 1 && lstCommits[i].curves.last() == Constants.CURVE_BLANK) {
            lstCommits[i].curves.pop()
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
