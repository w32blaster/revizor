<%@ page import="com.revizor.Chat" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="settingsMain">
		<g:set var="entityName" value="${message(code: 'chat.label', default: 'Chat')}" />
		<title><g:message code="default.edit.label" args="[entityName]" /></title>
	</head>
	<body>

        <h1><g:message code="default.create.label" args="[entityName]" /></h1>

        <g:render template="/layouts/flashMessage" />

        <g:render template="/layouts/showErrors" model="[instance: userInstance]"/>

        <g:render template="/layouts/actionButton" />


		<div class="form-container">
			<g:form class="form-horizontal" url="[resource:chatInstance, action:'update']" method="PUT" >
				<g:hiddenField name="version" value="${chatInstance?.version}" />
				<fieldset class="form">
					<g:render template="form"/>
				</fieldset>
				<fieldset class="buttons">
					<g:actionSubmit class="btn btn-primary"  action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
