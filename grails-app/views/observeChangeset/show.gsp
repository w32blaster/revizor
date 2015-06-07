
<%@ page import="com.revizor.Comment; com.revizor.ReviewFilter; com.revizor.Review" %>
<%@ page import="com.revizor.CommentType" %>
<%@ page import="com.revizor.ReviwerStatus" %>
<%@ page import="com.revizor.utils.Id" %>

<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'review.label', default: 'Review')}" />
    <title><g:message code="default.show.label" args="[entityName]" /></title>
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

        <div class="row">

            <small>
                <g:link controller="repository" action="dashboard" id="${repositoryInstance.ident()}" class="btn btn-default btn-success btn-xs">
                    <span class="glyphicon glyphicon-arrow-left"></span>
                    <span class="glyphicon glyphicon-home"></span>
                </g:link>
            </small>

            <g:render template="createReviewButton" />

            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>

            <h3><g:message code="changeset.header" args="[commit.message]" /></h3>

            <table class="table">
                <tr>
                    <th>SHA</th>
                    <td>${commit.id}</td>
                </tr>
                <tr>
                    <th><g:message code="repository.label"/></th>
                    <td>
                        <g:if test="${repositoryInstance.image}">
                            <img height="32" width="32" class="avatar img-rounded" src="${createLink(controller:'repository', action: 'logo_image', id: repositoryInstance.ident())}" />
                        </g:if>
                        ${repositoryInstance.title}</td>
                </tr>
                <tr>
                    <th><g:message code="commit.author"/></th>
                    <td>${commit.author}</td>
                </tr>
                <tr>
                    <th><g:message code="email.label"/></th>
                    <td>${commit.authorEmail}</td>
                </tr>
                <tr>
                    <th><g:message code="commit.date"/></th>
                    <td>${commit.commitDate}</td>
                </tr>
                <tr>
                    <th><g:message code="commit.message"/></th>
                    <td>${raw(commit.fullMessage.replaceAll('\\n', '<br />'))}</td>
                </tr>
            </table>

        </div>


    </div>
</div>
</body>
</html>
