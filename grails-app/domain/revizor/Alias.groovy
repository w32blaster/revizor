package revizor

import com.revizor.User

class Alias {

    String aliasEmail

    static belongsTo = [
        user: User
    ]

    static constraints = {
    }
}
