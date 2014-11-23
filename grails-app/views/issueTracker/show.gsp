
<%@ page import="com.revizor.IssueTracker" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="settingsMain">
		<g:set var="entityName" value="${message(code: 'issueTracker.label', default: 'IssueTracker')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>


	<h1><g:message code="default.show.label" args="[entityName]" /></h1>

	<g:render template="/layouts/flashMessage" />

	<g:render template="/layouts/actionButton" />


			<ol class="property-list issueTracker">
			
				<g:if test="${issueTrackerInstance?.issueKeyPattern}">
				<li class="fieldcontain">
					<span id="issueKeyPattern-label" class="property-label"><g:message code="issueTracker.issueKeyPattern.label" default="Issue Key Pattern" /></span>
					
						<span class="property-value" aria-labelledby="issueKeyPattern-label"><g:fieldValue bean="${issueTrackerInstance}" field="issueKeyPattern"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${issueTrackerInstance?.password}">
				<li class="fieldcontain">
					<span id="password-label" class="property-label"><g:message code="issueTracker.password.label" default="Password" /></span>
					
						<span class="property-value" aria-labelledby="password-label">*****</span>
					
				</li>
				</g:if>
			
				<g:if test="${issueTrackerInstance?.title}">
				<li class="fieldcontain">
					<span id="title-label" class="property-label"><g:message code="issueTracker.title.label" default="Title" /></span>
					
						<span class="property-value" aria-labelledby="title-label"><g:fieldValue bean="${issueTrackerInstance}" field="title"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${issueTrackerInstance?.type}">
				<li class="fieldcontain">
					<span id="type-label" class="property-label"><g:message code="issueTracker.type.label" default="Type" /></span>
					
						<span class="property-value" aria-labelledby="type-label"><g:fieldValue bean="${issueTrackerInstance}" field="type"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${issueTrackerInstance?.url}">
				<li class="fieldcontain">
					<span id="url-label" class="property-label"><g:message code="issueTracker.url.label" default="Url" /></span>
					
						<span class="property-value" aria-labelledby="url-label"><g:fieldValue bean="${issueTrackerInstance}" field="url"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${issueTrackerInstance?.username}">
				<li class="fieldcontain">
					<span id="username-label" class="property-label"><g:message code="issueTracker.username.label" default="Username" /></span>
					
						<span class="property-value" aria-labelledby="username-label"><g:fieldValue bean="${issueTrackerInstance}" field="username"/></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form url="[resource:issueTrackerInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="btn btn-primary" action="edit" resource="${issueTrackerInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="btn btn-link" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>

	</body>
</html>
