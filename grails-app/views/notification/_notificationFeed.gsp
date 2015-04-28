<%@ page import="com.revizor.utils.Constants" %>

<%-- 
    The Notification feed with Javascript.
    At the end of feed is placed the button "Load more" that dynamically loads additional
    data from server.
--%>
<g:set var="feedNontainerId" value="only-feed-container" />

<r:script>

    var offset = 0;
    var notificationLoadMoreUrl = "${createLink(controller: 'notification', action: 'loadMore')}";
    var mutex = true;

    function loadFeedsWithOffset() {
        offset += <%= Constants.MAX_PER_REQUEST %>

        $.get( notificationLoadMoreUrl + "?offset=" + offset)
            .done(function(html) {
                if(!html.trim()) {
                    $("#${feedNontainerId}").append("<div class='well well-sm'>${message(code: 'dashboard.no.more.events')}</div>");
                    $container.unbind("scroll");
                }
                else {
                    $("#${feedNontainerId}").append(html);
                }
            })
            .fail(function() {
                alert( "Error, can't load notifications" );
            })
            .always(function() {
                mutex = true;
                $("#loading-label-id").fadeOut();

            });
    };

    // infinite loading
    var $container = $("#${notificationContainerID}");
    var $feed = $("#${feedNontainerId}")
    $container.scroll(function() {
        if (mutex) {
            if ($container.scrollTop() + $container.height() >= $feed.height()) {
                mutex = false;
                $("#loading-label-id").fadeIn();
                setTimeout(loadFeedsWithOffset, 300);
            }
        }
    });

    $('.mention-popover').popover();


</r:script>

<div id="${feedNontainerId}">
    <ntl:feed />
</div>

<div class="panel panel-default" id="loading-label-id" style="display: block;" >
    <div class="panel-body">
        <span class='glyphicon glyphicon-refresh glyphicon-refresh-animate'></span>
        <g:message code="dashboard.loading" />
    </div>
</div>
