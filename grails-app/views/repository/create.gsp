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


			<div class="input-group">
				<g:form url="[resource:repositoryInstance, action:'save']" >
					<fieldset class="form">
                        <g:hiddenField name="hasImage" value="false"/>
						<g:render template="form"/>
					</fieldset>
					<fieldset class="buttons">
						<button id="clone-repo-btn" name="create" type="submit" class="btn btn-default btn-primary">
							${message(code: 'default.button.create.label', default: 'Create')}
						</button>
					</fieldset>
				</g:form>
			</div>

			<r:script>
				$('#clone-repo-btn').on('click', function () {
					var $btn = $(this).button('toggle');
					$btn.attr('disabled','disabled');
					$btn.html("<span class='glyphicon glyphicon-refresh glyphicon-refresh-animate'></span>");
				});
			</r:script>

	</body>
</html>
