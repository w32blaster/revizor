<%@ page import="com.revizor.Repository" %>



<div class="form-group ${hasErrors(bean: repositoryInstance, field: 'url', 'error')} ">
	<label for="url" class="col-lg-3">
		<g:message code="repository.url.label" default="Url" />
		<a href="${grailsApplication.config.links.wiki.clone}" target="_blank" title="<g:message code="wiki.clone.repository" />">
			<span class="glyphicon glyphicon-info-sign"></span>
		</a>
	</label>
	<div class="col-lg-9">
		<g:textField class="form-control col-lg-3" name="url" value="${repositoryInstance?.url}"/>

	</div>
</div>

<div class="form-group ${hasErrors(bean: repositoryInstance, field: 'title', 'error')} ">
	<label for="title" class="col-lg-3">
		<g:message code="repository.title.label" default="Title" />
		
	</label>
	<div class="col-lg-9">
		<g:textField class="form-control" name="title" value="${repositoryInstance?.title}"/>
	</div>
</div>

<div class="form-group ${hasErrors(bean: repositoryInstance, field: 'folderName', 'error')} ">
	<label for="folderName" class="col-lg-3">
		<g:message code="repository.folderName.label" default="Folder Name" />
		
	</label>
	<div class="col-lg-9">
		<g:textField class="form-control" name="folderName" pattern="${repositoryInstance.constraints.folderName.matches}" value="${repositoryInstance?.folderName}"/>
	</div>
</div>

<p class="text-primary">
    <g:message code="repository.only.for.not.anonymous.label" default="Left it empty for anonymous repositories" />
</p>

<div class="form-group ${hasErrors(bean: repositoryInstance, field: 'username', 'error')} ">
    <label for="username" class="col-lg-3">
        <g:message code="repository.username.label" default="User name" />
    </label>
	<div class="col-lg-9">
		<g:textField class="form-control" name="username" pattern="${repositoryInstance.constraints.folderName.matches}" value="${repositoryInstance?.username}"/>
	</div>
</div>

<div class="form-group ${hasErrors(bean: repositoryInstance, field: 'password', 'error')} ">
    <label for="password" class="col-lg-3">
        <g:message code="repository.password.label" default="Password" />
		<span id="password-tooltip-id" class="glyphicon glyphicon-exclamation-sign text-danger"
			  data-toggle="tooltip"
			  data-placement="top"
			  title="<g:message code="password.stored.as.plain.text"/>" ></span>
    </label>
	<div class="col-lg-9">
		<g:passwordField class="form-control" name="password" pattern="${repositoryInstance.constraints.password.matches}" value="${repositoryInstance?.password}"/>
	</div>
</div>

<div class="form-group ${hasErrors(bean: repositoryInstance, field: 'type', 'error')} required">
	<label for="type" class="col-lg-3">
		<g:message code="repository.type.label" default="Type" />
		<span class="required-indicator">*</span>
	</label>
	<div class="col-lg-9">
		<g:select name="type" from="${com.revizor.RepositoryType?.values()}" keys="${com.revizor.RepositoryType.values()*.name()}" required="" value="${repositoryInstance?.type?.name()}" />
	</div>
</div>


<r:script>
	(function($) {
		$('#password-tooltip-id').tooltip();
	})(jQuery);
</r:script>

