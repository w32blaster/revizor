<%@ page import="com.revizor.Issue" %>



<div class="form-group ${hasErrors(bean: issueInstance, field: 'key', 'error')} ">
	<label for="key">
		<g:message code="issue.key.label" default="Key" />
		
	</label>
	<g:textField name="key" value="${issueInstance?.key}"/>

</div>

<div class="form-group ${hasErrors(bean: issueInstance, field: 'tracker', 'error')} required">
	<label for="tracker">
		<g:message code="issue.tracker.label" default="Tracker" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="tracker" name="tracker.id" from="${com.revizor.IssueTracker.list()}" optionKey="id" required="" value="${issueInstance?.tracker?.id}" class="many-to-one"/>

</div>

