package com.revizor

import com.revizor.utils.Constants

class ObserveChangesetController {

    def show() {

        def repositoryInstance = Repository.get(params.repositoryId)
        def changeset = repositoryInstance.initImplementation().getCommitInfo(params.changeset)

        // view concrete file
        def view;
        switch (params.viewType) {

            case Constants.REVIEW_SINGLE_VIEW:
                view = "showSingleView";
                break

            case Constants.REVIEW_SIDE_BY_SIDE_VIEW:
                view = "showSideBySideView";
                break;

            default:
                view = "show";
                break;
        }

        respond repositoryInstance, view: view, model:[
                fileName: params[Constants.PARAM_FILE_NAME],
                commit: changeset,
                urlPrefix: g.createLink(controller: 'observe', action: 'show', id: repositoryInstance.ident()) + "/${params.changeset}"
        ]

    }
}
