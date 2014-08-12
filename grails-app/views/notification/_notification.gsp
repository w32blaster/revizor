<%@ defaultCodec="none" %>

<%-- 
	The body of only one notification 
--%>
<div class="media notification" style="width: 100%;">
  
  <a class="pull-left" href="${createLink(controller:'user', action:'show', id: mainActor?.ident())}">
    <img height="64" width="64" class="avatar img-rounded media-object" src="${createLink(controller:'user', action:'avatar_image', id:mainActor.ident())}" />
  </a>

  <div class="media-body">
        ${message.decodeHTML()} 
        <br />
        
        <g:if test="${details}">
            <blockquote><emoji:toHtml size="22">${details}</emoji:toHtml></blockquote>
        </g:if>
  </div>
</div>