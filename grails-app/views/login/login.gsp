<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<title>Sigh in</title>
	</head>
	<body>

		<div class="container" id="login-container">

            <img src="${resource(dir: 'images', file: 'revizor-logo4-160.png')}" alt="Revizor logo"/>

			<g:form id="login-form" name="loginForm" controller="login" action="doLogin" class="form-signin form-horizontal" >
				<fieldset class="form">

                    <h3 class="form-signin-heading"><g:message code="please.sign.in" /></h3>

					<div class="form-group input-group input-group">
                        <span class="input-group-addon">@</span>
                        <g:field type="email" name="email" class="form-control" placeholder="Email address" value="${fieldValue(bean:loginCmd, field: 'email')}"/>
					</div>

                    <div class="form-group">
					    <g:passwordField name="password" class="form-control" placeholder="Password" />
                    </div>

                    <div class="form-group">
                        <button id="submit-login-form-btn" type="submit" class="btn btn-lg btn-primary btn-block">
                            ${message(code: 'default.sign.in', default: 'Sigh in')}
                        </button>
					</div>

				</fieldset>
			</g:form>

			<g:renderErrors bean="${loginCmd}"/>

		</div>

	<r:script>
		$('#submit-login-form-btn').on('click', function () {
			var $btn = $(this).button('toggle');
			$btn.attr('disabled','disabled');
			$btn.html("<span class='glyphicon glyphicon-refresh glyphicon-refresh-animate'></span>");
		});
	</r:script>


	</body>
</html>