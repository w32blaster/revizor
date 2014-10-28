<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'repository.label', default: 'Repository')}" />
		<title><g:message code="default.create.label" args="[entityName]" /></title>
	</head>
	<body>

		<div id="create-repository" class="content scaffold-create" role="main">
			<h2><g:message code="default.create.label" args="[entityName]" /></h2>
			<g:if test="${flash.message}">
                <div class="alert alert-info">${flash.message}</div>
			</g:if>
			<g:hasErrors bean="${repositoryInstance}">
			    <div class="errors" role="alert">
                    <ul>
				        <g:eachError bean="${repositoryInstance}" var="error">
				            <li><g:message error="${error}"/></li>
				        </g:eachError>
                    </ul>
			    </div>
			</g:hasErrors>

            <div class="container">
                <div class="btn-group">

                    <g:link url="${createLink(uri: '/')}" class="btn btn-default btn-primary">
                        <span class="glyphicon glyphicon-home"></span>
                        <g:message code="default.home.label" />
                    </g:link>

                    <g:link action="list" class="btn btn-default btn-primary">
                        <span class="glyphicon glyphicon-inbox"></span>
                        <g:message code="default.list.label" args="[entityName]" />
                    </g:link>

                    <g:link action="create" class="btn btn-default btn-primary active">
                        <span class="glyphicon glyphicon-plus"></span>
                        <g:message code="default.new.label" args="[entityName]" />
                    </g:link>
                </div>


				<g:form url="[resource:repositoryInstance, action:'save']" >
					<fieldset class="form">
                        <g:hiddenField name="hasImage" value="false"/>
						<g:render template="form"/>
					</fieldset>
					<fieldset class="buttons">
						<g:submitButton name="create" class="btn btn-default btn-primary" value="${message(code: 'default.button.create.label', default: 'Create')}" />
					</fieldset>
				</g:form>
           </div>
		</div>
	</body>
</html>
