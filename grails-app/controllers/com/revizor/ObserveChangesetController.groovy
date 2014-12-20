package com.revizor

import com.revizor.utils.Constants

class ObserveChangesetController {

    def show() {

        if (params.fileName) {
            // view concrete file
        }
        else {
            // view the summary of a given changeset
            def repositoryInstance = Repository.get(params.repositoryId)
            def changeset = repositoryInstance.initImplementation().getCommitInfo(params.changeset)
            println changeset

            respond repositoryInstance, view: "show", model:[
                    fileName: params[Constants.PARAM_FILE_NAME],
                    commit: changeset,
                    ]
        }
    }
}
