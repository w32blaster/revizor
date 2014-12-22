package revizor

import com.revizor.Review
import com.revizor.repos.Commit
import com.revizor.utils.Constants

class CommitSelectorTagLib {

    static namespace = "sc"

    final private static int CURVE_WIDTH = 20; // in px

    /**
     *  Prints flat list of all the commits with a checkbox. Allows user to select commits for a review
     */
    def selectCommitsForReview = {attrs, body ->

        def repo = attrs.repo.initImplementation();
        def list = repo.getListOfCommits();
        def lstChecked = attrs.checkedItems ? attrs.checkedItems : []

        int count = 0;
        def isChecked = "";
        out << """<table class='table table-striped table-hover'>
                    <thead>
                        <th>#</th>
                        <th> </th>
                        <th>${message(code: 'commit.message')}</th>
                        <th>${message(code: 'commit.author')}</th>
                    </thead>
                    <tbody>
        """
        for (Commit rev : list) {
            isChecked = (rev.id.equals(attrs.selected) || rev.id in lstChecked) ? "checked" : ""
            out << "<tr ${isChecked ? "class='active'" : ""}>"
            out << "<td><input class='commit-checkbox' type='checkbox' name='commits' value='${rev.id}' ${isChecked} /></td>"
            out << "<td>${rev.id.subSequence(0, 7)}</td>"
            out << "<td class='truncate'>${rev.message }</span></td>"
            out << "<td><span class='label label-default'>${rev.author}</span></td>" /* rev.getId().getName() */
            out << "</tr>"
            count++;
            isChecked = ""
        }
        out << "</tbody></table>"
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
                    "<svg height='${list.size() * Constants.ROW_HEIGHT}' width='${CURVE_WIDTH * maxLaneIdx + 15}' overflow='hidden'><g>${arrCommits[0]}</g></svg></div>"

            list.each { Commit rev ->
                def urlToObserveChangeset = g.createLink(controller: "observe", action: "show") + "/${attrs.repo.ident()}/${rev.id}/"
                outHtml <<= """
                        <tr title="${rev.id}" height="${Constants.ROW_HEIGHT}">
                            <td><span class="graph-line-text" style="padding-left: ${rev.padding}px"><a href="${urlToObserveChangeset}">${_getCommitMessageHtml(rev)}</a></span></td>
                            <td><span class="label label-default" title="${rev.authorEmail}">${rev.author}</span><td>
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

    /**
     * Detects Smart Commits in a header and replaces them with appropriate links.
     * For example, command "+review" will be wrapped to a review linked with current commit
     *
     * @param header
     */
    def _getCommitMessageHtml(Commit commit) {
        def messageHtml = commit.message

        // make Smart Commit clickable
        if (commit.message.contains(Constants.SMART_COMMIT_CREATE_REVIEW)) {
            def review = Review.findBySmartCommitId(commit.id)
            if (review) {
                def htmlLinkReview = "<a href='${g.createLink(controller: "review", action: "show", id: review?.ident(),)}'/>" +
                        "<span class='label label-primary'>$Constants.SMART_COMMIT_CREATE_REVIEW</span></a>";
                messageHtml = commit.message.replace(Constants.SMART_COMMIT_CREATE_REVIEW, htmlLinkReview)
            }
        }

        // add link to full message
        if (commit.fullMessage.length() > (commit.message.length() + 1)) {
            messageHtml += " <a type='button' class='graph-tooltip' data-toggle='popover' tabindex='0' data-trigger='focus' data-placement='top' data-html='true' data-container='body' title='${commit.author}' data-content='${commit.fullMessage.replace('\n', "<br>").encodeAsHTML()}'>•••</a>"
        }

        return messageHtml
    }
}
