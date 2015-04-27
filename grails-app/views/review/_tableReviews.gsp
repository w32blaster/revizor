<%--
    Prints the list of all the reviews. Just a plain table
--%>

<table class="table">
    <thead>
    <tr>

        <g:sortableColumn property="title" title="${message(code: 'review.title.label', default: 'Title')}" />

        <th><g:message code="review.repository.label" default="Repository" /></th>

        <th width="32"> </th>

        <th width="32"> </th>

        <th width="64"> </th>

    </tr>
    </thead>
    <tbody>
    <g:set var="mapComments" value="${com.revizor.Comment.list().groupBy { it.review.id } }" />

    <g:each in="${reviewInstanceList}" status="i" var="reviewInstance">
        <g:render template="rowReviewInTable" model="[review: reviewInstance, commentsByReview: mapComments]" />
    </g:each>
    </tbody>
</table>