<%@ page import="com.revizor.Repository" %>
<li class="dropdown menu-top-item">

    <g:set var="selectedRepo" value="${com.revizor.Repository.get(session.activeRepository).title}" />
    <g:set var="dashboardActive" value="${(actionName == "dashboard" && controllerName == "repository") ? " active " : ""}" />
    <a id="repo-dropdown-label" href="${createLink(controller: 'repository', action: 'dashboard', id: session.activeRepository)}"
        class="menu-link ${dashboardActive}" title='${message(code: 'dashboard.breadcrumb', args: [selectedRepo])}'>
            <span class="glyphicon glyphicon-dashboard"></span>
            ${selectedRepo}
    </a>

    <a class="dropdown-toggle menu-link ${dashboardActive}" data-toggle="dropdown" role="button" aria-expanded="false">
        <span class="caret"></span>
    </a>

    <g:render template="/repository/repositoryDropDown" />
</li>