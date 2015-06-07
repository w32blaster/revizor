
<%@ page import="com.revizor.Issue" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="settingsMain">
		<g:set var="entityName" value="${message(code: 'issue.label', default: 'Issue')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>

	<h1><g:message code="default.list.label" args="[entityName]" /></h1>

	<g:render template="/layouts/flashMessage" />

	<g:render template="/layouts/actionButton" />

		<table class="table">
			<thead>
					<tr>
					
						<g:sortableColumn property="key" title="${message(code: 'issue.key.label', default: 'Key')}" />
					
						<th><g:message code="issue.tracker.label" default="Tracker" /></th>
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${issueInstanceList}" status="i" var="issueInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${issueInstance.id}">${fieldValue(bean: issueInstance, field: "key")}</g:link></td>
					
						<td>${fieldValue(bean: issueInstance, field: "tracker")}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>

			<div class="pagination">
				<g:paginate total="${issueInstanceCount ?: 0}" />
			</div>

	</body>
</html>
