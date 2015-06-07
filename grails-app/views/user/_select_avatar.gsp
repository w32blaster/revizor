<%--
	Form to upload image (avatar)
--%>
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