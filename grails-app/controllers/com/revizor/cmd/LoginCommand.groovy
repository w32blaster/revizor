package com.revizor.cmd

import com.revizor.User
import com.revizor.security.BCrypt;

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
			def isMatching = BCrypt.checkpw(val, obj.getUser().password)
			if (obj.user && !isMatching) {
				return "user.password.invalid" 	
			}
		})
	}
}