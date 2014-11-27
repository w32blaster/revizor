<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="settingsMain">
		<g:set var="entityName" value="${message(code: 'issueTracker.label', default: 'IssueTracker')}" />
		<title><g:message code="default.create.label" args="[entityName]" /></title>
	</head>
	<body>

	<h1><g:message code="default.create.label" args="[entityName]" /></h1>

	<g:render template="/layouts/flashMessage" />

	<g:render template="/layouts/showErrors" model="[instance: userInstance]"/>

	<g:render template="/layouts/actionButton" />


			<g:form class="form-horizontal" url="[resource:issueTrackerInstance, action:'save']" >
				<fieldset class="form">
					<g:render template="form"/>
				</fieldset>
				<fieldset class="buttons">
					<g:submitButton name="create" class="btn btn-primary" value="${message(code: 'default.button.create.label', default: 'Create')}" />
				</fieldset>
			</g:form>

	</body>
</html>
