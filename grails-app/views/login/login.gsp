<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<title>Sigh in</title>
	</head>
	<body>

		<div class="container" id="login-container">
	
			<g:form name="loginForm" controller="login" action="doLogin" class="form-signin" >
				<fieldset class="form">
					<h2 class="form-signin-heading">Please sign in</h2>
					
					<g:field type="email" name="email" class="form-control" placeholder="Email address" value="${fieldValue(bean:loginCmd, field: 'email')}"/>
					<g:passwordField name="password" class="form-control" placeholder="Password" /> 
					
					<label class="checkbox"> <input type="checkbox" value="remember-me">
						<g:message code="default.sign.in" default="Sign in" />
					</label>
					
					<input type="submit" class="btn btn-lg btn-primary btn-block" value="${message(code: 'default.sign.in', default: 'Sigh in')}">
					
				</fieldset>
			</g:form>
			
			<g:renderErrors bean="${loginCmd}"/>
			
		</div>

	</body>
</html>