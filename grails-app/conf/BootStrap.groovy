import com.revizor.Role
import com.revizor.User
import com.revizor.UserType
import com.revizor.security.BCrypt
import com.revizor.utils.Constants

class BootStrap {

    def init = { servletContext ->

        def repoDir = new File(Constants.LOCAL_REPO_PATH)
        if (!repoDir.exists()) repoDir.mkdirs()

        def dbDir = new File(Constants.LOCAL_DB_PATH)
        if (!dbDir.exists()) dbDir.mkdirs()

        println "Checking the user"
        if (User.all.isEmpty()) {

            //insert some default user
            new User(
                    email: 'admin@admin.com',
                    username: 'admin',
                    position: 'Administrator',
                    type: UserType.INNER,
                    password: BCrypt.hashpw('admin123', BCrypt.gensalt()),
                    role: Role.ADMIN
            ).save(failOnError: true)
        }
    }

    def destroy = {
    }
}
