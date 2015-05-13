<%@ defaultCodec="none" %>

<%-- 
	The body of only one notification 
--%>
<div class="media notification<g:if test="${forMe}"> notification-to-me</g:if> <g:if test="${isUnread}"> unread unread-new</g:if>" style="width: 100%;">
  
  <a class="pull-left" href="${createLink(controller:'user', action:'show', id: mainActor?.ident(), absolute: true)}">
      <g:render template="/user/userAvatar" model="[
              'user' : mainActor,
              'cssClass': 'avatar img-rounded media-object',
              'absoluteUrl': true]" />
  </a>

  <div class="media-body">
        ${message.decodeHTML()}
        <br />
        
        <g:if test="${details}">
            <blockquote><cmt:highlightUsername><emoji:toHtml size="22">${details}</emoji:toHtml></cmt:highlightUsername></blockquote>
        </g:if>
  </div>
</div>