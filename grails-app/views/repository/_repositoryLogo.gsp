<%--
    Renders the logo for repository

--%>
<g:if test="${repo.hasImage}">
    <img height="16" width="16" class="avatar img-rounded" src="${createLink(controller:'repository', action: 'logo_image', id: repo.ident())}" />
</g:if>
<g:else>
    <span class="pseudo-img">&nbsp;</span>
</g:else>