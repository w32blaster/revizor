<%--

    Template that displays an issue ticket (link to remote Issue Tracker, like Jira, Youtrack and so on...)
--%>
<img height="32" width="32" src="${resource(dir: 'images/issue-trackers', file: issue.trackerLogoUrl)}" class="pull-left"/>
<div>
    <b><a href="${issue.issueUrl}">${key}: ${issue.title}</a></b>
    <br/>
    <g:if test="${issue.tags}">
        <span class="glyphicon glyphicon-tags" style="margin-right: 10px;"></span>

        <g:each in="${issue.tags}" var="tag">
            <span class="label label-default">${tag}</span>&nbsp;
        </g:each>
    </g:if>

    <g:if test="${issue.authorImgUrl}">
        <img height="32" width="32" src="${issue.authorImgUrl}" class="issue-author-avatar right" title="${issue.authorName}" />
    </g:if>
    <span class="issue-author right">${issue.authorName}</span>
</div>