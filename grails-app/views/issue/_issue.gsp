<%--

    Template that displays an issue ticket (link to remote Issue Tracker, like Jira, Youtrack and so on...)
--%>
<img height="32" width="32" src="${resource(dir: 'images/issue-trackers', file: issue.trackerLogoUrl)}" class="pull-left"/>
<div>
    <b><a href="${issue.issueUrl}">${issue.title}</a></b>
    <br/>
    <span class="glyphicon glyphicon-tags" style="margin-right: 10px;"></span>

    <g:each in="${issue.tags}" var="tag">
        <span class="label label-primary">${tag}</span>
    </g:each>

    <img height="32" width="32" src="${issue.authorImgUrl}" class="issue-author-avatar" title="${issue.authorName}" />

</div>