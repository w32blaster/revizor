<%@ page import="com.revizor.Comment" %>



<div class="form-group ${hasErrors(bean: commentInstance, field: 'author', 'error')} required">
	<label for="author">
		<g:message code="comment.author.label" default="Author" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="author" name="author.id" from="${com.revizor.User.list()}" optionKey="id" required="" value="${commentInstance?.author?.id}" class="many-to-one"/>

</div>

<div class="form-group ${hasErrors(bean: commentInstance, field: 'text', 'error')} required">
	<label for="text">
		<g:message code="comment.text.label" default="Text" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField class="form-control" name="text" required="" value="${commentInstance?.text}"/>

</div>

<div class="form-group ${hasErrors(bean: commentInstance, field: 'commit', 'error')} ">
	<label for="commit">
		<g:message code="comment.commit.label" default="Commit" />
		
	</label>
	<g:textField class="form-control" name="commit" value="${commentInstance?.commit}"/>

</div>

<div class="form-group ${hasErrors(bean: commentInstance, field: 'fileName', 'error')} ">
	<label for="fileName">
		<g:message code="comment.fileName.label" default="File Name" />
		
	</label>
	<g:textField class="form-control" name="fileName" value="${commentInstance?.fileName}"/>

</div>

<div class="form-group ${hasErrors(bean: commentInstance, field: 'lineOfCode', 'error')} required">
	<label for="lineOfCode">
		<g:message code="comment.lineOfCode.label" default="Line Of Code" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="lineOfCode" type="number" value="${commentInstance.lineOfCode}" required=""/>

</div>

<div class="form-group ${hasErrors(bean: commentInstance, field: 'review', 'error')} required">
	<label for="review">
		<g:message code="comment.review.label" default="Review" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="review" name="review.id" from="${com.revizor.Review.list()}" optionKey="id" required="" value="${commentInstance?.review?.id}" class="many-to-one"/>

</div>

<div class="form-group ${hasErrors(bean: commentInstance, field: 'type', 'error')} required">
	<label for="type">
		<g:message code="comment.type.label" default="Type" />
		<span class="required-indicator">*</span>
	</label>
	<g:select name="type" from="${com.revizor.CommentType?.values()}" keys="${com.revizor.CommentType.values()*.name()}" required="" value="${commentInstance?.type?.name()}" />

<div class="form-group ${hasErrors(bean: commentInstance, field: 'typeOfLine', 'error')} required">
	<label for="typeOfLine">
		<g:message code="comment.typeOfLine.label" default="Type of a line" />
	</label>
	<g:select name="typeOfLine" from="${com.revizor.LineType?.values()}" keys="${com.revizor.LineType.values()*.name()}" required="" value="${commentInstance?.typeOfLine?.name()}" />


</div>

