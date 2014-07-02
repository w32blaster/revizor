<%@ page import="com.revizor.ReviewStatus" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'review.label', default: 'Review')}" />
		<title><g:message code="default.create.label" args="[entityName]" /></title>
	</head>
	<body>

		<div id="create-review" class="content scaffold-create" role="main">
			<h1><g:message code="default.create.label" args="[entityName]" /></h1>

			<g:if test="${flash.message}">
                <div class="alert alert-info" role="alert">${flash.message}</div>
			</g:if>

			<g:hasErrors bean="${reviewInstance}">
                    <g:eachError bean="${reviewInstance}" var="error">
                        <div class="alert alert-danger" role="alert" <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>>
                            <g:message error="${error}"/>
                        </div>
                    </g:eachError>
			</g:hasErrors>

			<g:form url="[resource:reviewInstance, action:'save']" >
				<fieldset class="form">
					<g:render template="form"/>
				</fieldset>

				<g:hiddenField name="repository" value="${repository?.id}" />
				<g:hiddenField name="status" value="${ReviewStatus.OPEN}" />
				<g:hiddenField name="author.id" value="${session.user.id}" />

				<fieldset class="buttons">
					<g:submitButton class="btn btn-default btn-success" name="create" value="${message(code: 'default.button.create.label', default: 'Create')}" />
				</fieldset>

			</g:form>
		</div>
	</body>
</html>
