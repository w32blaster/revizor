
<%@ page import="com.revizor.utils.Constants; com.revizor.Chat" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="settingsMain">
		<g:set var="entityName" value="${message(code: 'chat.label', default: 'Chat')}" />
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

                        <g:sortableColumn property="name" title="${message(code: 'chat.name.label', default: 'Name')}" />

						<g:sortableColumn property="url" title="${message(code: 'chat.url.label', default: 'Url')}" />

						<g:sortableColumn property="username" title="${message(code: 'chat.username.label', default: 'Username')}" />

					</tr>
				</thead>
				<tbody>
				<g:each in="${chatInstanceList}" status="i" var="chatInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

                        <td style="padding: 2px;">
                            <img height="32" width="32" src="${resource(dir: 'images/chats', file: chatInstance.type.getImageUrl())}"
                                          alt="${fieldValue(bean: chatInstance, field: "type")}"
                                          title="${fieldValue(bean: chatInstance, field: "type")}" />
                        </td>

						<td><g:link action="show" id="${chatInstance.id}">${fieldValue(bean: chatInstance, field: "name")}</g:link></td>

						<td><%
						    if (chatInstance.url.length() > com.revizor.utils.Constants.MAX_LENGTH_SHOWN_IN_TABLE) {
                                println chatInstance.url.substring(0, com.revizor.utils.Constants.MAX_LENGTH_SHOWN_IN_TABLE) + "..."
                            }
                            else {
                                println chatInstance.url
                            }
						%></td>

						<td>${fieldValue(bean: chatInstance, field: "username")}</td>

					</tr>
				</g:each>
				</tbody>
			</table>

			<div class="pagination">
				<g:paginate total="${chatInstanceCount ?: 0}" />
			</div>

	</body>
</html>
