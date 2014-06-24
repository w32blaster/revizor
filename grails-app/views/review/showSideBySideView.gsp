
<%@ page import="com.revizor.Review" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'review.label', default: 'Review')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<nav class="navbar navbar-default" role="navigation">
			<ul class="nav navbar-nav">
				<li><a href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li class="active"><g:link action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
			</ul>
		</nav>
		
		<div id="show-review" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			
			
			
				<g:if test="${reviewInstance?.title}">
				<li class="form-group">
					<span id="title-label" class="property-label"><g:message code="review.title.label" default="Title" /></span>
					
						<span class="property-value" aria-labelledby="title-label"><g:fieldValue bean="${reviewInstance}" field="title"/></span>
					
				</li>
				</g:if>
			
			
			

			
				<g:if test="${reviewInstance?.comments}">
				<li class="form-group">
					<span id="comments-label" class="property-label"><g:message code="review.comments.label" default="Comments" /></span>
					
						<g:each in="${reviewInstance.comments}" var="c">
						<span class="property-value" aria-labelledby="comments-label"><g:link controller="comment" action="show" id="${c.id}">${c?.encodeAsHTML()}</g:link></span>
						</g:each>
					
				</li>
				</g:if>
			
				<g:if test="${reviewInstance?.commits}">
				<li class="form-group">
					<span id="commits-label" class="property-label"><g:message code="review.commits.label" default="Commits" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${reviewInstance?.description}">
				<li class="form-group">
					<span id="description-label" class="property-label"><g:message code="review.description.label" default="Description" /></span>
					
						<span class="property-value" aria-labelledby="description-label"><g:fieldValue bean="${reviewInstance}" field="description"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${reviewInstance?.reviewers}">
				<li class="form-group">
					<span id="reviewers-label" class="property-label"><g:message code="review.reviewers.label" default="Reviewers" /></span>
					
						<g:each in="${reviewInstance.reviewers}" var="r">
						<span class="property-value" aria-labelledby="reviewers-label"><g:link controller="user" action="show" id="${r.id}">${r?.encodeAsHTML()}</g:link></span>
						</g:each>
					
				</li>
				</g:if>

				<li class="form-group">
					<sc:showDiffForCommit repo="${reviewInstance.repository}" commitID="${reviewInstance.commits[0]}" />
				</li>
			
			</ol>
			<g:form url="[resource:reviewInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${reviewInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
