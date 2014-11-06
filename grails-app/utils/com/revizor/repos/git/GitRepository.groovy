package com.revizor.repos.git

import com.revizor.repos.IRepository
import org.eclipse.jgit.api.CloneCommand
import org.eclipse.jgit.api.Git
import com.revizor.utils.Constants
import org.eclipse.jgit.api.PullCommand
import org.eclipse.jgit.api.errors.GitAPIException
import org.eclipse.jgit.internal.storage.file.FileRepository
import org.eclipse.jgit.revplot.PlotCommit
import org.eclipse.jgit.revplot.PlotCommitList
import org.eclipse.jgit.revplot.PlotLane
import org.eclipse.jgit.revplot.PlotWalk
import org.eclipse.jgit.revwalk.RevCommit
import org.eclipse.jgit.diff.DiffEntry
import org.eclipse.jgit.lib.ObjectId
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider
import org.eclipse.jgit.treewalk.CanonicalTreeParser
import org.eclipse.jgit.lib.ObjectReader
import org.eclipse.jgit.diff.DiffFormatter
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
    def cloneRepository(String url, String username, String password) {

        def dirRepo = new File(this.repoHome)
        if (dirRepo.exists()) {
            dirRepo.delete()
            dirRepo.mkdirs()
        }

        CloneCommand command = Git.cloneRepository()
                .setURI(url)
                .setDirectory(dirRepo)
                .setCloneSubmodules(true)
                .setCloneAllBranches(true);

        if (username != null && password != null) {
            command.setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password))
        }

        Git git = command.call()

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

        Iterable<RevCommit> logs = git.log()
                .all()
                .call()

        return _collectCommits(logs)

    }

    /**
     * {@inheritDoc }
     */
    @Override
    def getGraphSVG() {
        if (!this.repoPath) throw new RuntimeException("The folder name for the GIT repo was not specified")

        def localRepo = new FileRepository(this.repoPath);

        PlotWalk pw = new PlotWalk(localRepo);
        RevCommit rootCommit = pw.parseCommit(localRepo.resolve(org.eclipse.jgit.lib.Constants.HEAD));

        pw.markStart(rootCommit);
        //pw.markUninteresting(rw.parseCommit(to));

        PlotCommitList<PlotLane> commitList = new PlotCommitList<PlotLane>();
        commitList.source(pw);
        commitList.fillTo(Integer.MAX_VALUE);

        GitGraphRenderer renderer = new GitGraphRenderer();

        StringBuffer sb = new StringBuffer()
        def commits = []
        def maxLaneIdx = 0
        for (int i = 0; i < commitList.size(); i++) {
            PlotCommit<PlotLane> commit = commitList.get(i);
            renderer.reset(i);
            renderer.paintCommit(commit, Constants.ROW_HEIGHT)
            def pos = commit.getLane().getPosition()
            if (maxLaneIdx < pos) maxLaneIdx = pos

            def renderedCommit = renderer.getRenderedCommit()

            renderedCommit.setId(commit.getId().name())
            renderedCommit.setAuthor(commit.getAuthorIdent().getName())
            renderedCommit.setAuthorEmail(commit.getAuthorIdent().getEmailAddress())
            sb.append(renderedCommit.svg)
            commits << renderedCommit
        }

        return [sb.toString(), commits, maxLaneIdx]
    }

    /**
     * Pull the latest changes from an origin (remote repository). E.g update repo.
     */
    @Override
    def updateRepo() {
        def localRepo = new FileRepository(this.repoPath);
        def git = new Git(localRepo);

        // Firstly, get the local HEAD before fetching
        ObjectId headIdBeforePulling = localRepo.resolve(org.eclipse.jgit.lib.Constants.HEAD);

        PullCommand pullCmd = git.pull();
        try {
            pullCmd.call();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }

        ObjectId headIdAfterPulling = localRepo.resolve(org.eclipse.jgit.lib.Constants.HEAD);

        if (headIdAfterPulling.compareTo(headIdBeforePulling) != 0) {
            Iterable<RevCommit> logs = git.log()
                    .addRange(headIdBeforePulling, headIdAfterPulling)
                    .call();

            return _collectCommits(logs)
        }
        else {
            return []
        }
    }

    /**
     * Transform list of JGit commits to the list of com.revizor.repos.Commit commits,
     * that the rest of the code deals regardless of repository type
     *
     * @param logs
     * @return
     */
    private List<Commit> _collectCommits(Iterable<RevCommit> logs) {
        return logs.collect { commit ->

            new Commit(
                    id: commit.getId().name(),
                    author: commit.getAuthorIdent().getName(),
                    authorEmail: commit.getAuthorIdent().getEmailAddress(),
                    message: commit.getShortMessage(),
                    fullMessage: commit.getFullMessage()
            )
        }
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
