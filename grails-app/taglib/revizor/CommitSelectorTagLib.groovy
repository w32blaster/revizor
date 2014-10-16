package revizor

import com.revizor.repos.Commit
import com.revizor.repos.GraphBuilder
import com.revizor.utils.Constants
import com.revizor.utils.Utils

class CommitSelectorTagLib {

    static namespace = "sc"

    final private static int CURVE_WIDTH = 20; // in px
    final private static int ROW_HEIGHT = 35; // in px
    final private static int PADDING_LEFT = 13; // in px
    final private static int PADDING_TOP = 7; // in px
    final private static String CURVE_STYLE = " style='stroke:rgb(0,0,0);stroke-width:1;fill:none' ";
    final private static String CIRCLE_STYLE = " style='stroke:rgb(0,0,0);stroke-width:2;fill:blue' ";

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
            def graphHtml = "<div id='history-graph' style='position: absolute; top: ${(ROW_HEIGHT / 2) - PADDING_TOP}px'><svg height='${list.size() * ROW_HEIGHT}' width='${CURVE_WIDTH * 6}' overflow='hidden'><g>"

            list.reverse().eachWithIndex { Commit rev, int i ->

                rev.curves.eachWithIndex { curve, int idx ->
                    graphHtml <<= _drawSVGCurve(curve, idx, list, i)
                }

                outHtml <<= """
                        <tr title="${rev.id.subSequence(0, 7)}" height="${ROW_HEIGHT}">
                            <td><span class="graph-line-text" style="padding-left: ${rev.curves.size() * CURVE_WIDTH + PADDING_LEFT}px">${rev.message }</span></td>
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

    /**
     * Draw one SVG curve
     *
     * @param curve
     * @param idx
     * @param list
     * @param i
     *
     * @return HTML code for the current SVG line/curve
     */
    def _drawSVGCurve(byte curve, int idx, list, int i) {

        def isTop = (i == 0)
        def X = (idx * CURVE_WIDTH) + PADDING_LEFT
        def Yup = i * ROW_HEIGHT + PADDING_TOP
        def Ydown = (i+1) * ROW_HEIGHT + PADDING_TOP

        switch (curve) {
            case Constants.CURVE_VERTICAL_ACT:
                return "<line x1='${X}' y1='${Yup}' x2='${X}' y2='${Ydown}' $CURVE_STYLE />" +
                        "<circle cx='${X}' cy='${Yup}' r='3' $CIRCLE_STYLE />"
                break;

            case Constants.CURVE_VERTICAL:
                return "<line x1='${X}' y1='${Yup}' x2='${X}' y2='${Ydown}' $CURVE_STYLE />"
                break;

            case Constants.CURVE_SLASH:
            case Constants.CURVE_SLASH_ACT:

                def isParentNodeOnLineBelow = (list[i+1].id == list[i].parents[0])
                def htmlCurve
                if (isParentNodeOnLineBelow) {
                    def parentNodeIdx = list[i+1].currentCurveIdx
                    def Xparent = parentNodeIdx * CURVE_WIDTH + CURVE_WIDTH
                    htmlCurve =  "<path d='M ${Xparent} ${Ydown} C ${Xparent} ${Yup} ${X} ${Ydown} ${X} ${Yup}' $CURVE_STYLE />"
                }
                else {
                    htmlCurve = "<path d='M ${X - CURVE_WIDTH} ${Ydown} C ${X - CURVE_WIDTH} ${Yup} ${X} ${Ydown} ${X} ${Yup}' $CURVE_STYLE />"
                }

                if (curve == Constants.CURVE_SLASH_ACT) {
                    htmlCurve += "<circle cx='${X}' cy='${Yup}' r='3' $CIRCLE_STYLE />"
                }
                return htmlCurve

            case Constants.CURVE_BACK_SLASH:
            case Constants.CURVE_BACK_SLASH_ACT:

                def htmlCurve = "<path d='M${X} ${Yup} C ${X} ${Ydown} ${X - CURVE_WIDTH} ${Yup} ${X - CURVE_WIDTH} ${Ydown}' $CURVE_STYLE />"

                if (curve == Constants.CURVE_BACK_SLASH_ACT) {
                    htmlCurve += "<circle cx='${X}' cy='${Yup}' r='3' $CIRCLE_STYLE />"
                }
                return htmlCurve

            case Constants.CURVE_BLANK:
                // shouldn't be seen. For debugging purposes
                return "<text x='${X}' y='${Yup}' dx='0'>X</text>"
                break;

            case Constants.CURVE_MERGE:
                return "<line x1='${X}' y1='${Yup}' x2='${X}' y2='${Ydown}' $CURVE_STYLE />" +
                        "<path d='M${X} ${Yup} C ${X} ${Ydown} ${X + CURVE_WIDTH} ${Yup} ${X + CURVE_WIDTH} ${Ydown}' $CURVE_STYLE />" +
                        "<circle cx='${X}' cy='${Yup}' r='3' $CIRCLE_STYLE />"
        }

        return ""
    }

}
