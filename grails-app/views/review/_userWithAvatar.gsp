<%--
    Makes a small widget for a given user: just avatar, name and job position
--%>
<div class="user-cell">
    <g:render template="/user/userAvatar" model="['user' : user, 'size': 32]" />

    <div class="user-name-pos">
        <div>${user.username}</div>
        <div class="position">${user.position}</div>
    </div>

</div>