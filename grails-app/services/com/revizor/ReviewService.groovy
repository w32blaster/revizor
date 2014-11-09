package com.revizor

import com.revizor.repos.Commit
import com.revizor.utils.Constants
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import revizor.Alias


class ReviewService {

    Log log = LogFactory.getLog(this.class)
    def notificationService

    /**
     * Runs through the given list of commits, and if it detects Smart Commits,
     * then it creates new review for each of them
     *
     * @param lstCommits
     * @return
     */
    def checkNewRevisionsForSmartCommits(List<Commit> lstCommits, Repository repository) {
        lstCommits.each { Commit commit ->

            if (commit.fullMessage.contains(Constants.SMART_COMMIT_CREATE_REVIEW)) {
                // smart commits detected
                println(">>>> Found commit $commit")
                Review review = this.createReviewFromSmartCommit(repository, commit)
                if (review) {
                    review.save(flush:true, failOnError: true)
                    notificationService.create(review.author, Action.REVIEW_START, [review.author, review])
                }
            }
        }
    }

    /**
     * Parses a commit message for a smart commit commands and
     * creates proper review
     *
     * @param message
     */
    def createReviewFromSmartCommit(repo, Commit commit) {

        def user = User.findByEmail(commit.authorEmail)
        if (!user) {
            def alias = Alias.findByAliasEmail(commit.authorEmail)
            user = alias?.user
        }

        if(user) {
            def arr = getHeaderAndMessage(commit)

            return new Review(author: user,
                    title: arr[0],
                    description: arr[1],
                    commits: [commit.id],
                    status: ReviewStatus.OPEN,
                    repository: repo,
                    smartCommitId: commit.id)
        }
        else {
            log.warn("Were asked to create a new review, but the author ${commit.author} was not found in the Revizor database, thus " +
                    "I don't know who is the author. The review was not created.")
            return null
        }
    }

    /**
     * Extracts header and message from commit message. Expected the first line
     * without "+review" smart commit tag
     *
     * @see https://github.com/w32blaster/revizor/wiki/Smart-commits for more details
     *
     * @param commit
     * @return array:
     *  [0] - extracted header
     *  [1] - extracted message (or empty string in case of commit has only one line)
     *  [2] - list of reviewers
     */
    def getHeaderAndMessage(Commit commit) {
        def lines = commit.fullMessage.readLines()
        def header = lines[0].contains(Constants.SMART_COMMIT_CREATE_REVIEW) ?
                lines[0].substring(0, lines[0].indexOf(Constants.SMART_COMMIT_CREATE_REVIEW)).trim()
                : lines[0];

        def message = (lines.size() == 1) ? "no description" : lines[1.. -1]
                                                    .join("\n")
                                                    .trim()
                                                    .replace(Constants.SMART_COMMIT_CREATE_REVIEW, "")


        return [header, message]
    }

}
