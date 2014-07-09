<%@ page import="com.revizor.CommentType" %>

<%-- 
	The body of only one comment 
--%>

<div class="media comment-container panel" style="width: 800px;">
  <a class="pull-left" href="${createLink(controller:'user', action:'show', id:comment.author?.ident())}">
    <img height="64" width="64" class="avatar img-rounded media-object" src="${createLink(controller:'user', action:'avatar_image', id:comment.author?.ident())}" />
  </a>
  <div class="media-body">
    <h4 class="media-heading">${comment.author?.username}</h4>
    
    <markdown:renderHtml>${comment.text.decodeHTML()}</markdown:renderHtml>

    <button class="btn btn-default btn-xs btn-comment-reply"><span class="glyphicon glyphicon-share-alt"></span> Reply</button>
  </div>
</div>