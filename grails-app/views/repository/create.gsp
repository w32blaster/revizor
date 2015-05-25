<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="settingsMain">
		<g:set var="entityName" value="${message(code: 'repository.label', default: 'Repository')}" />
		<title><g:message code="default.create.label" args="[entityName]" /></title>
	</head>
	<body>

			<h2><g:message code="default.create.label" args="[entityName]" /></h2>

			<g:render template="/layouts/flashMessage" />

			<g:render template="/layouts/showErrors" model="[instance: repositoryInstance]" />

			<g:render template="/layouts/actionButton" />


			<div class="form-container">
				<g:form url="[resource:repositoryInstance, action:'save']" class="form-horizontal" >
					<fieldset class="form">
                        <g:hiddenField name="hasImage" value="false"/>
						<g:render template="form"/>
					</fieldset>
					<fieldset class="buttons">
						<button id="clone-repo-btn" name="create" type="submit" class="btn btn-default btn-primary">
							<span class="glyphicon glyphicon-plus"></span>
							${message(code: 'default.button.create.label', default: 'Create')}
						</button>
					</fieldset>
				</g:form>
			</div>

			<r:script>

				(function($) {

					var fnCheckFolder = function() {

						var $folderNameValue = $('#folderName').val();
						if($folderNameValue) {
							$.get("${createLink(controller: 'repository', action: 'checkFolderExistence')}/" + encodeURIComponent( $folderNameValue ) )
							.done(function(isExisting) {
								if (isExisting === "1") {
									$('#folderNameWrapperID').addClass("has-error");

									$folderNameField.attr("data-content", "${message(code: 'folder.exists.explanation')}"
											.replace("{0}", "${com.revizor.utils.Constants.LOCAL_REPO_PATH + File.separator}" + $folderNameValue));
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
					};

					$('#clone-repo-btn').on('click', function () {
						var $btn = $(this).button('toggle');
						$btn.attr('disabled','disabled');
						$btn.html("<span class='glyphicon glyphicon-refresh glyphicon-refresh-animate'></span>");
					});

					var folderNameInput = $("#folderName")
					$("#title").keyup(function() {
						var value = $(this).val();
						value = value.replace(/[^a-zA-Z\w]/g, "").toLowerCase();
						folderNameInput.val(value);
						fnCheckFolder();
					});

					$('#folderName').keyup(fnCheckFolder);

				})(jQuery);

			</r:script>

	</body>
</html>
