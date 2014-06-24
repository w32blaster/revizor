
<%@ page import="com.revizor.Comment" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'comment.label', default: 'Comment')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<nav class="navbar navbar-default" role="navigation">
			<ul class="nav navbar-nav">
				<li><a href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li class="active"><g:link action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</nav>
		
		<div id="list-comment" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
			<thead>
					<tr>
					
						<th><g:message code="comment.author.label" default="Author" /></th>
					
						<g:sortableColumn property="text" title="${message(code: 'comment.text.label', default: 'Text')}" />
					
						<g:sortableColumn property="commit" title="${message(code: 'comment.commit.label', default: 'Commit')}" />
					
						<g:sortableColumn property="fileName" title="${message(code: 'comment.fileName.label', default: 'File Name')}" />
					
						<g:sortableColumn property="lineOfCode" title="${message(code: 'comment.lineOfCode.label', default: 'Line Of Code')}" />
					
						<th><g:message code="comment.review.label" default="Review" /></th>
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${commentInstanceList}" status="i" var="commentInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${commentInstance.id}">${fieldValue(bean: commentInstance, field: "author")}</g:link></td>
					
						<td>${fieldValue(bean: commentInstance, field: "text")}</td>
					
						<td>${fieldValue(bean: commentInstance, field: "commit")}</td>
					
						<td>${fieldValue(bean: commentInstance, field: "fileName")}</td>
					
						<td>${fieldValue(bean: commentInstance, field: "lineOfCode")}</td>
					
						<td>${fieldValue(bean: commentInstance, field: "review")}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${commentInstanceCount ?: 0}" />
			</div>
		</div>
	</body>
</html>
