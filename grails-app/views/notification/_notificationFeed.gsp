<%@ page import="com.revizor.utils.Constants" %>

<%-- 
    The Notification feed with Javascript.
    At the end of feed is placed the button "Load more" that dynamically loads additional
    data from server.
--%>
<g:set var="buttonId" value="load-more-btn" />
<g:set var="feedNontainerId" value="only-feed-container" />

<g:javascript>
    var offset = 0;
    var button = $("#${buttonId}");
    var notificationLoadMoreUrl = "${createLink(controller: 'notification', action: 'loadMore')}";

    button.click(function(){
        offset += <%= Constants.MAX_PER_REQUEST %>
        button.attr("disabled", "disabled");

        $.get( notificationLoadMoreUrl + "?offset=" + offset)
            .done(function(html) {
                $("#${feedNontainerId}").append(html);
            })
            .fail(function() {
                alert( "Error, can't load notifications" );
            })
            .always(function() {
                button.removeAttr("disabled");
            });
    });

    	$('.mention-popover').popover();

</g:javascript>

<div id="${feedNontainerId}">
    <notification:feed />
</div>

<button id="${buttonId}" class="btn btn-default btn-primary center-block">
    <g:message code="dashboard.load.more" default="More..." />
     <span class="caret"></span> 
 </button>
