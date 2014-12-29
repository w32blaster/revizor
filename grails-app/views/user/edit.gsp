<%@ page import="com.revizor.User" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="settingsMain">
		<g:set var="entityName" value="${message(code: 'user.label', default: 'User')}" />
		<title><g:message code="default.edit.label" args="[entityName]" /></title>
	</head>
	<body>


				<h1><g:message code="default.edit.label" args="[entityName]" /></h1>

				<g:render template="/layouts/flashMessage" />

				<g:render template="/layouts/showErrors" model="[instance: userInstance]" />

				<g:render template="/layouts/actionButton" />

				<div class="form-container">
					<g:form url="[resource:userInstance, action:'update']" method="PUT" >
						<g:hiddenField name="version" value="${userInstance?.version}" />

						<fieldset class="form">
							<g:render template="form"/>
						</fieldset>
						<fieldset class="buttons">
							<g:actionSubmit class="btn btn-default btn-primary" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" />
						</fieldset>
					</g:form>
				</div>

				<hr />

				<%-- Form "Upload Avatar" --%>
				<g:render template="select_avatar" model="[id: userInstance?.ident()]"/>

				<%-- Form "Edit aliases" --%>
				<g:render template="editAliases" model="[aliases: userInstance?.aliases, userId: userInstance?.ident()]"/>


	</body>
</html>
