<%@ page import="com.revizor.Comment; com.revizor.Review; com.revizor.ReviewStatus" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main" />
    <title><g:message code="dashboard.title" /></title>
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'dashboard.css')}" type="text/css">

</head>
<body>

    <div id="content-container">
        <div class="container">

            <div class="row">

                <div class="col-md-6 hp-equal-height">
                    <div class="panel panel-default full-height">

                        <div class="panel-heading">
                            <g:message code="reviews.active.reviews" />
                        </div>

                        <table class="table">
                            <g:each in="${activeReviews}" var="reviewInstance">
                                <g:render template="/review/rowReviewInTable" model="[
                                        review: reviewInstance,
                                        commentsByReview: commentsGroupedByReview,
                                        isUnread: (reviewInstance.ident() in unreadReviews)]" />
                            </g:each>
                        </table>
                    </div>
                </div>

                <div class="col-md-6 hp-equal-height">
                    <div class="panel panel-default full-height">

                        <div class="panel-heading">
                            <g:message code="what.is.new" />

                            <button id="mark-all-read-id" class="btn btn-link">
                                <span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
                                <g:message code="mark.all.read" />
                            </button>

                        </div>

                        <g:set var="notificationContainerID" value="notification-feed-container" />
                        <div id="${notificationContainerID}" class="pre-scrollable">
                            <g:render template="/notification/notificationFeed" />
                        </div>

                    </div>

                </div>

            </div>


            <div class="row">
                <div class="col-md-6 hp-equal-height">
                    <div class="panel panel-default full-height">

                        <div class="panel-heading">
                            <g:message code="repository.header" />
                        </div>

                        <div id="list-of-repositories" class="pre-scrollable">
                            <g:each in="${com.revizor.Repository.list(sort: 'id', order: 'desc')}" var="repository">
                                <g:set var="isUnread" value="${repository.ident() in unreadRepos}" />

                                <div class="hp-row <% if (isUnread) {%> unread unread-new<%} %>">
                                    <g:render template="repositoryHeader" model="[
                                            size: 32,
                                            repo: repository,
                                            isUnread: isUnread]" />
                                </div>
                            </g:each>
                        </div>
                     </div>
                </div>

                <div class="col-md-6 hp-equal-height">
                    <div class="panel panel-default full-height">

                        <div class="panel-heading">
                            <g:message code="users.header" />
                        </div>


                        <g:each in="${com.revizor.User.list()}" var="user">
                            <div class="hp-row">
                                <g:render template="/review/userWithAvatar" model="[user: user]" />
                            </div>
                        </g:each>
                    </div>
                </div>
            </div>

        </div>
    </div>

    <r:script>

        var ReadAllModule = (function($) {

            var init = function() {
                $("#mark-all-read-id").click(function() {
                    $.get("${createLink(controller: 'notification', action: 'markAllReadEvents')}")
                            .done(function() {
                                $("#notification-feed-container .unread").removeClass( "unread" );
                                toastr.success("All new events are marked as 'read'");
                            })
                            .fail(function(err) {
                                toastr.error('Error occurs while attempt to mark all the events as read. Error: ' + err.statusText)
                            });
                });
            };

            return {
                init : init
            };

        })(jQuery);

        ReadAllModule.init();

    </r:script>
</body>
</html>