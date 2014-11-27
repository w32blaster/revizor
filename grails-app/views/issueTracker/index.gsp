
<%@ page import="com.revizor.IssueTracker" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="settingsMain">
		<g:set var="entityName" value="${message(code: 'issueTracker.label', default: 'IssueTracker')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>


		<h1><g:message code="default.list.label" args="[entityName]" /></h1>

		<g:render template="/layouts/flashMessage" />

		<g:render template="/layouts/actionButton" />



			<table class="table">
			<thead>
					<tr>

						<th>#</th>

						<g:sortableColumn property="title" title="${message(code: 'issueTracker.title.label', default: 'Title')}" />

						<g:sortableColumn property="issueKeyPattern" title="${message(code: 'issueTracker.issueKeyPattern.label', default: 'Issue Key Pattern')}" />

						<g:sortableColumn property="type" title="${message(code: 'issueTracker.type.label', default: 'Type')}" />
					
						<g:sortableColumn property="url" title="${message(code: 'issueTracker.url.label', default: 'Url')}" />
					
						<g:sortableColumn property="username" title="${message(code: 'issueTracker.username.label', default: 'Username')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${issueTrackerInstanceList}" status="i" var="issueTrackerInstance">
					<tr>
						<td style="padding: 2px;"><img height="32" width="32" src="${resource(dir: 'images/issue-trackers', file: issueTrackerInstance.type.getImageUrl())}" /></td>
					
						<td><g:link action="show" id="${issueTrackerInstance.id}">${fieldValue(bean: issueTrackerInstance, field: "title")}</g:link></td>

						<td><code>${fieldValue(bean: issueTrackerInstance, field: "issueKeyPattern")}</code></td>

						<td>${fieldValue(bean: issueTrackerInstance, field: "type")}</td>
					
						<td>${fieldValue(bean: issueTrackerInstance, field: "url")}</td>
					
						<td>${fieldValue(bean: issueTrackerInstance, field: "username")}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>

			<div class="pagination">
				<g:paginate total="${issueTrackerInstanceCount ?: 0}" />
			</div>

	</body>
</html>
