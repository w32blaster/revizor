<%@ page import="com.revizor.CommentType" %>

<%-- 
	The body of only one comment 
--%>

<div class="media comment-container well" style="width: 800px; margin-left: ${10 * indent}px;">
  <a class="pull-left" href="${createLink(controller:'user', action:'show', id:comment.author?.ident())}">
      <g:render template="/user/userAvatar" model="['user' : comment.author, 'cssClass': 'avatar img-rounded media-object']" />
  </a>
  <div class="media-body">
    <h4 class="media-heading">${comment.author?.username}</h4>
    
    <emoji:toHtml size="22">
        <markdown:renderHtml>${comment.text.decodeHTML()}</markdown:renderHtml>
    </emoji:toHtml>

    <button class="btn btn-default btn-xs btn-comment-reply" onclick="showForm(this, 'new-reply-to-${comment.id}-form', null, null, 'replies-container-${comment.id}-id');">
      <span class="glyphicon glyphicon-share-alt"></span>
      <g:message code="comment.reply"  />
    </button>

  </div>
</div>