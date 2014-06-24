package com.revizor.cmd

import com.revizor.User;

@grails.validation.Validateable
class LoginCommand {

	String email;
	String password;
	
	private u
	
	User getUser() {
		if (!u && email) {
			u = User.findByEmail(email)
		}	
		return u
	}

	static constraints = {
		email(blank:false, email: true , validator: {val, obj -> 
			if (!obj.getUser()) return "user.not.found"	
		})
		password(blank:false, validator: { val, obj ->
			if (obj.user && obj.user.password != val) { 
				return "user.password.invalid" 	
			}
		})
	}
}