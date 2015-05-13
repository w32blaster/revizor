package com.revizor.cmd

import com.revizor.LDAPUser
import com.revizor.User
import com.revizor.UserType
import com.revizor.security.BCrypt

@grails.validation.Validateable(nullable = true)
class LoginCommand {

	String email;
	String password;

	private u
    private ldapUser

    def userService
    def grailsApplication

    /**
     * Gets inner user from local database
     *
     * @return
     */
	User getUser() {
		if (!u && email) {
			u = User.findByEmailAndType(email, UserType.INNER)
		}
		return u
	}

    /**
     * Get LDAP user from remote directory
     *
     * @return
     */
    LDAPUser getLdapUser() {
        if (!ldapUser && email) {
            def emailField = grailsApplication.config.ldap.filter.email
            ldapUser = LDAPUser.find(
                    filter: "($emailField=$email)"
            )
        }
        return ldapUser
    }

    /**
     * Get user that logged in. It could be any type (inner, Ldap...)
     * @return
     */
    User getLoggedUser() {
        if (u) {
            return u;
        }
        else if (ldapUser) {
            def user = User.findByEmailAndType(email, UserType.LDAP)
            if (!user) {
                // if the LDAP user does not exist in our database, then create
                user = userService.fromLdap(ldapUser)
                user.save failOnError: true
            }
            return user

        }
    }

	static constraints = {
		email(blank:false, email: true , validator: {val, obj ->
            if (!obj.getUser() && !obj.getLdapUser()) return "user.not.found"
		})
		password(validator: { val, obj ->
            if (!val) {
                return "user.password.is.empty"
            }
            else if (obj.getUser()) {
                def isMatching = BCrypt.checkpw(val, obj.getUser().password)
                if (obj.user && !isMatching) {
                    return "user.password.invalid"
                }
            }
            else if(obj.getLdapUser()) {
                if(!obj.getLdapUser().authenticate(val)) {
                    return "user.password.invalid"
                }
            }
		})
	}

}