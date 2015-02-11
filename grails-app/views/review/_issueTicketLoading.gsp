<div id="issue-wrapper-${issueId}" class="issue-wrapper">
    <div id="issue-ticket-${issueId}" class="issue-ticket well">
        <span class="glyphicon glyphicon-refresh glyphicon-refresh-animate"></span>
    </div>

    <g:if test="${isEdit}">
        <button type="button" class="btn btn-danger btn-xs issue-delete-btn" onclick="unassignIssue('issue-wrapper-${issueId}', ${issueId});">
            <span class="glyphicon glyphicon-trash"></span>
        </button>
    </g:if>
</div>