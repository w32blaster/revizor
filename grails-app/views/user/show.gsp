
<%@ page import="com.revizor.CommentsFilter; com.revizor.User" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="settingsMain">
		<g:set var="entityName" value="${message(code: 'user.label', default: 'User')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>


				<h1><g:message code="default.show.label" args="[entityName]" /></h1>

				<g:render template="/layouts/flashMessage" />

				<g:render template="/layouts/actionButton" />

                <g:render template="/user/userAvatar" model="['user' : userInstance, 'cssClass': 'avatar img-rounded']" />

			<ol class="property-list user">


				<g:if test="${userInstance?.email}">
				<li class="form-group">
					<span id="email-label" class="property-label"><g:message code="user.email.label" default="Email" /></span>
					
						<span class="property-value" aria-labelledby="email-label"><g:fieldValue bean="${userInstance}" field="email"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${userInstance?.username}">
				<li class="form-group">
					<span id="username-label" class="property-label"><g:message code="user.username.label" default="username" /></span>
					
						<span class="property-value" aria-labelledby="username-label"><g:fieldValue bean="${userInstance}" field="username"/></span>
					
				</li>
				</g:if>

				<g:if test="${userInstance?.position}">
					<li class="form-group">
						<span id="position-label" class="property-label"><g:message code="user.position" default="Position" /></span>

						<span class="property-value" aria-labelledby="position-label"><g:fieldValue bean="${userInstance}" field="position"/></span>

					</li>
				</g:if>


		<g:if test="${userInstance?.comments}">
				<li class="form-group">
					<span id="comments-label" class="property-label"><g:message code="user.comments.label" default="Comments" /></span>

                    <g:link controller="comment" action="index" params="[filter: com.revizor.CommentsFilter.BY_AUTHOR, author: userInstance.ident()]" >
                        <g:message code="review.comments.header" />
                    </g:link>

				</li>
				</g:if>

				<g:if test="${userInstance?.reviewsAsAuthor}">
				<li class="form-group">
					<span id="reviewsAsAuthor-label" class="property-label"><g:message code="user.reviewsAsAuthor.label" default="Reviews" /></span>

                        <ul>
						<g:each in="${userInstance.reviewsAsAuthor}" var="r">
                            <li>
                                <g:link controller="review" action="show" id="${r.id}">
                                    ${r.ident()}: ${r?.title}
                                </g:link>
                            </li>
						</g:each>
                        </ul>
				</li>
				</g:if>
			


				<g:if test="${userInstance?.role}">
				<li class="form-group">
					<span id="role-label" class="property-label"><g:message code="user.role.label" default="Role" /></span>
					
						<span class="property-value" aria-labelledby="role-label"><g:fieldValue bean="${userInstance}" field="role"/></span>
					
				</li>
				</g:if>
			
			</ol>

				<g:if test="${userInstance?.aliases}">
					<table class="table table-striped table-hover ">
						<thead>
						<tr>
							<th>#</th>
							<th><g:message code="user.aliases" /></th>
						</tr>
						</thead>
						<tbody>
						<g:each in="${userInstance.aliases.sort({ it.id })}" var="alias">
							<tr>
								<td>${alias.id}</td>
								<td>${alias.aliasEmail}</td>
							</tr>
						</g:each>

						</tbody>
					</table>
				</g:if>


				<g:form url="[resource:userInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="btn btn-primary" action="edit" resource="${userInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="btn btn-default" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>


	</body>
</html>
