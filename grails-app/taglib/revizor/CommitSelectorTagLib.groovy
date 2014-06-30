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
            def isChecked = "";
            out << "<ul class='list-group'>"
            for (RevCommit rev : list) {
                isChecked = (rev.name().equals(attrs.selected)) ? "checked" : ""
                out << "<li class='list-group-item'><input type='checkbox' name='commits' value='${rev.name()}' $isChecked />"
                out << "<span class='truncate'> ${rev.name().subSequence(0, 7)} ${rev.getShortMessage() }</span> "
                out << "<span class='label label-default'>${rev.getAuthorIdent().getName()}</span></li>" /* rev.getId().getName() */
                count++;
                isChecked = ""
            }
            out << "</ul>"
            out << "<tr><td colspan='2'>Had " + count + " commits overall on current branch</td></tr>"

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
                            <a href="${createLink(controller: 'review', action: 'create', id: attrs.repo.ident(), params: [selected: rev.name()])}" class="btn btn-default btn-xs tree-context-button">
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
