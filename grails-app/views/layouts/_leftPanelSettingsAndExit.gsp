<div id="current-account-block" role="account-container">

    <g:render template="/user/userAvatar" model="['user' : session.user, 'size': 32]" />

    <a href="${createLink(controller: 'settings')}" class="menu-link">
        <span class="glyphicon glyphicon-cog"></span>
        <g:message code="settings.label" />
    </a>

    <g:link class="menu-link" controller="login" action="doLogout" title="${message(code: 'default.log.out')}">
        <span class="glyphicon glyphicon-off"></span>
        <g:message code="exit.label" />
    </g:link>

</div>