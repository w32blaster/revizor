<%@ page import="com.revizor.ReviewFilter; com.revizor.ReviewStatus" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'review.label', default: 'Review')}" />
		<title><g:message code="default.create.label" args="[entityName]" /></title>
	</head>
	<body>

		<div id="create-review" class="content scaffold-create" role="main">
			<h2><g:message code="default.create.label" args="[entityName]" /></h2>

            <g:render template="/layouts/flashMessage" />

            <g:render template="/layouts/showErrors" model="[instance: reviewInstance]" />

            <div class="container">

                <g:render template="reviewFilterButtons" />

                <g:form url="[resource: reviewInstance, action:'save']" >
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

        </div>
	</body>
</html>
