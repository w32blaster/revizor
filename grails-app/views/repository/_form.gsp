<%@ page import="com.revizor.utils.Constants; com.revizor.Repository" %>


<div class="form-group ${hasErrors(bean: repositoryInstance, field: 'url', 'error')} ">

    <div class="col-lg-3">
        <label for="url">
            <g:message code="repository.url.label" default="Url" />
        </label>

        <button id="url-help" type="button" class="btn btn-link floating" data-html="true" data-toggle="popover" data-trigger="focus" title="<g:message code="help.repo.url.title" />"
                data-content="${message(code: "help.repo.url.text", args: [ grailsApplication.config.links.wiki.clone ])}">
            <span class="glyphicon glyphicon-info-sign"></span>
        </button>
    </div>

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

<div id="folderNameWrapperID" class="form-group ${hasErrors(bean: repositoryInstance, field: 'folderName', 'error')} ">
	<label for="folderName" class="col-lg-3 ">
		<g:message code="repository.folderName.label" default="Folder Name" />
	</label>
	<div class="col-lg-9">

        <div class="input-group">

            <span class="input-group-addon">
                <span class="glyphicon glyphicon-folder-open" aria-hidden="true"></span>
                <samp>${com.revizor.utils.Constants.LOCAL_REPO_PATH + File.separator}</samp>
            </span>

            <g:if test="${actionName == "edit"}">
                <g:textField name="folderName"  class="form-control" value="${repositoryInstance?.folderName}" disabled="disabled" />
            </g:if>
            <g:else>
                <g:textField class="form-control" name="folderName" pattern="${repositoryInstance.constraints.folderName.matches}" value="${repositoryInstance?.folderName}"
                             data-trigger="manual" data-animation="true" data-placement="bottom" data-html="true"
                             title="${message(code: 'folder.exists')}" />
            </g:else>
        </div>
	</div>
</div>

<!-- User and password section -->
<button class="btn btn-link" type="button" data-toggle="collapse" data-target="#collapseUserAndPassword" aria-expanded="false" aria-controls="collapseExample">
    <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
    <g:message code="add.user.and.password" />
</button>
<div class="collapse" id="collapseUserAndPassword">
    <div class="well">

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

    </div>
</div>

<div class="form-group ${hasErrors(bean: repositoryInstance, field: 'type', 'error')} required">
    <label for="type" class="col-lg-3">
        <g:message code="repository.type.label" default="Type" />
        <span class="required-indicator">*</span>
    </label>
    <div class="col-lg-9">

        <g:select class="selectpicker" name="type" data-style="btn-primary"
                  from="${com.revizor.RepositoryType?.values()}" keys="${com.revizor.RepositoryType.values()*.name()}" required="" value="${repositoryInstance?.type?.name()}" />

    </div>
</div>

<r:script>
	(function($) {

        $('#password-tooltip-id').tooltip();
        $('.selectpicker').selectpicker();
        $('#url-help').popover();


	})(jQuery);




</r:script>

