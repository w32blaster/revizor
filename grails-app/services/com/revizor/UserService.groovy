package com.revizor

class UserService {

    def User fromLdap(LDAPUser ldapUser) {
        return new User(
                id: ldapUser.cn,
                username: ldapUser.sn,
                email: ldapUser.email,
                type: UserType.LDAP,
                role: Role.USER,
                position: "LDAP User"
        )
    }
}
