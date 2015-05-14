<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="settingsMain">
		<g:set var="entityName" value="${message(code: 'user.label', default: 'User')}" />
		<title><g:message code="default.create.label" args="[entityName]" /></title>
	</head>
	<body>

			<h1><g:message code="default.create.label" args="[entityName]" /></h1>

			<g:render template="/layouts/flashMessage" />

			<g:render template="/layouts/showErrors" model="[instance: userInstance]"/>

			<g:render template="/layouts/actionButton" />

			<div class="form-container">
				<g:form url="[resource:userInstance, action:'save']"  class="form-horizontal" >
					<fieldset class="form">
                        <g:hiddenField name="hasImage" value="false"/>
						<g:hiddenField name="type" value="${com.revizor.UserType.INNER}" />

						<g:render template="form"/>
					</fieldset>
					<fieldset class="buttons">
						<g:submitButton name="create" class="btn btn-default btn-primary" value="${message(code: 'default.button.create.label', default: 'Create')}" />

					</fieldset>
				</g:form>
		</div>
	</body>
</html>
