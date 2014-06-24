<%@ page import="com.revizor.ReviewStatus" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'review.label', default: 'Review')}" />
		<title><g:message code="default.create.label" args="[entityName]" /></title>
	</head>
	<body>

		<nav class="navbar navbar-default" role="navigation">
			<ul class="nav navbar-nav">
				<li><a href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li class="active"><g:link action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</nav>


		<div id="create-review" class="content scaffold-create" role="main">
			<h1><g:message code="default.create.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<g:hasErrors bean="${reviewInstance}">
			<ul class="errors" role="alert">
				<g:eachError bean="${reviewInstance}" var="error">
				<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
				</g:eachError>
			</ul>
			</g:hasErrors>
			<g:form url="[resource:reviewInstance, action:'save']" >
				<fieldset class="form">
					<g:render template="form"/>
				</fieldset>

				<g:hiddenField name="repository" value="${repository?.id}" />
				<g:hiddenField name="status" value="${ReviewStatus.OPEN}" />

				<fieldset class="buttons">
					<g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
