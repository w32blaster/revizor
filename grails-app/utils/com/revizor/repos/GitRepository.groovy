package com.revizor.repos

import org.eclipse.jgit.api.Git
import com.revizor.utils.Constants
import org.eclipse.jgit.internal.storage.file.FileRepository
import org.eclipse.jgit.revwalk.RevCommit
import org.eclipse.jgit.revwalk.RevWalk
import org.eclipse.jgit.diff.DiffEntry
import org.eclipse.jgit.lib.ObjectId
import org.eclipse.jgit.treewalk.CanonicalTreeParser
import org.eclipse.jgit.lib.ObjectReader
import org.eclipse.jgit.diff.DiffFormatter
import org.eclipse.jgit.lib.Ref
import org.eclipse.jgit.lib.RefDatabase;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.api.ListBranchCommand.ListMode;

import com.revizor.repos.Commit

/**
 * Implementation for the GIT repositories
 */
class GitRepository implements IRepository {

    private final repoHome;
    private final repoPath;

    public GitRepository(String folderName) {
        this.repoHome = Constants.LOCAL_REPO_PATH + File.separator + folderName;
        this.repoPath =  this.repoHome + File.separator + ".git"
    }

    /**
     * {@inheritDoc }
     */
    @Override
    def cloneRepository(String url) {

        def dirRepo = new File(this.repoHome)
        dirRepo.delete();

        Git git = Git.cloneRepository()
                .setURI(url)
                .setDirectory(dirRepo)
                .setCloneAllBranches(true)
                .call();

        //def localRepo = new FileRepository(this.repoPath);
        //Git git = new Git(localRepo)
        git.pull().call();

    }

    /**
     * {@inheritDoc }
     */
    @Override
    def getListOfCommits() {

        if (!this.repoPath) throw new RuntimeException("The folder name for the GIT repo was not specified")

        def localRepo = new FileRepository(this.repoPath);
        def git = new Git(localRepo);
        RevWalk walk = new RevWalk(localRepo);

        Iterable<RevCommit> logs = git.log()
                .all()
                .call()

        return logs.collect { commit ->

            new Commit(
                id: commit.getId().name(),
                parents: commit.getParents().collect({ it.getId().name()}),
                author: commit.getAuthorIdent().getName(),
                message: commit.getShortMessage()
            )}
    }

    /**
     * {@inheritDoc }
     */
    @Override
    def getMapBranchesReferences() {
        def localRepo = new FileRepository(this.repoPath);

        // build map "reference key" <=> "branch name"
        Map<String, List<String>> localRefs = new HashMap<String, List<String>>();
        localRepo.getAllRefs().each { key, value ->
            def newKey = value.objectId.getName().value.toString();
            if (localRefs.containsKey(newKey)) {
                localRefs.get(newKey).add(key);
            }
            else {
                localRefs.put(newKey, [key]);
            }
        };

        return localRefs;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    def getListOfMasterCommits() {
        def localRepo = new FileRepository(this.repoPath);
        def git = new Git(localRepo);

        // checkout to MASTER branch and select all the commits only from this branch
        git.checkout().setName(org.eclipse.jgit.lib.Constants.MASTER).call();
        return git.log().call().collect { it.getId().getName() }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    def getDiffForCommit(String commitID) {
		
        def localRepo = new FileRepository(this.repoPath);

        ObjectId parentID = localRepo.resolve("$commitID~1^{tree}");
        ObjectId childID = localRepo.resolve("$commitID^{tree}");

        ObjectReader reader = localRepo.newObjectReader();

        CanonicalTreeParser oldTreeIter = new CanonicalTreeParser();
        oldTreeIter.reset(reader, parentID);

        CanonicalTreeParser newTreeIter = new CanonicalTreeParser();
        newTreeIter.reset(reader, childID)

        List<DiffEntry> diffs = new Git(localRepo).diff().setOldTree(oldTreeIter).setNewTree(newTreeIter).call();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DiffFormatter df = new DiffFormatter(out);
        df.setRepository(localRepo);
		df.setContext(15);

        def output = []
        for (DiffEntry diff : diffs) {
            df.format(diff);
            output << out.toString("UTF-8").split('\n')
            out.reset();
        }

        localRepo.close();

        return output
    }
}
