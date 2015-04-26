<%@ page import="com.revizor.Review; com.revizor.ReviewStatus" contentType="text/html;charset=UTF-8" %>
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

                <div class="col-md-6">
                    <div class="panel panel-default">

                        <div class="panel-heading">
                            <h3 class="panel-title"><g:message code="reviews.active.reviews" /></h3>
                        </div>

                        <table class="table">
                            <g:each in="${com.revizor.Review.findAllByStatus(com.revizor.ReviewStatus.OPEN)}" var="reviewInstance">
                                <g:render template="/review/rowReviewInTable" model="[review: reviewInstance]" />
                            </g:each>
                        </table>
                    </div>
                </div>

                <div class="col-md-6">
                    <div class="panel panel-default">

                        <div class="panel-heading">
                            <h3 class="panel-title"><g:message code="what.is.new" /></h3>
                        </div>

                        <div id="notification-feed-container" class="pre-scrollable">
                            <g:render template="/notification/notificationFeed" />
                        </div>

                    </div>

                </div>

            </div>


            <div class="row">
                <div class="col-md-6">
                    <div class="panel panel-default">

                        <div class="panel-heading">
                            <h3 class="panel-title"><g:message code="repository.header" /></h3>
                        </div>

                        <g:each in="${com.revizor.Repository.list()}" var="repository">
                            <div class="hp-row">
                                <g:render template="repositoryHeader" model="[size: 32, repo: repository]" />
                            </div>
                        </g:each>
                     </div>
                </div>

                <div class="col-md-6">
                    <div class="panel panel-default">

                        <div class="panel-heading">
                            <h3 class="panel-title"><g:message code="users.header" /></h3>
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

</body>
</html>