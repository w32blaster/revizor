<%@ page import="com.revizor.Review; com.revizor.Issue" %>
<%--
    Prints the list of all the reviews. Just a plain table
--%>

<table class="table">
    <thead>
        <tr>

            <g:sortableColumn property="title" title="${message(code: 'review.title.label', default: 'Title')}" />
            <th><g:message code="review.repository.label" default="Repository" /></th>
            <th><g:message code="review.author.label" default="Author" /></th>
        </tr>
    </thead>
<tbody>
<g:each in="${groupedIssues}" var="issueTicket">

    <tr>
        <th colspan = "3">${issueTicket.getKey()}</th>
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