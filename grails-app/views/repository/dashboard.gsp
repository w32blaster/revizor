<%@ page import="com.revizor.ReviewFilter" %>
<%@ page import="com.revizor.CommentsFilter" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title>Welcome to Grails</title>
		<link rel="stylesheet" href="${resource(dir: 'css', file: 'dashboard.css')}" type="text/css">
	</head>
	<body>
		
		<g:set var="notificationContainerID" value="notification-feed-container" />
		<div id="${notificationContainerID}" class="col-md-6 full-height">
			
			<g:render template="/notification/notificationFeed" />

		</div>
		<div class="col-md-6 full-height">
			<div class="row">
				<div id="repository-header" class="col-md-12">

					<!-- Repository logo -->
					<img height="64" width="64" class="avatar img-rounded pull-left" src="${createLink(controller: 'repository', action: 'logo_image', id: selectedRepo?.ident())}" alt="${session.user.username}" />

					<h3 class="pull-left">${selectedRepo.title}</h3>

					<div id="switch-to-btn" class="btn-group">

						<!-- Reviews -->
						<g:link controller="review" action="index" params="[filter: ReviewFilter.ONLY_MINE]" class="btn btn-default btn-primary">
							<span class="glyphicon glyphicon-pencil"></span> Reviews
						</g:link>

						<!-- Comments -->
						<g:link controller="comment" action="index" params="[filter: CommentsFilter.ONLY_MINE]" class="btn btn-default btn-primary">
							<span class="glyphicon glyphicon-comment"></span> Comments
						</g:link>

						<!-- "Switch to..." button -->
						<div class="btn-group">
						  <button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown">
						  	<g:message code="dashboard.switch.to" default="Switch to..." />
						  	<span class="caret"></span>
						  </button>
						  <ul class="dropdown-menu" role="menu">
						  	<g:each var="repo" in="${repos}">
						    	<li><a href="${createLink(controller: 'repository', action: 'dashboard', id: repo.ident())}">${repo.title}</a></li>
						    </g:each>

						    <li role="presentation" class="divider"></li>
    						<li role="presentation"><a role="menuitem" tabindex="-1" href="${createLink(controller: 'repository', action: 'create')}">
    							<g:message code="repository.create.new" /></a></li>
						  </ul>
						</div>

					</div>

				</div>
			</div>
			<div class="row">
				
				<div id="repo-tree-id" class="col-md-12">
					<sc:buildFlatListofCommits repo="${selectedRepo}" />

				</div>

			</div>
		</div>	


	</body>
</html>
