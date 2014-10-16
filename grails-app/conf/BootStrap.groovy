import com.revizor.Role
import com.revizor.User
import com.revizor.utils.Constants

class BootStrap {

    def init = { servletContext ->

        def repoDir = new File(Constants.LOCAL_REPO_PATH)
        if (!repoDir.exists()) repoDir.mkdirs()

        println "Checking the user"
        if (User.all.isEmpty()) {

            //insert some default user
            new User(
                    email: 'admin@admin.com',
                    username: 'admin',
                    password: 'admin123',
                    role: Role.ADMIN
            ).save(failOnError: true)
        }
    }

    def destroy = {
    }
}
