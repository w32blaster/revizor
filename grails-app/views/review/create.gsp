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

                <div class="btn-group">

                    <!-- My reviews, where I am as an author -->
                    <g:link action="index" params="[filter: ReviewFilter.ONLY_MINE]" class="btn btn-default btn-primary">
                        <span class="glyphicon glyphicon-pencil"></span>
                        <g:message code="reviews.only.mine" default="My reviews" />
                    </g:link>

                    <!-- My invitations and reviews where I am a reviewer -->
                    <g:link action="index" params="[filter: ReviewFilter.WHERE_I_AM_REVIEWER]" class="btn btn-default btn-primary">
                        <span class="glyphicon glyphicon-thumbs-up"></span>
                        <g:message code="reviews.where.i.reviewer" default="My inspections" />
                    </g:link>

                    <!-- Finished (archived) reviews -->
                    <g:link action="index" params="[filter: ReviewFilter.ARCHIVED]" class="btn btn-default btn-primary">
                        <span class="glyphicon glyphicon-inbox"></span>
                        <g:message code="reviews.archived" default="My reviews" />
                    </g:link>

                <!-- All reviews -->
                    <g:link action="index" params="[filter: ReviewFilter.ALL]" class="btn btn-default btn-primary">
                        <g:message code="reviews.all" default="All reviews" />
                    </g:link>

                </div>




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
            </div>
	</body>
</html>
