<%@ page import="com.revizor.Chat" %>



<div class="form-group ${hasErrors(bean: chatInstance, field: 'url', 'error')} ">
	<label class="col-lg-3" for="url">
		<g:message code="chat.url.label" default="Url" />
        <span class="required-indicator">*</span>

        <a href="${grailsApplication.config.links.wiki.chats}" target="_blank" title="<g:message code="wiki.chat.integrations" />">
            <span class="glyphicon glyphicon-info-sign"></span>
        </a>

	</label>
    <div class="col-lg-9">
        <g:textField name="url" value="${chatInstance?.url}"/>
    </div>
</div>

<div class="form-group ${hasErrors(bean: chatInstance, field: 'name', 'error')} ">
    <label class="col-lg-3" for="username">
        <g:message code="chat.username.label" default="Name" />
        <span class="required-indicator">*</span>
    </label>
    <div class="col-lg-9">
        <g:textField name="name" value="${chatInstance?.name}"/>
    </div>
</div>

<div class="form-group ${hasErrors(bean: chatInstance, field: 'username', 'error')} ">
	<label class="col-lg-3" for="username">
		<g:message code="chat.username.label" default="Username" />
	</label>
    <div class="col-lg-9">
        <g:textField name="username" value="${chatInstance?.username}"/>
    </div>
</div>

<div class="form-group ${hasErrors(bean: chatInstance, field: 'password', 'error')} ">
	<label class="col-lg-3" for="password">
		<g:message code="chat.password.label" default="Password" />
	</label>
    <div class="col-lg-9">
        <g:textField name="password" value="${chatInstance?.password}"/>
    </div>
</div>

<div class="form-group ${hasErrors(bean: chatInstance, field: 'type', 'error')} required">
	<label class="col-lg-3"  for="type">
		<g:message code="chat.type.label" default="Type" />
		<span class="required-indicator">*</span>
	</label>
    <div class="col-lg-9">
        <g:select name="type" from="${com.revizor.ChatType?.values()}" keys="${com.revizor.ChatType.values()*.name()}" required="" value="${chatInstance?.type?.name()}" />
    </div>
</div>

