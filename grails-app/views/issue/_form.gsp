<%@ page import="com.revizor.Issue" %>



<div class="form-group ${hasErrors(bean: issueInstance, field: 'key', 'error')} ">
	<label for="key" class="col-lg-3">
		<g:message code="issue.key.label" default="Key" />
		
	</label>
	<div class="col-lg-9">
		<g:textField name="key" value="${issueInstance?.key}"/>
	</div>
</div>

<div class="form-group ${hasErrors(bean: issueInstance, field: 'tracker', 'error')} required">
	<label for="tracker" class="col-lg-3">
		<g:message code="issue.tracker.label" default="Tracker" />
		<span class="required-indicator">*</span>
	</label>
	<div class="col-lg-9">
		<g:select id="tracker" name="tracker.id" from="${com.revizor.IssueTracker.list()}" optionKey="id" required="" value="${issueInstance?.tracker?.id}" class="many-to-one"/>
	</div>
</div>

