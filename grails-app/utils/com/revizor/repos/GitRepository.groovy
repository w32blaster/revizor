package com.revizor.repos

import org.eclipse.jgit.api.Git
import com.revizor.utils.Constants
import org.eclipse.jgit.internal.storage.file.FileRepository
import org.eclipse.jgit.revwalk.RevCommit
import org.eclipse.jgit.diff.DiffEntry
import org.eclipse.jgit.lib.ObjectId
import org.eclipse.jgit.treewalk.CanonicalTreeParser
import org.eclipse.jgit.lib.ObjectReader
import org.eclipse.jgit.diff.DiffFormatter

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

        Git.cloneRepository()
                .setURI(url)
                .setDirectory(dirRepo)
                .call();

        def localRepo = new FileRepository(this.repoPath);
        Git git = new Git(localRepo)
        git.pull().call();
    }

    /**
     * {@inheritDoc }
     */
    @Override
    def getListOfCommits() {

        if (!this.repoPath) throw new RuntimeException("The folder name for the GIT repo was not specified")

        def localRepo = new FileRepository(this.repoPath);
        Iterable<RevCommit> logs = new Git(localRepo).log()
                .all()
                .call()

        return logs
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
