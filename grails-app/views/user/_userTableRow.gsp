<%--

    One row of users table.

--%>
<td>
    <g:if test="${userInstance?.image}">
        <img class="avatar" width="16" height="16" src="${createLink(controller: 'user', action: 'avatar_image', id: userInstance?.ident())}" />
    </g:if>
</td>

<td><g:link action="show" id="${userInstance.id}">${fieldValue(bean: userInstance, field: "email")}</g:link></td>

<td>${fieldValue(bean: userInstance, field: "username")}</td>

<td>${fieldValue(bean: userInstance, field: "role")}</td>

<td></td>