<%@ page import="com.revizor.CommentType" %>

<%-- 
	The body of only one comment 
--%>

<div class="media comment-container panel" style="width: 800px;">
  <a class="pull-left" href="${createLink(controller:'user', action:'show', id:comment.author?.ident())}">
      <g:render template="/user/userAvatar" model="['user' : comment.author, 'cssClass': 'avatar img-rounded media-object']" />
  </a>
  <div class="media-body">
    <h4 class="media-heading">${comment.author?.username}</h4>
    
    <emoji:toHtml size="22">
        <markdown:renderHtml>${comment.text.decodeHTML()}</markdown:renderHtml>
    </emoji:toHtml>

    <button class="btn btn-default btn-xs btn-comment-reply"><span class="glyphicon glyphicon-share-alt"></span> Reply</button>
  </div>
</div>