<%@ page import="com.revizor.CommentType" %>

<%-- 
	The form to add new comment
--%>
<div class="form-group add-new-comment-form">

	<g:if test="${replyToComment}">
		<b>
			<g:message code="review.comments.form.reply" default="Reply to" />
            <g:render template="/comment/mention" model="['user': replyToComment.author]" />
		</b>
	</g:if>
	<g:elseif test="${commentType == CommentType.LINE_OF_CODE.name()}">
		<b><g:message code="review.comments.form.line.code" default="Your comment to this line of code" /></b>
	</g:elseif>
	<g:else>
		<b><g:message code="review.comments.form.title" default="Your comment to this review" /></b>
	</g:else>

	<textarea data-provide="markdown" name="text" class="form-control" rows="5"></textarea>
</div>

<div class="form-group add-new-comment-form" style="width: 800px;">
	<g:hiddenField name="author.id" value="${session.user.id}"/>
	<g:hiddenField name="review.id" value="${reviewId}"/>
	<g:hiddenField name="type" value="${commentType}"/>
	<g:hiddenField name="indent" value="${indent}"/>

	<g:if test="${commentType == CommentType.LINE_OF_CODE.name()}">
		<g:hiddenField name="commit" value="${commit}"/>
		<g:hiddenField name="fileName" value="${fileName}"/>
	</g:if>
	<g:if test="${replyToComment}">
		<g:hiddenField name="replyTo.id" value="${replyToComment.id}"/>
	</g:if>
	<g:if test="${lineNo}">
		<g:hiddenField name="lineOfCode" value="${lineNo}" id="lineOfCode" />
		<g:hiddenField name="typeOfLine" value="${lineType}" id="typeOfLine" />
	</g:if>

	<button type="button" name="submit-comment-review-btn" class="btn btn-primary" onclick="createNewComment();">
		<span class="glyphicon glyphicon-send" aria-hidden="true"></span>
		${message(code: 'review.comments.post', default: 'Post the comment')}
	</button>
	<a href="#" class="btn btn-link" onclick="closeForm();"><g:message code="cancel" /></a>
</div>