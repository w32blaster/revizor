<%@ page import="com.revizor.CommentType" %>
<%@ page import="com.revizor.Review" %>

<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'review.label', default: 'Review')}" />
    <title><g:message code="default.show.label" args="[entityName]" /></title>

    <link rel="stylesheet" href="${resource(dir: 'css/prettify', file: 'prettify.css')}" type="text/css">
    <script type="text/javascript" src="${resource(dir: 'js/prettify', file: 'prettify.js')}"></script>
</head>
<body>

<div class="row">

    <div class="col-md-3">

        <ft:showFilesForReview
                repo="${repositoryInstance}"
                commitID="${commit.id}"
                urlPrefix="${urlPrefix}"/>

    </div>

    <div class="col-md-9">

        <small>
            <g:link controller="repository" action="dashboard" id="${repositoryInstance.ident()}" class="btn btn-default btn-success btn-xs">
                <span class="glyphicon glyphicon-arrow-left"></span>
                <span class="glyphicon glyphicon-home"></span>
            </g:link>
        </small>

    <%--

    Is not implemented yet.
    Issue: https://github.com/w32blaster/revizor/issues/8

      <g:render template="viewTypeButtons"></g:render>

    --%>


        <g:if test="${flash.message}">
            <div class="alert alert-warning">${flash.message}</div>
        </g:if>


    <!-- Print the Diff of the considered file -->
        <r:script>
            prettyPrint();
        </r:script>

        <sc:showDiffForCommit
                repo="${repositoryInstance}"
                commitID="${commit.id}"
                fileName="${fileName}" />

    </div>

</div>
</body>
</html>
