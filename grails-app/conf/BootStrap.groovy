import com.revizor.Role
import com.revizor.User

class BootStrap {

    def init = { servletContext ->

        if (!User.count()) {
            User user = new User(
                    email: 'admin@admin.com',
                    username: 'admin',
                    password: 'admin123',
                    role: Role.ADMIN)
            user.save(failOnError: true)
        }
    }

    def destroy = {
    }
}
