<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title>Welcome to Grails</title>
	</head>
	<body>

    <div class="container">


        <div class="panel panel-default">
            <div class="panel-body">

                <h2>Settings</h2>

                <div class="btn-group">
                    <g:link class="btn btn-default btn-primary" url="${createLink(uri: '/')}">
                        <span class="glyphicon glyphicon-home"></span>
                        <g:message code="default.home.label" default="Home"/>
                    </g:link>

                    <g:link class="btn btn-default btn-primary" controller="comment">
                        <span class="glyphicon glyphicon-comment"></span>
                        <g:message code="review.comments.header" default="Comments" />
                    </g:link>

                    <g:link class="btn btn-default btn-primary" controller="repository">
                        <span class="glyphicon glyphicon-cloud"></span>
                        <g:message code="repository.header" default="Repositories" />
                    </g:link>

                    <g:link class="btn btn-default btn-primary" controller="review">
                        <span class="glyphicon glyphicon-pencil"></span>
                        <g:message code="reviews.header" default="Reviews" />
                    </g:link>

                    <g:link class="btn btn-default btn-primary" controller="user">
                        <span class="glyphicon glyphicon-user"></span>
                        <g:message code="users.header" default="Users" />
                    </g:link>

                </div>

            </div>
        </div>

        <div class="panel panel-default">
            <div class="panel-heading">Application Status</div>

            <table class="table table-striped">
                <tr><td>App version:</td><td> <g:meta name="app.version"/></td></tr>
                <tr><td>Grails version:</td><td> <g:meta name="app.grails.version"/></td></tr>
                <tr><td>Groovy version:</td><td> ${GroovySystem.getVersion()}</td></tr>
                <tr><td>JVM version:</td><td> ${System.getProperty('java.version')}</td></tr>
                <tr><td>Reloading active:</td><td> ${grails.util.Environment.reloadingAgentEnabled}</td></tr>
                <tr><td>Controllers:</td><td> ${grailsApplication.controllerClasses.size()}</td></tr>
                <tr><td>Domains:</td><td> ${grailsApplication.domainClasses.size()}</td></tr>
                <tr><td>Services:</td><td> ${grailsApplication.serviceClasses.size()}</td></tr>
                <tr><td>Tag Libraries:</td><td> ${grailsApplication.tagLibClasses.size()}</td></tr>
            </table>
        </div>

        <div class="panel panel-default">
            <div class="panel-heading">Installed Plugins</div>

        <table class="table table-striped">
            <g:each var="plugin" in="${applicationContext.getBean('pluginManager').allPlugins}">
                <tr><td>${plugin.name}</td><td>${plugin.version}</td></tr>
            </g:each>
        </table>

	</div>


	</body>
</html>
