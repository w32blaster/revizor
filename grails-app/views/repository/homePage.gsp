<%@ page contentType="text/html;charset=UTF-8" %>
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
                <div class="col-md-6">.col-md-5</div>
                <div class="col-md-6">.col-md-5</div>
            </div>


            <div class="row">
                <div class="col-md-6">
                    <h3><g:message code="repository.header" /></h3>

                    <g:each in="${com.revizor.Repository.list()}" var="repository">
                        <div class="row">
                            <g:render template="repositoryHeader" model="[size: 32, repo: repository]" />
                        </div>
                    </g:each>
                </div>

                <div class="col-md-6">
                    <h3><g:message code="users.header" /></h3>

                    <g:each in="${com.revizor.User.list()}" var="user">
                        <div class="row">
                            <g:render template="/review/userWithAvatar" model="[user: user]" />
                        </div>
                    </g:each>

                </div>
            </div>

        </div>
    </div>

</body>
</html>