
<%@ page import="com.revizor.Comment" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'comment.label', default: 'Comment')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<nav class="navbar navbar-default" role="navigation">
			<ul class="nav navbar-nav">
				<li><a href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li class="active"><g:link action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</nav>
		
		<div id="show-comment" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list comment">
			
				<g:if test="${commentInstance?.author}">
				<li class="form-group">
					<span id="author-label" class="property-label"><g:message code="comment.author.label" default="Author" /></span>
					
						<span class="property-value" aria-labelledby="author-label"><g:link controller="user" action="show" id="${commentInstance?.author?.id}">${commentInstance?.author?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<g:if test="${commentInstance?.text}">
				<li class="form-group">
					<span id="text-label" class="property-label"><g:message code="comment.text.label" default="Text" /></span>
					
						<span class="property-value" aria-labelledby="text-label"><g:fieldValue bean="${commentInstance}" field="text"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${commentInstance?.commit}">
				<li class="form-group">
					<span id="commit-label" class="property-label"><g:message code="comment.commit.label" default="Commit" /></span>
					
						<span class="property-value" aria-labelledby="commit-label"><g:fieldValue bean="${commentInstance}" field="commit"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${commentInstance?.fileName}">
				<li class="form-group">
					<span id="fileName-label" class="property-label"><g:message code="comment.fileName.label" default="File Name" /></span>
					
						<span class="property-value" aria-labelledby="fileName-label"><g:fieldValue bean="${commentInstance}" field="fileName"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${commentInstance?.lineOfCode}">
				<li class="form-group">
					<span id="lineOfCode-label" class="property-label"><g:message code="comment.lineOfCode.label" default="Line Of Code" /></span>
					
						<span class="property-value" aria-labelledby="lineOfCode-label"><g:fieldValue bean="${commentInstance}" field="lineOfCode"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${commentInstance?.review}">
				<li class="form-group">
					<span id="review-label" class="property-label"><g:message code="comment.review.label" default="Review" /></span>
					
						<span class="property-value" aria-labelledby="review-label"><g:link controller="review" action="show" id="${commentInstance?.review?.id}">${commentInstance?.review?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<g:if test="${commentInstance?.type}">
				<li class="form-group">
					<span id="type-label" class="property-label"><g:message code="comment.type.label" default="Type" /></span>
					
						<span class="property-value" aria-labelledby="type-label"><g:fieldValue bean="${commentInstance}" field="type"/></span>
					
				</li>
				</g:if>

				<g:if test="${commentInstance?.typeOfLine}">
				<li class="form-group">
					<span id="type-of-line-label" class="property-label"><g:message code="comment.typeOfLine.label" default="Type of line" /></span>
					
						<span class="property-value" aria-labelledby="type-of-line-label"><g:fieldValue bean="${commentInstance}" field="typeOfLine"/></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form url="[resource:commentInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${commentInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
