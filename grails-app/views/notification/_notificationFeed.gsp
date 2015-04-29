<%@ page import="com.revizor.utils.Constants" %>

<%-- 
    The Notification feed with Javascript.
    At the end of feed is placed the button "Load more" that dynamically loads additional
    data from server.
--%>
<g:set var="feedContainerId" value="only-feed-container" />

<r:script>

    var FeedModule = (function($) {

        var offset = 0;
        var notificationLoadMoreUrl = "${createLink(controller: 'notification', action: 'loadMore')}";
        var mutex = true;

        var $container = $("#${notificationContainerID}");
        var $feed = $("#${feedContainerId}")

        /**
         * Load additional feeds with offset
         * @private
         */
        var _loadFeedsWithOffset = function() {
            offset += <%= Constants.MAX_PER_REQUEST %>

            $.get( notificationLoadMoreUrl + "?offset=" + offset)
                .done(function(html) {
                    if(!html.trim()) {
                        $("#${feedContainerId}").append("<div class='well well-sm'>${message(code: 'dashboard.no.more.events')}</div>");
                        $container.unbind("scroll");
                        $("#loading-label-id").hide();
                    }
                    else {
                        $("#${feedContainerId}").append(html);
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

        /**
        * Checks that the current position of feed on the bottom or not
        * @returns {boolean}
        * @private
        */
        var _isBottomIsReached = function() {
            return ($container.scrollTop() + $container.height() >= $feed.height());
        };

        var init = function() {
            if (!_isBottomIsReached()) {
                $container.scroll(function() {
                    if (mutex) {
                        if (_isBottomIsReached()) {
                            mutex = false;
                            $("#loading-label-id").fadeIn();
                            setTimeout(_loadFeedsWithOffset, 300);
                        }
                    }
                });
            }
        };

        return {
          init : init
        };

    })(jQuery);

    FeedModule.init();

    $('.mention-popover').popover();

</r:script>

<div id="${feedContainerId}">
    <ntl:feed />
</div>

<div id="loading-label-id" class="panel panel-default" style="display: block; opacity: 0;">
    <div class="panel-body">
        <g:message code="dashboard.loading" />
    </div>
</div>
