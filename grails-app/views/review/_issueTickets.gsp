<%@ page import="com.revizor.IssueTracker" %>
<%--
    List of all the issue tickets and Ajax request.

    As long as information about tickets will be loaded from 3rd party API,
    this operation could be potentially long. That's why we load it using Ajax,
    separately from the main view rendering.
--%>
<r:script>

    <g:each in="${issueTickets}" var="issue">
        requestDetails("issue-ticket-${issue.ident()}", ${issue.ident()});
    </g:each>

    /**
     * Load issue and put it to the specified container
     */
    <g:render template="scriptLoadIssueTicket" model="[isSmall:false]" />

    <g:if test="${isEdit}">

        var assignUrl = "${createLink(controller: 'issue', action: 'assignReviewWithAnIssue')}/";
        $('#assign-issue-btn-id').click(function() {
            var data = {
                'tracker.id': $('#select-issue-tracker').val(),
                'key': $('#issue-key-id').val(),
                'assignToReview': ${reviewId},
                'review.id': ${reviewId}};
            $.post( assignUrl, data )
                .done(function(arrData) {
                    var container = $('#issue-ticket-container-id')
                    container.append(arrData[0]);
                    requestDetails("issue-ticket-" + arrData[1], arrData[1]);
                    $('#issue-key-id').val("");
                })
                .fail(function(errorObj, b, errorName) {
                    alert( "Error, can't save issue " );
                });
        });


        function unassignIssue(containerId, issueId, reviewId) {
            var unassignUrl = "${createLink(controller: 'issue', action: 'delete')}/";
            var data = { 'key': issueId};
            $.ajax({
                url: unassignUrl,
                type: 'DELETE',
                data: data
            })
            .done(function(arrData) {
                $("#" + containerId).empty();
            })
            .fail(function(errorObj, b, errorName) {
                    alert( "Error, can't delete issue " );
            });
        };

    </g:if>

</r:script>

<div id="issue-ticket-container-id">
    <g:each in="${issueTickets}" var="issue">
        <g:render template="issueTicketLoading" model="[issueId: issue.ident(), isEdit: isEdit]" />
    </g:each>
</div>

<g:if test="${isEdit}">
    <label class="control-label"><g:message code="review.associate.issue.tickets" /></label>
    <div class="input-group" style="max-width: 600px;">

            <select class="form-control" id="select-issue-tracker" style="width: 300px;">
                <g:each in="${com.revizor.IssueTracker.all}" var="issueTracker">
                <option value="${issueTracker.ident()}">${issueTracker.title}</option>
                </g:each>
            </select>

            <label class="input-group-addon"><g:message code="issue.key.label" /></label>

            <input id="issue-key-id" class="form-control" type="text" style="width: 100px;" placeholder="<g:message code="key.placeholder"/>" />

            <span class="input-group-btn">
                <button id="assign-issue-btn-id" class="btn btn-primary" type="button"><g:message code="assign.button" /></button>
            </span>
    </div>
</g:if>