
<%@ page import="com.revizor.User" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="settingsMain">
		<g:set var="entityName" value="${message(code: 'user.label', default: 'User')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>


			<h1><g:message code="default.list.label" args="[entityName]" /></h1>

			<g:render template="/layouts/flashMessage" />

			<g:render template="/layouts/actionButton" />



					<table class="table">
					<thead>
							<tr>

								<g:sortableColumn property="email" title="${message(code: 'user.email.label', default: 'Email')}" />

								<g:sortableColumn property="username" title="${message(code: 'user.username.label', default: 'username')}" />

								<g:sortableColumn property="role" title="${message(code: 'user.role.label', default: 'Role')}" />

							</tr>
						</thead>
						<tbody>

                        <h3><g:message code="registered.in.revizor" /></h3>
						<g:each in="${userInstanceList}" status="i" var="userInstance">
							<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

								<td><g:link action="show" id="${userInstance.id}">${fieldValue(bean: userInstance, field: "email")}</g:link></td>

								<td>${fieldValue(bean: userInstance, field: "username")}</td>

								<td>${fieldValue(bean: userInstance, field: "role")}</td>

							</tr>
						</g:each>

                        <h3><g:message code="ldap.users" /></h3>
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

	</body>
</html>
