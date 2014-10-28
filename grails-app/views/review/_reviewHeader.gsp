
<h2>${reviewInstance?.title}</h2>

<div class="row">

    <g:if test="${reviewInstance?.author}">

        <g:render template="/user/userAvatar" model="['user' : reviewInstance?.author]" />
        ${reviewInstance?.author?.username.encodeAsHTML()}

    </g:if>
</div>