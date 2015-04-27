<%@ page import="com.revizor.utils.Constants; com.revizor.Repository" %>


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

<div id="folderNameWrapperID" class="form-group ${hasErrors(bean: repositoryInstance, field: 'folderName', 'error')} ">
	<label for="folderName" class="col-lg-3 ">
		<g:message code="repository.folderName.label" default="Folder Name" />
	</label>
	<div class="col-lg-9">
		<g:textField class="form-control" name="folderName" pattern="${repositoryInstance.constraints.folderName.matches}" value="${repositoryInstance?.folderName}"
                     data-trigger="manual" data-animation="true" data-placement="bottom" data-html="true"
                     title="${message(code: 'folder.exists')}" />
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

        <g:if test="${actionName == "create"}">
            var $folderNameField = $('#folderName');
            $folderNameField.focusout(function() {
                var $folderNameValue = $(this).val();
                if($folderNameValue) {
                    $.get("${createLink(controller: 'repository', action: 'checkFolderExistence')}/" + encodeURIComponent( $folderNameValue ) )
                        .done(function(isExisting) {
                            if (isExisting === "1") {
                                $('#folderNameWrapperID').addClass("has-error");

                                $folderNameField.popover({
                                    content : "${message(code: 'folder.exists.explanation')}"
                                        .replace("{0}", "${com.revizor.utils.Constants.LOCAL_REPO_PATH + File.separator}" + $folderNameValue)
                                });
                                $folderNameField.popover('show');
                                $('#clone-repo-btn').attr('disabled','disabled');
                            }
                            else {
                                $('#folderNameWrapperID').removeClass("has-error");
                                $folderNameField.popover('hide');
                                $('#clone-repo-btn').attr('disabled',false);
                            }
                        })
                        .fail(function() {
                            toastr.error('Error occures while the request to check the folder existence.')
                        });
                    }
                });
        </g:if>

	})(jQuery);




</r:script>

