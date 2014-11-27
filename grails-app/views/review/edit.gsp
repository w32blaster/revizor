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

            <g:render template="/layouts/flashMessage" />

            <g:render template="/layouts/showErrors" model="[instance: reviewInstance]" />

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
