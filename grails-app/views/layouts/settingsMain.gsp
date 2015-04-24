<!DOCTYPE html>
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <html lang="en" class="no-js"><!--<![endif]-->
  <head>
    <title><g:layoutTitle default="Grails"/></title>

    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link rel="shortcut icon" href="${resource(dir: 'images', file: 'favicon.ico')}" type="image/x-icon">
    <link rel="apple-touch-icon" href="${resource(dir: 'images', file: 'apple-touch-icon.png')}">
    <link rel="apple-touch-icon" sizes="114x114" href="${resource(dir: 'images', file: 'apple-touch-icon-retina.png')}">

    <g:layoutHead/>

    <g:javascript library="jquery" plugin="jquery"/>
    <g:javascript library="application"/>
    <g:javascript library="bootstrap"/>
    <g:javascript library="selectbox"/>

    <link rel="stylesheet" href="${resource(dir: 'css', file: 'main.css')}" type="text/css">

    <r:layoutResources />
  </head>
  <body>

  <div id="page" class="container-full container-fluid">

    <div id="header" class="row" role="header">
      <div class="col-md-12">
        <a href="${createLink(uri: '/')}" class="inline-block">
          <img src="${resource(dir: 'images', file: 'revizor-logo4-43.png')}" alt="Revizor logo"/>
        </a>

        <g:if test="${session.user}">
          <div id="current-account-block" role="account-container">
            <g:render template="/user/userAvatar" model="['user' : session.user, 'size': 32]" />

            <a href="${createLink(controller: 'settings')}" class="btn btn-default btn-sm"><span class="glyphicon glyphicon-cog"></span></a>

            <g:link  controller="login" action="doLogout" class="btn btn-default btn-sm" title="${message(code: 'default.log.out')}"><span class="glyphicon glyphicon-off"></span></g:link>

          </div>
        </g:if>
      </div>
    </div>


    <div class="container">
      <div class="row">

        <div class="col-md-2">
          <g:render template="/layouts/settingsSidebar" />
        </div>

        <div class="col-md-10">
            <g:layoutBody/>
        </div>

      </div>
    </div>

    </div>
    <r:layoutResources />
  </body>
</html>
