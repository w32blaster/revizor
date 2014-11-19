<%@ page import="com.revizor.CommentType" %>

<%-- 
	The body of only one comment 
--%>

<g:set var="indentPx" value="${(indent+1) * 15}" />
<g:if test="${indent > 0}">
    <div class='pull-left reply-indent' style='width: ${indentPx}px;'>
    <g:each in="${(0..indent).toList()}" var="count" >
      <span>&#9679;</span>
    </g:each>
    </div>
</g:if>

<div class="media comment-container well" style="width: ${800 - indentPx}px;">
  <a class="pull-left" href="${createLink(controller:'user', action:'show', id:comment.author?.ident())}">
      <g:render template="/user/userAvatar" model="['user' : comment.author, 'cssClass': 'avatar img-rounded media-object']" />
  </a>
  <div class="media-body">
    <h4 class="media-heading">${comment.author?.username}</h4>

    <cmt:highlightUsername>
        <emoji:toHtml size="22">
            <markdown:renderHtml>${comment.text.decodeHTML()}</markdown:renderHtml>
        </emoji:toHtml>
    </cmt:highlightUsername>

    <button class="btn btn-default btn-xs btn-comment-reply" onclick="showForm('new-reply-to-${comment.id}-form', null, null, 'replies-container-${comment.id}-id', '${comment.id}', ${indent+1});">
      <span class="glyphicon glyphicon-share-alt"></span>
      <g:message code="comment.reply"  />
    </button>

  </div>
</div>

<g:if test="${indent > 0}">
  <div class='clearfix'></div>
</g:if>