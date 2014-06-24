<%@ page import="com.revizor.Repository" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'repository.label', default: 'Repository')}" />
		<title><g:message code="default.edit.label" args="[entityName]" /></title>
	</head>
	<body>
		
		<nav class="navbar navbar-default" role="navigation">
			<ul class="nav navbar-nav">
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
			</ul>
		</nav>
		
		
		<div id="edit-repository" class="content scaffold-edit" role="main">
			<h1><g:message code="default.edit.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>

			<g:hasErrors bean="${repositoryInstance}">
				<ul class="errors" role="alert">
					<g:eachError bean="${repositoryInstance}" var="error">
					<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
					</g:eachError>
				</ul>
			</g:hasErrors>
			
			<g:render template="/user/select_avatar" model="[id: repositoryInstance?.ident()]"/>

			<div class="input-group">
			<g:form url="[resource:repositoryInstance, action:'update']" method="PUT" >
				<g:hiddenField name="version" value="${repositoryInstance?.version}" />
				<fieldset class="form">
					<g:render template="form"/>
				</fieldset>
				<fieldset class="buttons">
					<g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" />
				</fieldset>
			</g:form>
			</div>
			
		</div>
	</body>
</html>
