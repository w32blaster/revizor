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
			def pathContext = webRequest.currentRequest.contextPath
			def fullUrl = request.getRequestURL()
			session.baseUrl = fullUrl.substring(0, fullUrl.indexOf(pathContext) + pathContext.length())
			redirect controller: 'repository'
		}
	}
	
	def doLogout = {
		session.user = null
		redirect uri: '/'
	}
}