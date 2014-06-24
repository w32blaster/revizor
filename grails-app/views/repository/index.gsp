
<%@ page import="com.revizor.Repository" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'repository.label', default: 'Repository')}" />
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
		
		<div id="list-repository" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table class="table">
			<thead>
					<tr>
					
						<g:sortableColumn property="url" title="${message(code: 'repository.url.label', default: 'Url')}" />
					
						<g:sortableColumn property="title" title="${message(code: 'repository.title.label', default: 'Title')}" />
					
						<g:sortableColumn property="folderName" title="${message(code: 'repository.folderName.label', default: 'Folder Name')}" />
					
						<g:sortableColumn property="type" title="${message(code: 'repository.type.label', default: 'Type')}" />
					
						<td> </td>
					</tr>
				</thead>
				<tbody>
				<g:each in="${repositoryInstanceList}" status="i" var="repositoryInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${repositoryInstance.id}">${fieldValue(bean: repositoryInstance, field: "url")}</g:link></td>
					
						<td>${fieldValue(bean: repositoryInstance, field: "title")}</td>
					
						<td>${fieldValue(bean: repositoryInstance, field: "folderName")}</td>
					
						<td>${fieldValue(bean: repositoryInstance, field: "type")}</td>

						<td>
							<g:link controller="review" action="create" id="${repositoryInstance.id}" class="btn btn-primary active" role="button">
								<g:message code="default.new.label" args="[Review]" />
							</g:link>
						</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${repositoryInstanceCount ?: 0}" />
			</div>
		</div>
	</body>
</html>
