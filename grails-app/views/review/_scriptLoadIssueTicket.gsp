function requestDetails(containerId, issueId) {
    var refreshRepositoryUrl = "${createLink(controller: 'issue', action: isSmall ? 'requestIssueDetailsSmall' : 'requestIssueDetailsBig')}/" + issueId;
        var container = $("#" + containerId);
        // make a query
        $.get( refreshRepositoryUrl )
            .done(function(html) {
                container.empty();
                container.append(html);
            })
            .fail(function(errorObj, b, errorName) {
                container.empty();
                if(errorObj.status == 404) {
                    container.append("Issue not found");
                }
                else {
                    container.append("<p class='text-danger'>Cannot load issue. " + errorName + "</p>");
                }
            });
}