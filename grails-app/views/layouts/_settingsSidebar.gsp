<ul class="nav nav-pills nav-stacked" role="tablist">

    <li>
        <g:link url="${createLink(uri: '/')}">
            <span class="glyphicon glyphicon-home"></span>
            <g:message code="default.home.label" default="Home"/>
        </g:link>
    </li>

    <g:if test="${session.user.role == com.revizor.Role.USER}">
        <li<g:if test="${controllerName == "user"}"> class="active"</g:if>>
            <g:link controller="user" action="show" id="${session.user.ident()}">
                <span class="glyphicon glyphicon-user"></span>
                <g:message code="my.account" default="My Account" />
            </g:link>
        </li>
    </g:if>

    <g:if test="${session.user.role == com.revizor.Role.ADMIN}">

        <li<g:if test="${request.forwardURI.endsWith("/settings")}"> class="active"</g:if>>
        <g:link url="${createLink(uri: '/settings')}">
            <span class="glyphicon glyphicon-cog"></span>
            <g:message code="settings.label" default="Settings"/>
        </g:link>
        </li>

        <li<g:if test="${controllerName == "repository"}"> class="active"</g:if>>
            <g:link controller="repository" action="list">
                <span class="glyphicon glyphicon-cloud"></span>
                <g:message code="repository.header" default="Repositories" />
            </g:link>
        </li>

        <li<g:if test="${controllerName == "user"}"> class="active"</g:if>>
            <g:link controller="user">
                <span class="glyphicon glyphicon-user"></span>
                <g:message code="users.header" default="Users" />
            </g:link>
        </li>

        <li<g:if test="${controllerName == "issueTracker"}"> class="active"</g:if>>
            <g:link controller="issueTracker">
                <span class="glyphicon glyphicon-tags"></span>
                <g:message code="issue.tracker.label" default="Issue Tracker" />
            </g:link>
        </li>

        <li<g:if test="${controllerName == "issue"}"> class="active"</g:if>>
            <g:link controller="issue">
                <span class="glyphicon glyphicon-tag"></span>
                <g:message code="issue.tickets.label" default="Issue Tickets" />
            </g:link>
        </li>

        <li<g:if test="${controllerName == "chat"}"> class="active"</g:if>>
            <g:link controller="chat">
                <span class="glyphicon glyphicon-bullhorn"></span>
                <g:message code="chats.label" default="Chat" />
            </g:link>
        </li>

    </g:if>
</ul>
