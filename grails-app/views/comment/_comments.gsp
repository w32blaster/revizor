<%@ page import="com.revizor.CommentType" %>
<%@ page import="com.revizor.Comment" %>

<%-- 
	The set of comments with the form to post new one
--%>
<g:set var="comments" value="${Comment.findAllByReviewAndType(reviewInstance, CommentType.REVIEW)}" />

<h1><g:message code="review.comments.header" default="Comments" /></h1>
		
<g:each var="comment" in="${comments}">
    <g:render template="/comment/comment" model="['comment' : comment]" />
</g:each>

<g:form id='add-new-comment-form-id' role="form" url="[controller: 'comment', action:'save', format: 'html']">
    
    <g:javascript>
        function createNewComment() {
            $( "#add-new-comment-form-id" ).submit();
        }
    </g:javascript>
    
    <g:render template="/comment/addNewCommentForm" model="['reviewId' : reviewInstance.id, 'commentType' : commentType.name()]" />
    
</g:form>