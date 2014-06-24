<%@ page import="com.revizor.Review" %>



<div class="form-group ${hasErrors(bean: reviewInstance, field: 'title', 'error')} ">
	<label for="title">
		<g:message code="review.title.label" default="Title" />
		
	</label>
	<g:textField class="form-control" name="title" value="${reviewInstance?.title}"/>

</div>

<div class="form-group ${hasErrors(bean: reviewInstance, field: 'author', 'error')} required">
	<label for="author">
		<g:message code="review.author.label" default="Author" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="author" name="author.id" from="${com.revizor.User.list()}" optionKey="id" required="" value="${reviewInstance?.author?.id}" class="many-to-one"/>

</div>

<div class="form-group ${hasErrors(bean: reviewInstance, field: 'author', 'error')} required">
	<sc:selectCommitsForReview repo="${repository}" />
</div>

<div class="form-group ${hasErrors(bean: reviewInstance, field: 'comments', 'error')} ">
	<label for="comments">
		<g:message code="review.comments.label" default="Comments" />
		
	</label>
	
<ul class="one-to-many">
<g:each in="${reviewInstance?.comments?}" var="c">
    <li><g:link controller="comment" action="show" id="${c.id}">${c?.encodeAsHTML()}</g:link></li>
</g:each>
<li class="add">
<g:link controller="comment" action="create" params="['review.id': reviewInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'comment.label', default: 'Comment')])}</g:link>
</li>
</ul>


</div>

<div class="form-group ${hasErrors(bean: reviewInstance, field: 'commits', 'error')} required">
	<label for="commits">
		<g:message code="review.commits.label" default="Commits" />
		<span class="required-indicator">*</span>
	</label>
	

</div>

<div class="form-group ${hasErrors(bean: reviewInstance, field: 'description', 'error')} ">
	<label for="description">
		<g:message code="review.description.label" default="Description" />
		
	</label>
	<g:textField class="form-control" name="description" value="${reviewInstance?.description}"/>

</div>

<div class="form-group ${hasErrors(bean: reviewInstance, field: 'reviewers', 'error')} ">
	<label for="reviewers">
		<g:message code="review.reviewers.label" default="Reviewers" />
		
	</label>
	<g:select name="reviewers" from="${com.revizor.User.list()}" multiple="multiple" optionKey="id" size="5" value="${reviewInstance?.reviewers*.id}" class="many-to-many"/>

</div>




