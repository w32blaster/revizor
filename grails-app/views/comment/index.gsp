
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

		<div id="list-comment" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>

			<g:if test="${flash.message}">
				<div class="alert alert-info">${flash.message}</div>
			</g:if>

			<div class="container">
				<div class="row">
					<div class="btn-group">

                        <g:link class="btn btn-default btn-primary" url="${createLink(uri: '/')}">
                            <span class="glyphicon glyphicon-home"></span>
                            <g:message code="default.home.label" default="Home"/>
                        </g:link>

						<!-- My comments -->
						<% def cssClassMy = (params.filter == CommentsFilter.ONLY_MINE.toString()) ? 'active' : '' %>
						<g:link action="index" params="[filter: CommentsFilter.ONLY_MINE]" class="btn btn-default btn-primary ${cssClassMy}">
							<span class="glyphicon glyphicon-pencil"></span> 
							<g:message code="CommentsFilter.ONLY_MINE.value()" default="My comments" />
						</g:link>

						<!-- replies to me -->
						<% def cssClassReplies = (params.filter == CommentsFilter.REPLIES_TO_ME.toString()) ? 'active' : '' %>
						<g:link action="index" params="[filter: CommentsFilter.REPLIES_TO_ME]" class="btn btn-default btn-primary ${cssClassReplies}">
							<span class="glyphicon glyphicon-thumbs-up"></span> 
							<g:message code="CommentsFilter.REPLIES_TO_ME.value()" default="Replies to me" />
						</g:link>

						<!-- All comments -->
						<% def cssClassAll = (params.filter == CommentsFilter.ALL.toString()) ? 'active' : '' %>
						<g:link action="index" params="[filter: CommentsFilter.ALL]" class="btn btn-default btn-primary ${cssClassAll}">
							<g:message code="CommentsFilter.ALL.value()" default="All comments" />
						</g:link>

					</div>
				</div>

				<div class="row">
					<g:each var="comment" in="${commentInstanceList}">
						
						<g:link controller="review" action="show" id="${comment.review.id}" class="btn btn-default btn-xs">
							${comment.review.title}
						</g:link>
					    <g:render template="/comment/comment" model="['comment' : comment]" />
					    </br>
					</g:each>
				</div>

				<div class="pagination">
					<g:paginate total="${commentInstanceCount ?: 0}" />
				</div>
			</div>
		</div>
	</body>
</html>
