<%@ page import="com.revizor.Repository" %>
<li class="dropdown">
    <a href="${createLink(controller: 'repository', action: 'dashboard', id: session.activeRepository)}" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">
        ${message(code: "dashboard.breadcrumb", args: [com.revizor.Repository.get(session.activeRepository).title] )}
        <span class="caret"></span>
    </a>
    <ul class="dropdown-menu" role="menu">
        <g:each var="repo" in="${com.revizor.Repository.list()}">
            <g:if test="${repo.ident() == session.activeRepository}" >
                <li>${repo.title}</li>
            </g:if>
            <g:else>
                <li><a href="${createLink(controller: 'repository', action: 'dashboard', id: repo.ident())}">${repo.title}</a></li>
            </g:else>
        </g:each>

        <li role="presentation" class="divider"></li>
        <li role="presentation"><a role="menuitem" tabindex="-1" href="${createLink(controller: 'repository', action: 'create')}">
            <span class="glyphicon glyphicon-plus"></span> <g:message code="repository.create.new" />...</a></li>
    </ul>
</li>