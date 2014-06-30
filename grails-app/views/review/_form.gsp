<%@ page import="com.revizor.Review" %>



<div class="form-group ${hasErrors(bean: reviewInstance, field: 'title', 'error')} ">
	<label for="title">
		<g:message code="review.title.label" default="Title" />
		
	</label>
	<g:textField class="form-control" name="title" value="${reviewInstance?.title}"/>

</div>

<div class="form-group">
    <label for="author">
        <g:message code="review.author.label" default="Author" />
    </label>
    <g:render template="reviewer" model="['reviewer' : session.user]" />
</div>


<div class="form-group required listOfCommits">
    <label for="commits">
        <g:message code="review.commits.label" default="Commits" />
    </label>
    <sc:selectCommitsForReview repo="${repository}" selected="${params.selected}" />
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




