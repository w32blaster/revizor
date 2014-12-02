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
    <g:each in="${reviewInstanceList}" status="i" var="reviewInstance">
        <g:render template="rowReviewInTable" model="[review: reviewInstance]" />
    </g:each>
    </tbody>
</table>