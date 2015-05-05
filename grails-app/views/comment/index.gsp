
<%@ page import="com.revizor.Comment" %>
<%@ page import="com.revizor.CommentsFilter" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'comment.label', default: 'Comment')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>

    <!-- Breadcrumbs -->
    <div class="row" role="breadcrumb">
        <ul class="breadcrumb">
            <li class="active">${message(code: CommentsFilter.msgCodeFromValue(params.filter))}</li>
        </ul>
    </div>


    <div id="content-container">

		<div id="list-comment" class="content scaffold-list" role="main">

			<h1>${message(code: CommentsFilter.msgCodeFromValue(params.filter))}</h1>

			<g:if test="${flash.message}">
				<div class="alert alert-info">${flash.message}</div>
			</g:if>

			<div class="container">
				<div class="row">
					<div class="btn-group">

						<!-- My comments -->
						<% def cssClassMy = (params.filter == CommentsFilter.ONLY_MINE.toString()) ? 'active' : '' %>
						<g:link action="index" params="[filter: CommentsFilter.ONLY_MINE]" class="btn btn-default btn-primary ${cssClassMy}">
							<span class="glyphicon glyphicon-pencil"></span> 
							<g:message code="${CommentsFilter.ONLY_MINE.value()}" default="My comments" />
						</g:link>

						<!-- replies to me -->
						<% def cssClassReplies = (params.filter == CommentsFilter.REPLIES_TO_ME.toString()) ? 'active' : '' %>
						<g:link action="index" params="[filter: CommentsFilter.REPLIES_TO_ME]" class="btn btn-default btn-primary ${cssClassReplies}">
							<span class="glyphicon glyphicon glyphicon-share-alt"></span>
							<g:message code="${CommentsFilter.REPLIES_TO_ME.value()}" default="Replies to me" />
						</g:link>

						<!-- All comments -->
						<% def cssClassAll = (params.filter == CommentsFilter.ALL.toString()) ? 'active' : '' %>
						<g:link action="index" params="[filter: CommentsFilter.ALL]" class="btn btn-default btn-primary ${cssClassAll}">
							<g:message code="${CommentsFilter.ALL.value()}" default="All comments" />
						</g:link>

					</div>
				</div>

                <g:each var="comment" status="i"  in="${commentInstanceList}">
                    <div class="row ${(i % 2) == 0 ? 'even' : 'odd'}">
                        <div class="col-lg-3">

                            <dl>
                                <dt><g:message code="Review.label" /></dt>
                                <dd>
                                    <g:link controller="review" action="show" id="${comment.review.id}">
                                        ${comment.review.title}
                                    </g:link>
                                </dd>
                                <g:if test="${comment.type == com.revizor.CommentType.LINE_OF_CODE}">
                                    <dt><g:message code="file.label" /></dt>
                                    <dd>
                                        <a href="${comment.getLinkHref()}">
                                            <sc:fileNameWithoutPackage>
                                                ${comment.fileName}
                                            </sc:fileNameWithoutPackage>
                                        </a>
                                    </dd>
                                </g:if>
                            </dl>

                        </div>
                        <div class="col-lg-9">
                            <g:render template="/comment/comment" model="['comment' : comment, 'isReplyButtonHidden': true, 'indent': 0, 'isUnread': (comment.ident() in unreadComments) ]" />
                        </div>
                    </div>
                </g:each>

				<div class="pagination">
					<g:paginate total="${commentInstanceCount ?: 0}" />
				</div>
			</div>
		    </div>
        </div>
	</body>
</html>
