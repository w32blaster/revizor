<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="settingsMain"/>
		<title>Welcome to Grails</title>
	</head>
	<body>

       <h2><g:message code="settings.label" /></h2>

        <div class="panel panel-default">
            <div class="panel-heading">Application Status</div>

            <table class="table table-striped">
                <tr><td>Revizor version:</td><td> <g:meta name="app.version"/></td></tr>
                <tr><td>Working folder:</td><td> <samp>${com.revizor.utils.Constants.LOCAL_REPO_PATH + File.separator}</samp> </td></tr>
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
