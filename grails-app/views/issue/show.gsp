
<%@ page import="com.revizor.Issue" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="settingsMain">
		<g:set var="entityName" value="${message(code: 'issue.label', default: 'Issue')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>


	<h1><g:message code="default.show.label" args="[entityName]" /></h1>

	<g:render template="/layouts/flashMessage" />

	<g:render template="/layouts/actionButton" />

			<ol class="property-list issue">
			
				<g:if test="${issueInstance?.key}">
				<li class="fieldcontain">
					<span id="key-label" class="property-label"><g:message code="issue.key.label" default="Key" /></span>
					
						<span class="property-value" aria-labelledby="key-label"><g:fieldValue bean="${issueInstance}" field="key"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${issueInstance?.tracker}">
				<li class="fieldcontain">
					<span id="tracker-label" class="property-label"><g:message code="issue.tracker.label" default="Tracker" /></span>
					
						<span class="property-value" aria-labelledby="tracker-label"><g:link controller="issueTracker" action="show" id="${issueInstance?.tracker?.id}">${issueInstance?.tracker?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form url="[resource:issueInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${issueInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>


	</body>
</html>
