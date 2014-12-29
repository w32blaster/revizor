<%@ page import="com.revizor.IssueTracker" %>



<div class="form-group ${hasErrors(bean: issueTrackerInstance, field: 'issueKeyPattern', 'error')} ">
	<label class="col-lg-3" for="issueKeyPattern">
		<g:message code="issueTracker.issueKeyPattern.label" default="Issue Key Pattern" />
		
	</label>
	<div class="col-lg-9">
		<g:textField  name="issueKeyPattern" value="${issueTrackerInstance?.issueKeyPattern ? issueTrackerInstance?.issueKeyPattern : "[A-Z]+-{1}\\d+" }"/>
	</div>
</div>



<div class="form-group ${hasErrors(bean: issueTrackerInstance, field: 'title', 'error')} ">
	<label class="col-lg-3" for="title">
		<g:message code="issueTracker.title.label" default="Title" />
		
	</label>
	<div class="col-lg-9">
		<g:textField name="title" value="${issueTrackerInstance?.title}"/>
	</div>
</div>

<div class="form-group ${hasErrors(bean: issueTrackerInstance, field: 'url', 'error')} ">
	<label class="col-lg-3" for="url">
		<g:message code="issueTracker.url.label" default="Url" />

		<a href="${grailsApplication.config.links.wiki.issueTracker}" target="_blank" title="<g:message code="wiki.clone.repository" />">
			<span class="glyphicon glyphicon-info-sign"></span>
		</a>
	</label>
	<div class="col-lg-9">
		<g:textField name="url" value="${issueTrackerInstance?.url}" placeholder="https://"/>
	</div>
</div>

<div class="form-group ${hasErrors(bean: issueTrackerInstance, field: 'type', 'error')} required">
	<label class="col-lg-3" for="type">
		<g:message code="issueTracker.type.label" default="Type" />
		<span class="required-indicator">*</span>
	</label>
	<div class="col-lg-9">
		<g:select name="type" from="${com.revizor.IssueTrackerType?.values()}" keys="${com.revizor.IssueTrackerType.values()*.name()}" required="" value="${issueTrackerInstance?.type?.name()}" />
	</div>
</div>



<div class="form-group ${hasErrors(bean: issueTrackerInstance, field: 'username', 'error')} ">
	<label class="col-lg-3" for="username">
		<g:message code="issueTracker.username.label" default="Username" />
	</label>
	<div class="col-lg-9">
		<g:textField name="username" value="${issueTrackerInstance?.username}"/>
	</div>
</div>

<div class="form-group ${hasErrors(bean: issueTrackerInstance, field: 'password', 'error')} ">
	<label class="col-lg-3" for="password">
		<g:message code="issueTracker.password.label" default="Password" />
	</label>
	<div class="col-lg-9">
		<g:passwordField name="password" value="${issueTrackerInstance?.password}"/>
	</div>
</div>