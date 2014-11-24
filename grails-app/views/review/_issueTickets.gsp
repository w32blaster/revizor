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

    function requestDetails(containerId, issueId) {
        var refreshRepositoryUrl = "${createLink(controller: 'issue', action: 'requestIssueDetails')}/" + issueId;
        var container = $("#" + containerId);
        // make a query
        $.get( refreshRepositoryUrl )
            .done(function(html) {
                container.empty();
                container.append(html);
            })
            .fail(function(errorObj, b, errorName) {
                if(errorObj.status == 404) {
                    container.empty();
                    container.append("issue not found");
                }
                else {
                    alert( "Error, can't load issue " + issueId );
                }
            });
    }

</r:script>

<g:each in="${issueTickets}" var="issue">
    <div id="issue-ticket-${issue.ident()}" class="issue-ticket bg-warning">
        <span class="glyphicon glyphicon-refresh glyphicon-refresh-animate"></span>
    </div>
</g:each>