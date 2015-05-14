<%@ page import="com.revizor.ReviewFilter" %>
<%@ page import="com.revizor.Review" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'review.label', default: 'Review')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>


    <!-- Breadcrumbs -->
    <div class="row" role="breadcrumb">
        <ul class="breadcrumb">
            <li class="active">
                    ${message(code: ReviewFilter.msgCodeFromValue(params.filter))}
            </li>
        </ul>
    </div>


    <div id="content-container">

		<div id="list-review" class="content scaffold-list" role="main">

            <h1>${message(code: ReviewFilter.msgCodeFromValue(params.filter))}</h1>
			
			<g:if test="${flash.message}">
				<div class="alert alert-info">${flash.message}</div>
			</g:if>
			<g:if test="${flash.error}">
				<div class="errors">
					<ul>
						<li>${flash.error}</li>
					</ul>
				</div>
			</g:if>

    <div class="container">

            <g:render template="reviewFilterButtons" />

			<!-- Create new review -->
			<div class="btn-group" style="float: right;">
			  <button type="button" class="btn btn-success dropdown-toggle" data-toggle="dropdown">
			  	<g:message code="reviews.create.new" default="Create new" /> 
			  	<span class="caret"></span>
			  </button>
			  <ul class="dropdown-menu" role="menu">
			  	<g:each var="repo" in="${repos}">
			    	<li><a href="${createLink(controller: 'review', action: 'create', id: repo.ident())}">
						<g:render template="/repository/repositoryLogo" />
						${repo.title}
					</a></li>
			    </g:each>
			  </ul>
			</div>

			<g:if test="${params.filter == ReviewFilter.GROUPED_BY_ISSUE_TICKETS.toString()}">
				<g:render template="tableReviewsGroupedByIssues" />
			</g:if>
			<g:else>
				<g:render template="tableReviews" />
			</g:else>


			<div class="pagination">
				<g:paginate total="${reviewInstanceCount ?: 0}" />
			</div>
			</div>
		</div>
	</body>
</html>
