// external properties file

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