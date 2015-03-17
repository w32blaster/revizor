<%@ page import="com.revizor.RepositoryType; com.revizor.ReviewFilter" %>
<%@ page import="com.revizor.CommentsFilter" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title><g:message code="dashboard.title" /></title>
		<link rel="stylesheet" href="${resource(dir: 'css', file: 'dashboard.css')}" type="text/css">
	</head>
	<body>
		
		<g:set var="notificationContainerID" value="notification-feed-container" />
		<div id="${notificationContainerID}" class="col-md-4 full-height">
			
			<g:render template="/notification/notificationFeed" />

		</div>
		<div class="col-md-8 full-height">
			<div class="row">
				<div id="repository-header" class="col-md-12">

					<!-- Repository logo -->
                    <g:if test="${selectedRepo.isHasImage()}">
					    <img height="64" width="64" class="avatar img-rounded pull-left" src="${createLink(controller: 'repository', action: 'logo_image', id: selectedRepo?.ident())}" alt="${session.user.username}" />
                    </g:if>
                    <g:elseif test="${selectedRepo.type == RepositoryType.GIT}">
                        <img height="64" width="64" class="avatar img-rounded pull-left" src="${resource(dir: 'images/default-icons', file: 'git.png')}" alt="Default icon for a GIT repository" />
                    </g:elseif>
                    <g:elseif test="${selectedRepo.type == RepositoryType.MERCURIAL}">
                        <img height="64" width="64" class="avatar img-rounded pull-left" src="${resource(dir: 'images/default-icons', file: 'mercurial.png')}" alt="Default icon for a Mercurial repository" />
                    </g:elseif>

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
								<span class="glyphicon glyphicon-plus"></span> <g:message code="repository.create.new" />...</a></li>
						  </ul>
						</div>

						<g:render template="buttonUpdateRepository" model="['repoId': selectedRepo.ident() ]" />

					</div>

				</div>
			</div>
			<div class="row">
				
				<div id="repo-tree-id" class="col-md-12">

					<sc:buildFlatListofCommits repo="${selectedRepo}" />

				</div>

			</div>
		</div>	

	    <r:script>
			(function($) {
				$('.gpopover').popover();
			})(jQuery);
		</r:script>
	</body>
</html>
