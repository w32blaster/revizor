<%@ page import="com.revizor.Review; com.revizor.Issue" %>
<%--
    Prints the list of all the reviews. Just a plain table
--%>

<r:script>

    /**
     * Load issue and put it to the specified header container
     */
    <g:render template="scriptLoadIssueTicket" model="[isSmall:true]" />

    <g:each in="${groupedIssues}" var="issueTicket">
        requestDetails("loaded-issue-${issueTicket.getKey()}-details-id", ${issueTicket.getValue()[0].ident()}, "${issueTicket.getKey()}");
    </g:each>

</r:script>

<h1></h1>

<table class="table">
    <tbody>
    <g:each in="${groupedIssues}" var="issueTicket">

        <tr>
            <th colspan="3" id="loaded-issue-${issueTicket.getKey()}-details-id">
                <span class="glyphicon glyphicon-refresh glyphicon-refresh-animate"></span>
            </th>
        </tr>

        <g:each in="${issueTicket.getValue()}" status="i" var="issue">
            <g:render template="rowReviewInTable" model="[review: issue.review]" />
        </g:each>

    </g:each>


        <tr>
            <th colspan = "3">Without tickets:</th>
        </tr>

        <g:each in="${reviewInstanceList}" status="i" var="review">
            <g:render template="rowReviewInTable" model="[review: review]" />
        </g:each>

    </tbody>
</table>