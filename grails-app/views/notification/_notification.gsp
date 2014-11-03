<%@ defaultCodec="none" %>

<%-- 
	The body of only one notification 
--%>
<div class="media notification" style="width: 100%;">
  
  <a class="pull-left" href="${createLink(controller:'user', action:'show', id: mainActor?.ident())}">
      <g:render template="/user/userAvatar" model="['user' : mainActor, 'cssClass': 'avatar img-rounded media-object']" />
  </a>

  <div class="media-body">
        ${message.decodeHTML()} 
        <br />
        
        <g:if test="${details}">
            <blockquote><emoji:toHtml size="22">${details}</emoji:toHtml></blockquote>
        </g:if>
  </div>
</div>