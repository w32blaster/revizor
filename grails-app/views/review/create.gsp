<%@ page import="com.revizor.ReviewFilter; com.revizor.ReviewStatus" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'review.label', default: 'Review')}" />
		<title><g:message code="default.create.label" args="[entityName]" /></title>
	</head>
	<body>


    <!-- Breadcrumbs -->
    <div class="row" role="breadcrumb">
        <ul class="breadcrumb">
            <li>
                <a href="${createLink(controller: 'repository', action: 'dashboard', id: session.activeRepository)}">${message(code: "default.home.label")}</a>
            </li>
            <li>
                <a href="${createLink(controller: "review", action:"index", 'params':[filter: com.revizor.ReviewFilter.ONLY_MINE])}">
                    ${message(code: 'reviews.only.mine')}
                </a>
            </li>
            <li class="active">${message(code: "reviews.create.new")}</li>
        </ul>
    </div>


    <div id="content-container">

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
                        <g:submitButton class="btn btn-default btn-primary" name="create" value="${message(code: 'default.button.create.label', default: 'Create')}" />
                        <a href="<g:createLink controller="repository" action="dashboard" id="${params.id}" />" class="btn btn-link"><g:message code="cancel" /></a>
                    </fieldset>

                </g:form>
            </div>
            <p />

            </div>
        </div>
	</body>
</html>


