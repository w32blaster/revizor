package revizor

import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.revwalk.RevCommit
import org.eclipse.jgit.internal.storage.file.FileRepository

class CommitSelectorTagLib {

    static namespace = "sc"

    def selectCommitsForReview = {attrs, body ->

        _printCommits(attrs, { list ->
            
            int count = 0;
            out << "<table>"
            for (RevCommit rev : list) {
                out << "<tr><td width='20'><input type='checkbox' name='commits' value='${rev.name()}'></td>"
                out << "<td class='truncate'> ${rev.name().subSequence(0, 7)} ${rev.getShortMessage() }</td>"
                out << "<td>by ${rev.getAuthorIdent().getName()}</td></tr>" /* rev.getId().getName() */
                count++;
            }
            out << "<tr><td colspan='2'>Had " + count + " commits overall on current branch</td></tr>"
            out << "</table>"

        })
    }

    def buildFlatListofCommits = { attrs, body ->
        _printCommits(attrs, { list ->

            out << "<ul class='list-group'>"
            for (RevCommit rev : list) {
                out << """
                        <li class="list-group-item truncate" title="${rev.name().subSequence(0, 7)}">
                            ${rev.getShortMessage() } 
                            <span class="label label-default">${rev.getAuthorIdent().getName()}</span>
                            <a href="${createLink(controller: 'review', action: 'create', id: attrs.repo.ident())}" class="btn btn-default btn-xs tree-context-button">
                                <span class="glyphicon glyphicon-plus"></span>
                            </a>
                         </li>
                        """
            }
            out << "</ul>"
        })
    }

    def _printCommits(attrs, closurePrint) {
        if (null == attrs.repo) {
            out << "A repository is not specified"
        }
        else {
            def repo = attrs.repo.initImplementation();
            def commitList = repo.getListOfCommits();

            closurePrint(commitList);
        }
    }
}
