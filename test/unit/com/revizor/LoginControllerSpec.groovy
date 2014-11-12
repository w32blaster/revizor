package com.revizor

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification


@TestFor(LoginController)
@Mock(User)
class LoginControllerSpec extends Specification {
	
    def setup() {
    }

    def cleanup() {
    }

    def "User is not found while login"() {
		given:
			request.method = 'POST'
		
		when: "non-existing user tries to login"
			params.email = 'non-existing-user@mail.com'
			params.password = 'any-password'

		and:
			controller.doLogin()
			
		then:
			def cmd = model.loginCmd
		
		and: "command contains errors"
			cmd.hasErrors()
			cmd.errors.errorCount == 1
			
			'user.not.found' == cmd.errors['email'].code
			
		and: "user is not registered in the session" 
			session.user == null
		
		and: "login form is rendered again"
			'/login/login' == view
    }
	
	def "User is found but the password is incorrect"() {
		given:
			request.method = 'POST'
			
		and: "add an user to the database" 
			def u = new User(
				email: "user@email.com",
				username: "Alexand Vasiljevitch",
				password: "12234567",
				role: Role.ADMIN
			).save()
			
		expect:
			u != null
			
		when: "this user tries to login"
			params.email = "user@email.com"
			params.password = "wrong-password"
			
		and:
			controller.doLogin()
			
		then: "the model contains errors"
			def cmd = model.loginCmd
			cmd.hasErrors()
			
		and: "errors array contains the message about the wrong password"
			'user.password.invalid' == cmd.errors['password'].code
		
		and: "user is not registered in the session"
			session.user == null
			
		and: "login form is rendered again"
			'/login/login' == view
	}
}
