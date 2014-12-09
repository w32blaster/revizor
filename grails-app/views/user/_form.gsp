<%@ page import="com.revizor.User" %>


<div class="form-group ${hasErrors(bean: userInstance, field: 'email', 'error')} ">
	<label for="email">
		<g:message code="user.email.label" default="Email" />
		
	</label>
	<g:field type="email" name="email" class="form-control" value="${userInstance?.email}"/>

</div>

<div class="form-group ${hasErrors(bean: userInstance, field: 'username', 'error')} ">
	<label for="username">
		<g:message code="user.username.label" default="username" />
		
	</label>
	<g:textField class="form-control" name="username" value="${userInstance?.username}"/>

</div>

<div class="form-group ${hasErrors(bean: userInstance, field: 'password', 'error')} ">
	<label for="password">
		<g:message code="user.password.label" default="Password" />
	</label>
	<g:passwordField class="form-control" name="password" value="" />
</div>

<div class="form-group ${hasErrors(bean: userInstance, field: 'position', 'error')} ">
	<label for="position">
		<g:message code="user.position" default="position" />
	</label>
	<g:textField  class="form-control" name="position"  value="${userInstance?.position}" placeholder="Developer"/>

</div>

<div class="form-group ${hasErrors(bean: userInstance, field: 'comments', 'error')} ">
	<label for="comments">
		<g:message code="user.comments.label" default="Comments" />
		
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
	<label for="repositories">
		<g:message code="user.repositories.label" default="Repositories" />
	</label>
	<g:select  class="form-control" name="repositories" from="${com.revizor.Repository.list()}" multiple="multiple" optionKey="id" size="5" value="${userInstance?.repositories*.id}" class="many-to-many"/>

</div>

<div class="form-group ${hasErrors(bean: userInstance, field: 'role', 'error')} required">
	<label for="role">
		<g:message code="user.role.label" default="Role" />
		<span class="required-indicator">*</span>
	</label>
	<g:select  class="form-control" name="role" from="${com.revizor.Role?.values()}" keys="${com.revizor.Role.values()*.name()}" required="" value="${userInstance?.role?.name()}" />

</div>

