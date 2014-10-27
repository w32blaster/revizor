package com.revizor.repos

/**
 * Interface represents a repository. Implementations should contain actual logic to work with remote and local repositories,
 * such as GIT, Mercurial, SVN and so on...
 */
public interface IRepository {

    /**
     * Clones repository to the local hard drive.
     *
     * Called when an user adds a new repo to the project.
     * After successful cloning a repo should
     * be updated with a last changes and be ready to work with it
     *
     * @param url - URL of a repository
     */
    def cloneRepository(String url);

    /**
     * Returns the list of last commits
     *
     * @return list of commits (com.revizor.repos.Commit)
     */
    def getListOfCommits();

    /**
     * Returns DIFF between a commit and its parent
     *
     * @param commitID - SHA-1 of a commit
     * @return list of list: [ [lines from file 1], [lines from file 2], [lines from file 3]... ]. The
     * List contains the files, splitted by line
     */
    def getDiffForCommit(String commitID);

    def getGraph()
}