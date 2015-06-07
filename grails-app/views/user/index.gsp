
<%@ page import="com.revizor.User" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="settingsMain">
		<g:set var="entityName" value="${message(code: 'user.label', default: 'User')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>


			<h1><g:message code="default.list.label" args="[entityName]" /></h1>

			<g:render template="/layouts/flashMessage" />

			<g:render template="/layouts/actionButton" />

                    <g:if test="${ldapError}">
                        <div class="alert alert-danger">
                            <strong>Error</strong> ${raw(ldapError)}
                        </div>
                    </g:if>

                    <h3><g:message code="registered.in.revizor" /></h3>

					<table class="table">
					    <thead>
							<tr>
                                <th class="col-md-1"> </th>
                                <th class="col-md-3">${message(code: 'user.email.label', default: 'Email')} </th>
                                <th class="col-md-3">${message(code: 'user.username.label', default: 'username')}</th>
                                <th class="col-md-3">${message(code: 'user.role.label', default: 'Role')}</th>
                                <th class="col-md-2"> </th>
							</tr>
						</thead>
						<tbody>

						<g:each in="${userInstanceList}" status="i" var="userInstance">
							<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
                                <g:render template="userTableRow" model="[userInstance: userInstance]" />
							</tr>
						</g:each>


						<g:if test="${isLdapUsed}">

                            <tr>
                                <td colspan="5">
                                    <h3><g:message code="ldap.users" /></h3>
                                </td>
                            </tr>

                            <g:each in="${ldapUsers}" status="i" var="ldapUser">
                                <g:set var="isActivated" value="${mapUsersByEmail.containsKey(ldapUser.email)}" />

                                <tr class="${(i % 2) == 0 ? 'even' : 'odd'} ${(isActivated) ? '' : 'inactive'}">

                                    <g:if test="${isActivated}">
                                        <g:render template="userTableRow" model="[userInstance: mapUsersByEmail.get(ldapUser.email)]" />
                                    </g:if>
                                    <g:else>

                                        <td> </td>
                                        <td>${ldapUser.email}</td>
                                        <td>${ldapUser.sn}</td>
                                        <td> </td>
                                        <td>
                                            <g:if test="${sendInvites}">
                                                <a href="#" class="btn btn-link" title="${message(code: 'invite.user', args: [ ldapUser.email ])}"
                                                    onclick="sendInvite('${URLEncoder.encode(ldapUser.email, 'UTF-8')}'); return false;">
                                                    <span class="glyphicon glyphicon-send"></span>
                                                    <g:message code="invite.label" />
                                                </a>
                                            </g:if>
                                        </td>

                                    </g:else>

                                </tr>
                            </g:each>
						</g:if>

						</tbody>
					</table>

<g:if test="${sendInvites}">
    <r:script>

        function sendInvite(email) {
            $.get("${g.createLink(controller: 'user', action: 'send_invite')}?email=" + email)
                .done(function() {
                    toastr.success("<g:message code="invitation.was.sent" />")
                })
                .fail(function() {
                    toastr.error('<g:message code="invitation.was.not.sent.error" />')
                });
        };

   </r:script>
</g:if>

	</body>
</html>
