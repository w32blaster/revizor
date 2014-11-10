
<%@ page import="com.revizor.User" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'user.label', default: 'User')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>

		
		<div id="list-user" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>

			<div class="container">
				<div class="btn-group">
					<g:link url="${createLink(uri: '/')}" class="btn btn-default btn-primary">
						<span class="glyphicon glyphicon-home"></span>
						<g:message code="default.home.label" />
					</g:link>

					<g:link action="list" class="btn btn-default btn-primary active">
						<span class="glyphicon glyphicon-inbox"></span>
						<g:message code="default.list.label" args="[entityName]" />
					</g:link>

					<g:link action="create" class="btn btn-default btn-primary">
						<span class="glyphicon glyphicon-plus"></span>
						<g:message code="default.new.label" args="[entityName]" />
					</g:link>
				</div>

					<table class="table">
					<thead>
							<tr>

								<g:sortableColumn property="email" title="${message(code: 'user.email.label', default: 'Email')}" />

								<g:sortableColumn property="username" title="${message(code: 'user.username.label', default: 'username')}" />

								<g:sortableColumn property="role" title="${message(code: 'user.role.label', default: 'Role')}" />

							</tr>
						</thead>
						<tbody>
						<g:each in="${userInstanceList}" status="i" var="userInstance">
							<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

								<td><g:link action="show" id="${userInstance.id}">${fieldValue(bean: userInstance, field: "email")}</g:link></td>

								<td>${fieldValue(bean: userInstance, field: "username")}</td>

								<td>${fieldValue(bean: userInstance, field: "role")}</td>

							</tr>
						</g:each>
						</tbody>
					</table>
					<div class="pagination">
						<g:paginate total="${userInstanceCount ?: 0}" />
					</div>
				</div>
		</div>
	</body>
</html>
