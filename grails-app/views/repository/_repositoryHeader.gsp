<g:if test="${isUnread}">
    <span class="label label-danger new-label" title="${message(code:"unread.review")}">N</span>
</g:if>


    <!-- Repository logo -->
    <g:if test="${repo.isHasImage()}">
        <img height="${size}" width="${size}" class="avatar img-rounded pull-left repo-logo" src="${createLink(controller: 'repository', action: 'logo_image', id: repo?.ident())}" alt="${session.user.username}" />
    </g:if>
    <g:elseif test="${repo.type == com.revizor.RepositoryType.GIT}">
        <img height="${size}" width="${size}" class="avatar img-rounded pull-left repo-logo" src="${resource(dir: 'images/default-icons', file: 'git.png')}" alt="Default icon for a GIT repository" />
    </g:elseif>
    <g:elseif test="${repo.type == com.revizor.RepositoryType.MERCURIAL}">
        <img height="${size}" width="${size}" class="avatar img-rounded pull-left repo-logo" src="${resource(dir: 'images/default-icons', file: 'mercurial.png')}" alt="Default icon for a Mercurial repository" />
    </g:elseif>

    <b>
        <a href="${createLink(controller: 'repository', action: 'dashboard', id: repo.ident())}">
            ${repo.title}
        </a>
    </b></br>

    <i>
        <hs:maskPassword>
            ${repo.url}
        </hs:maskPassword>
    </i>

