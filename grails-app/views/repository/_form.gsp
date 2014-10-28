<%@ page import="com.revizor.Repository" %>



<div class="form-group ${hasErrors(bean: repositoryInstance, field: 'url', 'error')} ">
	<label for="url">
		<g:message code="repository.url.label" default="Url" />
		
	</label>
	<g:textField class="form-control" name="url" value="${repositoryInstance?.url}"/>

</div>

<div class="form-group ${hasErrors(bean: repositoryInstance, field: 'title', 'error')} ">
	<label for="title">
		<g:message code="repository.title.label" default="Title" />
		
	</label>
	<g:textField class="form-control" name="title" value="${repositoryInstance?.title}"/>

</div>

<div class="form-group ${hasErrors(bean: repositoryInstance, field: 'folderName', 'error')} ">
	<label for="folderName">
		<g:message code="repository.folderName.label" default="Folder Name" />
		
	</label>
	<g:textField class="form-control" name="folderName" pattern="${repositoryInstance.constraints.folderName.matches}" value="${repositoryInstance?.folderName}"/>

</div>

<p class="text-primary">
    <g:message code="repository.only.for.not.anonymous.label" default="Left it empty for anonymous repositories" />
</p>

<div class="form-group ${hasErrors(bean: repositoryInstance, field: 'username', 'error')} ">
    <label for="username">
        <g:message code="repository.username.label" default="User name" />
    </label>
    <g:textField class="form-control" name="username" pattern="${repositoryInstance.constraints.folderName.matches}" value="${repositoryInstance?.username}"/>

</div>

<div class="form-group ${hasErrors(bean: repositoryInstance, field: 'password', 'error')} ">
    <label for="password">
        <g:message code="repository.password.label" default="Password" />
    </label>
    <g:passwordField class="form-control" name="password" pattern="${repositoryInstance.constraints.password.matches}" value="${repositoryInstance?.password}"/>
</div>


<div class="form-group ${hasErrors(bean: repositoryInstance, field: 'members', 'error')} ">
	<label for="members">
		<g:message code="repository.members.label" default="Members" />
	</label>
	
<ul class="one-to-many">
<g:each in="${repositoryInstance?.members?}" var="r">
    <li><g:link controller="user" action="show" id="${r.id}">${r?.encodeAsHTML()}</g:link></li>
</g:each>
<li class="add">
<g:link controller="user" action="create" params="['repository.id': repositoryInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'review.label', default: 'Member')])}</g:link>
</li>
</ul>


</div>

<div class="form-group ${hasErrors(bean: repositoryInstance, field: 'type', 'error')} required">
	<label for="type">
		<g:message code="repository.type.label" default="Type" />
		<span class="required-indicator">*</span>
	</label>
	<g:select name="type" from="${com.revizor.RepositoryType?.values()}" keys="${com.revizor.RepositoryType.values()*.name()}" required="" value="${repositoryInstance?.type?.name()}" />

</div>

