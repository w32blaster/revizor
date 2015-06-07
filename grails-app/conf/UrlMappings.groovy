class UrlMappings {

	static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }
		
		"/review/show/$id/$viewType"(controller: "review", action: "show")

        "/observe/show/$repositoryId/$changeset/$viewType?"(controller: "observeChangeset", action: "show")

        "/repository/checkFolderExistence/$folderName"(controller: "repository", action: "checkFolderExistence")

        "/" {
            controller = "repository"
            action = "index"
        }
        "500"(view: '/error')

        "/settings" (view: '/settings')
	}
}
