
<%@ page import="com.revizor.Chat" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="settingsMain">
		<g:set var="entityName" value="${message(code: 'chat.label', default: 'Chat')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>

        <h1><g:message code="default.show.label" args="[entityName]" /></h1>

        <g:render template="/layouts/flashMessage" />

        <g:render template="/layouts/actionButton" />

			<ol class="property-list chat">
			
				<g:if test="${chatInstance?.url}">
				<li class="fieldcontain">
					<span id="url-label" class="property-label"><g:message code="chat.url.label" default="Url" /></span>
					
						<span class="property-value" aria-labelledby="url-label"><g:fieldValue bean="${chatInstance}" field="url"/></span>
					
				</li>
				</g:if>

                <g:if test="${chatInstance?.name}">
                    <li class="fieldcontain">
                        <span id="name-label" class="property-label"><g:message code="chat.username.label" default="Name" /></span>

                        <span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${chatInstance}" field="name"/></span>

                    </li>
                </g:if>

				<g:if test="${chatInstance?.username}">
				<li class="fieldcontain">
					<span id="username-label" class="property-label"><g:message code="chat.username.label" default="Username" /></span>
					
						<span class="property-value" aria-labelledby="username-label"><g:fieldValue bean="${chatInstance}" field="username"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${chatInstance?.password}">
				<li class="fieldcontain">
					<span id="password-label" class="property-label"><g:message code="chat.password.label" default="Password" /></span>
					
						<span class="property-value" aria-labelledby="password-label"><g:fieldValue bean="${chatInstance}" field="password"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${chatInstance?.type}">
				<li class="fieldcontain">
					<span id="type-label" class="property-label"><g:message code="chat.type.label" default="Type" /></span>
					
						<span class="property-value" aria-labelledby="type-label"><g:fieldValue bean="${chatInstance}" field="type"/></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form url="[resource:chatInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="btn btn-primary" action="edit" resource="${chatInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="btn btn-link" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>

	</body>
</html>
