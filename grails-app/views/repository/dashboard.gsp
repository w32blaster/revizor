<%@ page import="com.revizor.RepositoryType; com.revizor.ReviewFilter" %>
<%@ page import="com.revizor.CommentsFilter" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main" />
		<title><g:message code="dashboard.title" /></title>
		<link rel="stylesheet" href="${resource(dir: 'css', file: 'dashboard.css')}" type="text/css">

	</head>
	<body>

        <div id="content-container">
            <g:set var="notificationContainerID" value="notification-feed-container" />
            <div id="${notificationContainerID}" class="col-md-4 full-height">

                <g:render template="/notification/notificationFeed" />

            </div>
            <div class="col-md-8 full-height">
                <div class="row">
                    <div id="repository-header" class="col-md-12">

                        <!-- Repository logo -->
                        <g:render template="repositoryHeader" model="[size: 64, repo: selectedRepo]" />

                        <div id="switch-to-btn" class="btn-group">

                            <!-- "Switch to..." button -->
                            <div class="btn-group">
                                <button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown">
                                    <g:message code="dashboard.switch.to" default="Switch to..." />
                                    <span class="caret"></span>
                                </button>
                                <g:render template="/repository/repositoryDropDown" />
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
        </div>

	    <r:script>
			(function($) {
				$('.gpopover').popover();
			})(jQuery);
		</r:script>
	</body>
</html>
