package revizor

class SecurityFilters {

    def filters = {

		/**
		 * Filter checks every request whether a caller is a logged in user or not.		
		 */
		loginCheck(controller: '*', action: '*') {
			before = {
				if (!session.user && !controllerName.equals('login')) {
					redirect(controller:'login', action: 'index')
					return false
				}
			}
		}
		
    }
}
