package revizor

class HideSecretTagLib {

    static defaultEncodeAs = 'html'
    static namespace = "hs"

    def sshPattern = ~/ssh:\/\/\w+:\S+@\S+/

    /**
     * Replaces password with asterisks in case if
     * the given string is a ssh url with username and password
     *
     */
    def maskPassword = { attrs, body ->
        def value = body()
        if (sshPattern.matcher(value)) {
            out << (value =~ /(?<!ssh):\S+@/).replaceAll(":****@")
        }
        else {
            out << value
        }
    }

}
