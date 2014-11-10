<%--
	Form to upload image (avatar)
--%>
<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title"><g:message code="user.avatar.upload.title" default="Upload" /></h3>
	</div>
	<div class="panel-body">
		<fieldset>
			<g:uploadForm action="upload" id="${id}">
				<label for="avatar"><g:message code="user.avatar.upload" default="Image (16K)" /></label>
				<input type="file" name="image" id="avatar" />

				<div style="font-size: 0.8em; margin: 1.0em;">
					<g:message code="user.avatar.upload.description" default="username" />
				</div>

				<input type="submit" class="btn btn-default" value="Upload" />
			</g:uploadForm>
		</fieldset>

	</div>
</div>