package revizor

import com.revizor.repos.Commit
import com.revizor.repos.GraphBuilder
import com.revizor.utils.Constants
import com.revizor.utils.Utils

class CommitSelectorTagLib {

    static namespace = "sc"

    final private static int CURVE_WIDTH = 10; // in px
    final private static int ROW_HEIGHT = 35; // in px

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

            def graphBuilder = new GraphBuilder()
            list = graphBuilder.prepareHistoryGraph(list.reverse(), listOfMasterIds, mapBranches.keySet())

            def outHtml = "<table class='table table-condensed'>"
            def graphHtml = "<div id='history-graph' style='position: absolute;'><svg height='${list.size() * ROW_HEIGHT}' width='100' overflow='hidden'><g>"

            list.reverse().eachWithIndex { Commit rev, int i ->

                rev.curves.eachWithIndex { curve, int idx ->
                    graphHtml <<= "<line x1='${idx * CURVE_WIDTH}' y1='${i * ROW_HEIGHT}' x2='${idx * CURVE_WIDTH}' y2='${(i+1) * ROW_HEIGHT}' style='stroke:rgb(255,0,0);stroke-width:2' />"
                }

                outHtml <<= """
                        <tr title="${rev.id.subSequence(0, 7)}" height="${ROW_HEIGHT}">
                            <td><span style="padding-left: ${rev.curves.size() * CURVE_WIDTH}px">${rev.message }</span></td>
                            <td><span class="label label-default">${rev.author}</span><td>
                            <td><a href="${createLink(controller: 'review', action: 'create', id: attrs.repo.ident(), params: [selected: rev.id])}" class="btn btn-default btn-xs tree-context-button">
                                <span class="glyphicon glyphicon-plus"></span>
                            </a>
                            </td>
                        </tr>
                        """
            }
            graphHtml <<= "</g></svg></div>"
            outHtml <<= "</table>"

            out << graphHtml + outHtml
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

}
