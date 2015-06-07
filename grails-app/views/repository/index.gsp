
<%@ page import="com.revizor.Repository" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="settingsMain">
		<g:set var="entityName" value="${message(code: 'repository.label', default: 'Repository')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>

			<h2><g:message code="default.list.label" args="[entityName]" /></h2>

			<g:render template="/layouts/flashMessage" />

			<g:render template="/layouts/actionButton" />

			<table class="table">
			<thead>
					<tr>

						<td> </td>
						<g:sortableColumn property="url" title="${message(code: 'repository.url.label', default: 'Url')}" />
					
						<g:sortableColumn property="title" title="${message(code: 'repository.title.label', default: 'Title')}" />
					
						<g:sortableColumn property="folderName" title="${message(code: 'repository.folderName.label', default: 'Folder Name')}" />
					
						<g:sortableColumn property="type" title="${message(code: 'repository.type.label', default: 'Type')}" />

					</tr>
				</thead>
				<tbody>
				<g:each in="${repositoryInstanceList}" status="i" var="repositoryInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
                        <td>
                            <g:if test="${repositoryInstance?.image}">
                                <img class="avatar" width="16" height="16" src="${createLink(controller: 'repository', action: 'logo_image', id: repositoryInstance?.ident())}" />
                            </g:if>
                        </td>
						<td>
							<g:link action="show" id="${repositoryInstance.id}">
								<hs:maskPassword>
									${fieldValue(bean: repositoryInstance, field: "url")}
								</hs:maskPassword>
							</g:link>
						</td>
						<td>${fieldValue(bean: repositoryInstance, field: "title")}</td>
						<td><samp>${fieldValue(bean: repositoryInstance, field: "folderName")}</samp></td>
						<td>${fieldValue(bean: repositoryInstance, field: "type")}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>

			<div class="pagination">
				<g:paginate total="${repositoryInstanceCount ?: 0}" />
			</div>


	</body>
</html>
