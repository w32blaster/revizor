<%@ page import="com.revizor.IssueTracker" %>



<div class="form-group ${hasErrors(bean: issueTrackerInstance, field: 'issueKeyPattern', 'error')} ">
	<label class="control-label" for="issueKeyPattern">
		<g:message code="issueTracker.issueKeyPattern.label" default="Issue Key Pattern" />
		
	</label>
	<g:textField  name="issueKeyPattern" value="${issueTrackerInstance?.issueKeyPattern ? issueTrackerInstance?.issueKeyPattern : "[A-Z]+-{1}\\d+" }"/>

</div>



<div class="form-group ${hasErrors(bean: issueTrackerInstance, field: 'title', 'error')} ">
	<label class="control-label" for="title">
		<g:message code="issueTracker.title.label" default="Title" />
		
	</label>
	<g:textField name="title" value="${issueTrackerInstance?.title}"/>

</div>

<div class="form-group ${hasErrors(bean: issueTrackerInstance, field: 'url', 'error')} ">
	<label class="control-label" for="url">
		<g:message code="issueTracker.url.label" default="Url" />

	</label>
	<g:textField name="url" value="${issueTrackerInstance?.url}" placeholder="https://"/>

</div>

<div class="form-group ${hasErrors(bean: issueTrackerInstance, field: 'type', 'error')} required">
	<label class="control-label" for="type">
		<g:message code="issueTracker.type.label" default="Type" />
		<span class="required-indicator">*</span>
	</label>
	<g:select name="type" from="${com.revizor.IssueTrackerType?.values()}" keys="${com.revizor.IssueTrackerType.values()*.name()}" required="" value="${issueTrackerInstance?.type?.name()}" />

</div>



<div class="form-group ${hasErrors(bean: issueTrackerInstance, field: 'username', 'error')} ">
	<label class="control-label" for="username">
		<g:message code="issueTracker.username.label" default="Username" />

	</label>
	<g:textField name="username" value="${issueTrackerInstance?.username}"/>

</div>

<div class="form-group ${hasErrors(bean: issueTrackerInstance, field: 'password', 'error')} ">
	<label class="control-label" for="password">
		<g:message code="issueTracker.password.label" default="Password" />

	</label>
	<g:passwordField name="password" value="${issueTrackerInstance?.password}"/>

</div>