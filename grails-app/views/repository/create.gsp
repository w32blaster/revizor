<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="settingsMain">
		<g:set var="entityName" value="${message(code: 'repository.label', default: 'Repository')}" />
		<title><g:message code="default.create.label" args="[entityName]" /></title>
	</head>
	<body>

			<h2><g:message code="default.create.label" args="[entityName]" /></h2>

			<g:render template="/layouts/flashMessage" />

			<g:render template="/layouts/showErrors" model="[instance: repositoryInstance]" />

			<g:render template="/layouts/actionButton" />


				<g:form url="[resource:repositoryInstance, action:'save']" >
					<fieldset class="form">
                        <g:hiddenField name="hasImage" value="false"/>
						<g:render template="form"/>
					</fieldset>
					<fieldset class="buttons">
						<g:submitButton name="create" class="btn btn-default btn-primary" value="${message(code: 'default.button.create.label', default: 'Create')}" />
					</fieldset>
				</g:form>


	</body>
</html>
