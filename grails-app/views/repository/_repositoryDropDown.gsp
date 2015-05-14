<%--

    Renders dropdown list with all the repositories.

--%>
<ul class="dropdown-menu" role="menu">
    <g:each var="repo" in="${com.revizor.Repository.list()}">
        <g:if test="${repo.ident() == session.activeRepository}" >
            <li class="dropdown-header">
                <g:render template="/repository/repositoryLogo" model="['repo': repo]"/>
                ${repo.title}
            </li>
        </g:if>
        <g:else>
            <li>
                <a href="${createLink(controller: 'repository', action: 'dashboard', id: repo.ident())}">
                    <g:render template="/repository/repositoryLogo" model="['repo': repo]" />
                    ${repo.title}
                </a>
            </li>
        </g:else>
    </g:each>

    <li role="presentation" class="divider"></li>
    <li role="presentation"><a role="menuitem" tabindex="-1" href="${createLink(controller: 'repository', action: 'create')}">
        <span class="glyphicon glyphicon-plus"></span> <g:message code="repository.create.new" />...</a></li>
</ul>