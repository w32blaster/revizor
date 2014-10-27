package revizor

import com.revizor.repos.Commit
import com.revizor.repos.GraphBuilder
import com.revizor.utils.Constants
import com.revizor.utils.Utils

class CommitSelectorTagLib {

    static namespace = "sc"

    final private static int CURVE_WIDTH = 20; // in px

    /**
     *  Prints flat list of all the commits with a checkbox. Allows user to select commits for a review
     */
    def selectCommitsForReview = {attrs, body ->

        def repo = attrs.repo.initImplementation();
        def list = repo.getListOfCommits();

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
    }

    /**
     * Prints the list of commits with graph. Intended to be displayed on the dashboard.
     *
     */
    def buildFlatListofCommits = { attrs, body ->

            def repo = attrs.repo.initImplementation();
            def arrCommits = repo.getGraphSVG();

            def list = arrCommits[1]
            int maxLaneIdx = arrCommits[2]

            def outHtml = "<table class='table table-condensed'>"
            def graphHtml = "<div id='history-graph' style='position: absolute; top: 3px'>" +
                    "<svg height='${list.size() * Constants.ROW_HEIGHT}' width='${CURVE_WIDTH * maxLaneIdx + 10}' overflow='hidden'><g>${arrCommits[0]}</g></svg></div>"

            list.each { Commit rev ->

                outHtml <<= """
                        <tr title="${rev.id}" height="${Constants.ROW_HEIGHT}">
                            <td><span class="graph-line-text" style="padding-left: ${rev.padding}px">${rev.message}</span></td>
                            <td><span class="label label-default">${rev.author}</span><td>
                            <td><a href="${createLink(controller: 'review', action: 'create', id: attrs.repo.ident(), params: [selected: rev.id])}" class="btn btn-default btn-xs tree-context-button">
                                <span class="glyphicon glyphicon-plus"></span>
                            </a>
                            </td>
                        </tr>
                        """
            }
            outHtml <<= "</table>"

            out << graphHtml + outHtml

    }
}
