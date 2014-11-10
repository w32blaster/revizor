<%@ page import="com.revizor.User" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'user.label', default: 'User')}" />
		<title><g:message code="default.edit.label" args="[entityName]" /></title>
	</head>
	<body>
		<nav class="navbar navbar-default" role="navigation">
			<ul class="nav navbar-nav">
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</nav>
		
		<div id="edit-user" class="content scaffold-edit" role="main">
			<h1><g:message code="default.edit.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
				<div class="alert alert-info" role="status">${flash.message}</div>
			</g:if>
			<g:hasErrors bean="${userInstance}">
			<ul class="errors" role="alert">
				<g:eachError bean="${userInstance}" var="error">
					<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
				</g:eachError>
			</ul>
			</g:hasErrors>

			<div class="container">

				<%-- Form "Upload Avatar" --%>
				<g:render template="select_avatar" model="[id: userInstance?.ident()]"/>

				<%-- Form "Edit aliases" --%>
				<g:render template="editAliases" model="[aliases: userInstance?.aliases, userId: userInstance?.ident()]"/>

				<div class="input-group">
					<g:form url="[resource:userInstance, action:'update']" method="PUT" >
						<g:hiddenField name="version" value="${userInstance?.version}" />

						<fieldset class="form">
							<g:render template="form"/>
						</fieldset>
						<fieldset class="buttons">
							<g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" />
						</fieldset>
					</g:form>
				</div>

			</div>
		</div>
	</body>
</html>
