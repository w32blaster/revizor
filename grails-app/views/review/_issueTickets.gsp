<%@ page import="com.revizor.IssueTracker" %>
<%--
    List of all the issue tickets and Ajax request.

    As long as information about tickets will be loaded from 3rd party API,
    this operation could be potentially long. That's why we load it using Ajax,
    separately from the main view rendering.
--%>

<g:set var="isEmpty" value="${com.revizor.IssueTracker.all.size() == 0}" />

<r:script>

    <g:each in="${issueTickets}" var="issue">
        requestDetails("issue-ticket-${issue.ident()}", ${issue.ident()}, "${issue.key}");
    </g:each>

    /**
     * Load issue and put it to the specified container
     */
    <g:render template="scriptLoadIssueTicket" model="[isSmall:false]" />

    <g:if test="${isEdit && !isEmpty}">

        var assignUrl = "${createLink(controller: 'issue', action: 'assignReviewWithAnIssue')}/";
        $('#assign-issue-btn-id').click(function() {
            var issueTicketKey = $('#issue-key-id').val();
            if (!issueTicketKey) {
                toastr.error("<g:message code="issue.ticket.key.is.not.specified" />");
                return;
            }

            var $btn = $(this).button('toggle');
            disableButtonWithLoading($btn);

            var data = {
                'tracker.id': $('#select-issue-tracker').val(),
                'key': issueTicketKey,
                'assignToReview': ${reviewId},
                'review.id': ${reviewId}};

            $.post( assignUrl, data )
                .done(function(arrData) {
                    var container = $('#issue-ticket-container-id')
                    container.append(arrData[0]);
                    requestDetails("issue-ticket-" + arrData[1], arrData[1], arrData[2]);
                    $('#issue-key-id').val("");

                    releaseDownloadedButton($btn, "<g:message code="assign.button" />", null, false);
                    toastr.success("<g:message code="issue.ticket.was.assigned" default="Review was finished." />");
                })
                .fail(function(errorObj, b, errorName) {
                    $btn.addClass("btn-danger");
                    $btn.html("<span class='glyphicon glyphicon-exclamation-sign'></span>");
					toastr.error("Can't assign issue: " + errorName)
                });
        });


        function unassignIssue(containerId, issueId) {
            var unassignUrl = "${createLink(controller: 'issue', action: 'deleteByKey')}/" + issueId;
            $.ajax({
                url: unassignUrl,
                type: 'DELETE'
            })
            .done(function(arrData, b, c) {
                toastr.success('<g:message code="issue.ticket.was.deleted.successfully" default="Issue was removed" />');
                $("#" + containerId).remove();
            })
            .fail(function(errorObj, b, errorName) {
                toastr.error( "Error, can't delete issue " );
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

        <g:if test="${isEmpty}">
            <!-- Disabled inputs -->
            <select class="form-control selectpicker" data-style="btn-primary" id="select-issue-tracker" style="width: 300px;" disabled="disabled">
                <option>${message(code: "no.issue.trackers.found")}</option>
            </select>

            <label class="input-group-addon"><g:message code="issue.key.label" /></label>

            <input id="issue-key-id" class="form-control" type="text" style="width: 100px;" placeholder="<g:message code="key.placeholder"/>"  disabled="disabled" />

            <span class="input-group-btn">
                <button class="btn btn-primary" type="button" disabled="disabled"><g:message code="assign.button" /></button>
            </span>
        </g:if>
        <g:else>
            <select class="form-control selectpicker" data-style="btn-primary" id="select-issue-tracker" style="width: 300px;">
                <g:each in="${com.revizor.IssueTracker.all}" var="issueTracker">
                    <option value="${issueTracker.ident()}">${issueTracker.title}</option>
                </g:each>
            </select>

            <label class="input-group-addon"><g:message code="issue.key.label" /></label>

            <input id="issue-key-id" class="form-control" type="text" style="width: 100px;" placeholder="<g:message code="key.placeholder"/>" />

            <span class="input-group-btn">
                <button id="assign-issue-btn-id"  class="btn btn-primary" type="button"><g:message code="assign.button" /></button>
            </span>
        </g:else>


    </div>
</g:if>