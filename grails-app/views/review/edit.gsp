<%@ page import="com.revizor.ReviewFilter; com.revizor.Review" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'review.label', default: 'Review')}" />
		<title><g:message code="default.edit.label" args="[entityName]" /></title>
	</head>
	<body>

	<div id="edit-review" class="content scaffold-edit" role="main">
		<h2><g:message code="default.edit.label" args="[entityName]" /></h2>
			<g:if test="${flash.message}">
                <div class="alert alert-info">${flash.message}</div>
			</g:if>
			<g:hasErrors bean="${reviewInstance}">
                <g:eachError bean="${reviewInstance}" var="error">
                    <div class="alert alert-danger" role="alert" <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>>
                        <g:message error="${error}"/>
                    </div>
                </g:eachError>
            </g:hasErrors>


            <div class="container">

                <g:render template="reviewFilterButtons" />

                <div class="input-group">

                    <g:form url="[resource:reviewInstance, action:'update']" method="PUT" >
                        <g:hiddenField name="version" value="${reviewInstance?.version}" />
                        <g:hiddenField name="repository" value="${repository?.id}" />
                        <fieldset class="form">
                            <g:render template="form"/>
                        </fieldset>
                        <fieldset class="buttons">
                            <g:actionSubmit class="btn btn-default btn-success" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" />
                        </fieldset>
                    </g:form>
                </div>

		    </div>
        </div>
	</body>
</html>
