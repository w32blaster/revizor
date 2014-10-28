
<%@ page import="com.revizor.Repository" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'repository.label', default: 'Repository')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>

		<div id="list-repository" class="content scaffold-list" role="main">
			<h2><g:message code="default.list.label" args="[entityName]" /></h2>
			<g:if test="${flash.message}">
                <div class="alert alert-info">${flash.message}</div>
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
       </div>
	</body>
</html>
