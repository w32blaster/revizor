
<%@ page import="com.revizor.Repository" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'repository.label', default: 'Repository')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		
		<div id="show-repository" class="" role="main">
			<h2><g:message code="default.show.label" args="[entityName]" /></h2>
			<g:if test="${flash.message}">
                <div class="alert alert-info">${flash.message}</div>
			</g:if>

            <div class="container">
                <div class="btn-group">

                    <g:link url="${createLink(uri: '/')}" class="btn btn-default btn-primary">
                        <span class="glyphicon glyphicon-home"></span>
                        <g:message code="default.home.label" />
                    </g:link>

                    <g:link action="list" class="btn btn-default btn-primary">
                        <span class="glyphicon glyphicon-inbox"></span>
                        <g:message code="default.list.label" args="[entityName]" />
                    </g:link>

                    <g:link action="create" class="btn btn-default btn-primary">
                        <span class="glyphicon glyphicon-plus"></span>
                        <g:message code="default.new.label" args="[entityName]" />
                    </g:link>
                </div>


			<ol class="property-list repository">
			
				<g:if test="${repositoryInstance?.image}">
					  <img class="avatar" src="${createLink(controller: 'repository', action: 'logo_image', id: repositoryInstance?.ident())}" />
				</g:if>

				<g:if test="${repositoryInstance?.url}">
				<li class="form-group">
					<span id="url-label" class="property-label"><g:message code="repository.url.label" default="Url" /></span>
					
						<span class="property-value" aria-labelledby="url-label"><g:fieldValue bean="${repositoryInstance}" field="url"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${repositoryInstance?.title}">
				<li class="form-group">
					<span id="title-label" class="property-label"><g:message code="repository.title.label" default="Title" /></span>
					
						<span class="property-value" aria-labelledby="title-label"><g:fieldValue bean="${repositoryInstance}" field="title"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${repositoryInstance?.folderName}">
				<li class="form-group">
					<span id="folderName-label" class="property-label"><g:message code="repository.folderName.label" default="Folder Name" /></span>
					
						<span class="property-value" aria-labelledby="folderName-label"><g:fieldValue bean="${repositoryInstance}" field="folderName"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${repositoryInstance?.members}">
				<li class="form-group">
					<span id="members-label" class="property-label"><g:message code="repository.members.label" default="members" /></span>
					
						<g:each in="${repositoryInstance.members}" var="r">
						<span class="property-value" aria-labelledby="members-label"><g:link controller="review" action="show" id="${r.id}">${r?.encodeAsHTML()}</g:link></span>
						</g:each>
					
				</li>
				</g:if>
			
				<g:if test="${repositoryInstance?.type}">
				<li class="form-group">
					<span id="type-label" class="property-label"><g:message code="repository.type.label" default="Type" /></span>
					
						<span class="property-value" aria-labelledby="type-label"><g:fieldValue bean="${repositoryInstance}" field="type"/></span>
					
				</li>
				</g:if>
			
			</ol>
			
			<g:form url="[resource:repositoryInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="btn btn-default btn-primary" action="edit" resource="${repositoryInstance}">
                        <span class="glyphicon glyphicon-pencil"></span>
                        <g:message code="default.button.edit.label" default="Edit" />
                    </g:link>
					<g:actionSubmit class="btn btn-default btn-danger" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
			</div>
		</div>
	</body>
</html>
