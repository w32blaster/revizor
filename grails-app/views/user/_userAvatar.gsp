<%--
	Shows user avatar or a default image with a smile
--%>
<g:set var="size" value="${size ? size : 64}" />
<g:set var="cssClass" value="${cssClass ? cssClass : 'avatar img-rounded'}" />
<g:if test="${user?.isHasImage()}">
    <img height="${size}" width="${size}" class="${cssClass}"
         src="${createLink(controller:'user', action:'avatar_image', id: user?.ident())}" alt="${user.username}" title="${user.username}" />
</g:if>
<g:else>
    <img height="${size}" width="${size}" class="${cssClass}"
         src="${resource(dir: 'images/default-icons', file: 'big_smile.png')}"
         alt="${message(code:"default.icon.for.an.user")}" title="${message(code:"default.icon.for.an.user")}" />
</g:else>