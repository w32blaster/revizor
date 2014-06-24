package com.revizor

import com.revizor.cmd.LoginCommand

class LoginController {

	static allowedMethods = [index: "GET", doLogin: "POST"]
	
	def index = {
		render(view: 'login')
	}

	def doLogin = {LoginCommand cmd ->
		
		if (cmd.hasErrors()) {
			render view: 'login', model: [loginCmd: cmd]
		}
		else {
			session.user = cmd.getUser()
			redirect controller: 'repository'
		}
	}
	
	def doLogout = {
		session.user = null
		redirect uri: '/'
	}
}