<%@ page import="com.revizor.Repository" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="settingsMain">
		<g:set var="entityName" value="${message(code: 'repository.label', default: 'Repository')}" />
		<title><g:message code="default.edit.label" args="[entityName]" /></title>
	</head>
	<body>

			<h2><g:message code="default.edit.label" args="[entityName]" /></h2>

            <g:render template="/layouts/flashMessage" />

            <g:render template="/layouts/showErrors" model="[instance: repositoryInstance]" />

			<g:render template="/layouts/actionButton" />

			<div class="form-container">
			<g:form url="[resource:repositoryInstance, action:'update']" method="PUT" class="form-horizontal" >
				<g:hiddenField name="version" value="${repositoryInstance?.version}" />
				<fieldset class="form">
					<g:render template="form"/>
				</fieldset>
				<fieldset class="buttons">
					<g:actionSubmit class="btn btn-default btn-primary" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" />
				</fieldset>
			</g:form>
			</div>

	</body>
</html>
