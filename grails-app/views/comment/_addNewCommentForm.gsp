<%@ page import="com.revizor.CommentType" %>

<%-- 
	The form to add new comment
--%>
<div class="form-group" style="width: 800px;">

	<g:if test="${commentType == CommentType.LINE_OF_CODE.name()}">
		<h3><g:message code="review.comments.form.line.code" default="Your comment to this line of code" /></h3>
	</g:if>
	<g:else>
		<h3><g:message code="review.comments.form.title" default="Your comment to this review" /></h3>
	</g:else>

	<textarea data-provide="markdown" name="text" class="form-control" rows="5"></textarea>
</div>

<div class="form-group" style="width: 800px;">
	
	<g:if test="${commentType == CommentType.LINE_OF_CODE.name()}">
		<g:hiddenField name="commit" value="${commit}"/>
		<g:hiddenField name="fileName" value="${fileName}"/>
	</g:if>

	<g:hiddenField name="redirectTo" value="review/${reviewId}"/>
	<g:hiddenField name="author.id" value="${session.user.id}"/>
	<g:hiddenField name="review.id" value="${reviewId}"/>
	<g:hiddenField name="type" value="${commentType}"/>
	<button type="button" name="submit-comment-review-btn" class="btn btn-default" onclick="createNewComment();">${message(code: 'review.comments.post', default: 'Post the comment')}</button>
</div>