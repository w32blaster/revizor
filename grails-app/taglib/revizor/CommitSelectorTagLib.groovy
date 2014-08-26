package revizor

import com.revizor.repos.Commit
import com.revizor.utils.Constants;

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

    /**
     * Prepares the list of commits to be displayed as history graph
     *
     * @param list of all the commits. This list will be updated
     * @param list of SHA keys of master/default branch commit
     * @param map "reference SHA key" <=> list of branch names
     */
    def prepareHistoryGraph(lstCommits, lstMaster) {
          
        def mapIds = [:]
        lstCommits.eachWithIndex {commit, i -> 
            mapIds.put(commit.id, i);
        }

        lstCommits.reverseEach { commit ->
            if (commit.parents.size() == 1) {
                def currentNodeIndex = mapIds.get(commit.id)
                def parentNodeIndex = mapIds.get(commit.parents[0])

                // add current node to the children list of its parent node
                lstCommits[parentNodeIndex].children.add(commit.id);

                // draw a line (edge) between current node and its parent
                _addLinesBetweenTwoNodes(parentNodeIndex, currentNodeIndex, lstCommits);
            }
            else if (commit.parents.size() > 1) {
                // when a node has more than one parents, this is a merging of two branches/revisions
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
     */
    def _addLinesBetweenTwoNodes(from, to, lstCommits) {
        for (i in from+1..to) {
            if (lstCommits[i-1].children.size() > 1) {
                // if parent has more than one children, here come(s) new branch(es)
                if (lstCommits[i].curves.size() == 0) {
                    // the very first branch should be vertical.
                    lstCommits[i].curves.add( (i == to) ? Constants.CURVE_VERTICAL_ACT : Constants.CURVE_VERTICAL);
                }
                else {
                    // others branchs should be curve, because here new branch "goes to the right" away of basic line
                    lstCommits[i].curves.add( (i == to) ? Constants.CURVE_SLASH_ACT : Constants.CURVE_SLASH);
                }
            }
            else {
                lstCommits[i].curves.add( (i == to) ? Constants.CURVE_VERTICAL_ACT : Constants.CURVE_VERTICAL);
            }
        }
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
}
