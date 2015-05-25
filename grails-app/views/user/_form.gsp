<%@ page import="com.revizor.User" %>


<div class="form-group ${hasErrors(bean: userInstance, field: 'email', 'error')} ">
	<label for="email" class="col-lg-3">
		<g:message code="user.email.label" default="Email" />
		
	</label>
	<div class="col-lg-9">
		<g:field type="email" name="email" class="form-control" value="${userInstance?.email}" placeholder="user@example.com"/>
	</div>
</div>

<div class="form-group ${hasErrors(bean: userInstance, field: 'username', 'error')} ">
	<label for="username" class="col-lg-3">
		<g:message code="user.username.label" default="username" />
		
	</label>
	<div class="col-lg-9">
		<g:textField class="form-control" name="username" value="${userInstance?.username}"/>
	</div>
</div>

<div class="form-group ${hasErrors(bean: userInstance, field: 'password', 'error')} ">
	<label for="password" class="col-lg-3">
		<g:message code="user.password.label" default="Password" />
	</label>
	<div class="col-lg-9">
		<g:passwordField class="form-control" name="password" value="" autocomplete="off" />
	</div>
</div>

<div class="form-group ${hasErrors(bean: userInstance, field: 'position', 'error')} ">
	<label for="position" class="col-lg-3">
		<g:message code="user.position" default="position" />
	</label>
	<div class="col-lg-9">
		<g:textField  class="form-control" name="position"  value="${userInstance?.position}" placeholder="Developer"/>
	</div>
</div>

<%--
<div class="form-group ${hasErrors(bean: userInstance, field: 'comments', 'error')} ">
	<label for="comments" class="col-lg-3">
		<g:message code="user.comments.label" default="Comments" id="comments" />
	</label>

<ul class="one-to-many">
<g:each in="${userInstance?.comments?}" var="c">
    <li><g:link controller="comment" action="show" id="${c.id}">${c?.encodeAsHTML()}</g:link></li>
</g:each>
<li class="add">
<g:link controller="comment" action="create" params="['user.id': userInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'comment.label', default: 'Comment')])}</g:link>
</li>
</ul>

</div>

<div class="form-group ${hasErrors(bean: userInstance, field: 'repositories', 'error')} ">
	<label for="repositories" class="col-lg-3">
		<g:message code="user.repositories.label" default="Repositories" />
	</label>
	<div class="col-lg-9">
		<g:select  class="form-control" name="repositories" from="${com.revizor.Repository.list()}" multiple="multiple" optionKey="id" size="5" value="${userInstance?.repositories*.id}" class="many-to-many"/>
	</div>
</div>
--%>

<div class="form-group ${hasErrors(bean: userInstance, field: 'role', 'error')} required">
	<label for="role" class="col-lg-3">
		<g:message code="user.role.label" default="Role" />
		<span class="required-indicator">*</span>
	</label>
	<div class="col-lg-9">
		<g:if test="${session.user.role == com.revizor.Role.ADMIN}">
			<g:select  class="form-control selectpicker" data-style="btn-primary" name="role" from="${com.revizor.Role?.values()}" keys="${com.revizor.Role.values()*.name()}" required="" value="${userInstance?.role?.name()}" />
		</g:if>
		<g:else>
			<span class="label label-primary">${userInstance?.role?.name()}</span>
		</g:else>
	</div>
</div>


<r:script>

    $('.selectpicker').selectpicker();

</r:script>

