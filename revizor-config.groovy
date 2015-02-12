
// how often Revizor should check new commits in a repositories (in ms)
grails.job.pull.period.time=10 * 60 * 1000 // 10 min

// should Revizor send notifications to user's email?
grails.allowed.email.notifications=false

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

// change to your server actual URL
grails.serverURL = "http://localhost:8080/revizor"