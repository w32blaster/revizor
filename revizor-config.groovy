
// change to your server actual URL. This setting will be used in redirection and link making
// grails.serverURL = "http://change-to-your-hostname-in-file-revizor-config:8080"
grails.serverURL = "http://localhost:8080/revizor"

// how often Revizor should check for new commits in a repositories (in ms)
grails.job.pull.period.time=10 * 60 * 1000 // 10 min

// should Revizor send notifications to user's email?
grails.allowed.email.notifications=false
/*
// SMTP server configuration to send emails. For more details and examples, please
// refer to the url http://grails.org/plugin/mail section "Configuration"
grails {
    mail {
        host = "smtp.zone.ee"
        port = 1025
        username = "email@email.com"
        password = '****'
        props = ["mail.smtp.auth": "true",
                 "mail.smtp.port": "1025",
                 "mail.smtp.starttls.enable": "false"]
    }
}
*/

// is LDAP authentication enabled?
ldap.enabled = false
/*
ldap {

    filter{
        email = "mail" // field name, that holds user email
        defaultFilter = "(objectClass=person)"
    }

    // please fill the properties for your LDAP server
    directories {
        directory1 {
            url = "ldap://ldap.server.com:389"
            base = ""
            userDn = ""
            password = ""
            searchControls {
                countLimit = 40
                timeLimit = 600
                searchScope = "subtree"
            }
        }
    }
}
*/