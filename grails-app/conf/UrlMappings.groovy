class UrlMappings {

	static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }
		
		"/review/show/$id/$viewType"(controller: "review", action: "show")

        "/" {
            controller = "repository"
            action = "index"
        }
        "500"(view:'/error')
	}
}
