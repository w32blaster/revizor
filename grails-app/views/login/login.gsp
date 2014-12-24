<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<title>Sigh in</title>
	</head>
	<body>

		<div class="container" id="login-container">
	
			<g:form id="login-form" name="loginForm" controller="login" action="doLogin" class="form-signin form-horizontal" >
				<fieldset class="form">
					<h2 class="form-signin-heading">Please sign in</h2>

					<div class="input-group">
						<span class="input-group-addon">@</span>
						<g:field type="email" name="email" class="form-control" placeholder="Email address" value="${fieldValue(bean:loginCmd, field: 'email')}"/>
					</div>

					<g:passwordField name="password" class="form-control" placeholder="Password" /> 

					<button id="submit-login-form-btn" type="submit" class="btn btn-lg btn-primary btn-block">
						${message(code: 'default.sign.in', default: 'Sigh in')}
					</button>
					
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