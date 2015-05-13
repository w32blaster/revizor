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

        def repoImpl = attrs.repo.initImplementation();

        def arrCommits = []
        try {
            arrCommits = repoImpl.getGraphSVG();
        }
        catch(NullPointerException npe) {

            // this can happen, when cloning was unsuccessful and the repository exists in the DB, but
            // the folder is empty or repo is corrupted.
            out << """
                    <div class='alert alert-danger'>
                        <strong>${g.message(code: 'fatal.error')}!</strong> <br />
                        ${g.message(code: 'no.such.repo.or.corrupted')}</br>
                        <span class="glyphicon glyphicon-arrow-right" aria-hidden="true"></span>
                        <a href="${g.createLink(controller: 'repository', action: 'show', id: attrs.repo.ident())}">${attrs.repo.title}</a>
                    </div>""";
            return;
        }

        def mapReviews = _getMapOfUsedReviews(Review.findAllByRepository(attrs.repo))

        def list = arrCommits[1]
        int maxLaneIdx = arrCommits[2]

        def graphHtml = "<div id='history-graph' style='position: absolute; top: 3px'>" +
                "<svg height='${list.size() * Constants.ROW_HEIGHT}' width='${CURVE_WIDTH * maxLaneIdx + 15}' overflow='hidden'><g>${arrCommits[0]}</g></svg></div>"

        def outHtml = "<table class='table table-condensed'>"
        list.each { Commit rev ->
            def urlToObserveChangeset = g.createLink(controller: "observe", action: "show") + "/${attrs.repo.ident()}/${rev.id}/"
            outHtml <<= """
                    <tr title="${rev.id}" height="${Constants.ROW_HEIGHT}">
                        <td>
                            <div class="graph-line-text" style="padding-left: ${rev.padding}px">
                                <a class="graph-label" href="${urlToObserveChangeset}">${_getCommitMessageHtml(rev)}</a>
                            </div>
                        </td>
                        <td>${_renderReviewsCount(mapReviews.get(rev.id))}</td>
                        <td><span class="label label-default" title="${rev.authorEmail}">${rev.author}</span><td>
                        <td><a href="${createLink(controller: 'review', action: 'create', id: attrs.repo.ident(), params: [selected: rev.id])}" class="btn btn-default btn-xs">
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
     * renders the label with popuver, containing list of reviews relating to current review
     *
     * @param lstReviews
     * @return
     */
    def _renderReviewsCount(lstReviews) {
        if(lstReviews && !lstReviews.isEmpty()) {
            def htmlContent = ""
            for (Review review : lstReviews) {
                def url = g.createLink(controller: "review", action: "show")
                htmlContent <<= "<a href='${url}/${review.id}'>#${review.id}</a><br />"
            }

            def out = "<a type='button' class='gpopover' data-toggle='popover' tabindex='0' data-trigger='focus'" +
                    " data-placement='top' data-html='true' data-container='body' data-title='Reviews' data-content=\"${htmlContent}\">";
            out <<= "<span class='label label-default'>${message(code: 'reviews.count.plural', args: [lstReviews.size()])}</span>"
            out <<= "</a>"
            return out;
        }
        else {
            return ""
        }
    }

    /**
     * Builds the map "commit revision" <==> "list of used reviews"
     *
     * @param reviews
     * @return
     */
    def _getMapOfUsedReviews(reviews) {
        def mapReviews = [:]
        reviews.each { review ->
            for (String sha : review.commits) {
                if (!mapReviews.containsKey(sha)) {
                    mapReviews.put(sha, [review])
                }
                else {
                    mapReviews.get(sha).add(review)
                }
            }
        }
        return mapReviews
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
            messageHtml += " <a type='button' class='graph-tooltip gpopover' data-toggle='popover' tabindex='0' data-trigger='focus' data-placement='top' data-html='true' data-container='body' title='${commit.author}' data-content='${commit.fullMessage.replace('\n', "<br>").encodeAsHTML()}'>•••</a>"
        }

        return messageHtml
    }
}
