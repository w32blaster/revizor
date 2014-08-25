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
            if(commit.parents.size() > 0) {
                commit.parents.each { parentId ->
                    _addLinesBetweenTwoNodes(mapIds.get(parentId), mapIds.get(commit.id), lstCommits);
                }
            }
        }

        return lstCommits;
    }

    /**
     * "Draws" the path (edge) between two nodes in the graph.
     * The idea is a path should repeat "shape"
     * of already existing graph.
     *
     * @param from - index of node that we want draw line from (inclusive)
     * @param to - index of node that we want draw line to (exclusive)
     */
    def _addLinesBetweenTwoNodes(from, to, lstCommits) {
        for (i in from+1..to) {
            lstCommits[i].curves.add(Constants.CURVE_VERTICAL);
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
