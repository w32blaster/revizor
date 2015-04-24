<%@ page import="com.revizor.ReviewFilter; com.revizor.Review" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'review.label', default: 'Review')}" />
		<title><g:message code="default.edit.label" args="[entityName]" /></title>
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
            <li class="active">${message(code: "review.edit.of", args: [reviewInstance.title])}</li>
        </ul>
    </div>


    <div id="content-container">

	    <div id="edit-review" class="content scaffold-edit" role="main">

            <h2><g:message code="review.edit.of" args="[reviewInstance.title]" /></h2>

            <g:render template="/layouts/flashMessage" />

            <g:render template="/layouts/showErrors" model="[instance: reviewInstance]" />

            <div class="container">

                <g:render template="reviewFilterButtons" />


                    <g:form url="[resource:reviewInstance, action:'update']" method="PUT" >
                        <g:hiddenField name="version" value="${reviewInstance?.version}" />
                        <g:hiddenField name="repository" value="${repository?.id}" />
                        <fieldset class="form">
                            <g:render template="form"/>
                        </fieldset>
                        <fieldset class="buttons">
                            <g:actionSubmit class="btn btn-default btn-success" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" />
                            <a href="<g:createLink action="show" id="${reviewInstance.ident()}" />" class="btn btn-link" onclick="closeForm();"><g:message code="cancel" /></a>
                        </fieldset>
                    </g:form>
                </div>
                <p />
            </div>
        </div>
	</body>
</html>
