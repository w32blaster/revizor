package revizor

import com.revizor.repos.Commit

class CommitSelectorTagLib {

    static namespace = "sc"

    def selectCommitsForReview = {attrs, body ->

        _printCommits(attrs, { list ->
            
            int count = 0;
            def isChecked = "";
            out << "<ul class='list-group'>"
            for (Commit rev : list) {
                isChecked = (rev.id.equals(attrs.selected)) ? "checked" : ""
                out << "<li class='list-group-item'><input type='checkbox' name='commits' value='${rev.id}' $isChecked />"
                out << "<span class='truncate'> ${rev.id.subSequence(0, 7)} ${rev.message }</span> "
                out << "<span class='label label-default'>${rev.author.getName()}</span></li>" /* rev.getId().getName() */
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
            for (Commit rev : list) {
                out << """
                        <li class="list-group-item truncate" title="${rev.id.subSequence(0, 7)}">
                            ${rev.message } 
                            <span class="label label-default">${rev.author}</span>
                            <a href="${createLink(controller: 'review', action: 'create', id: attrs.repo.ident(), params: [selected: rev.id])}" class="btn btn-default btn-xs tree-context-button">
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
