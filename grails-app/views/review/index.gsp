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
		
		<div id="list-review" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			
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

			<div class="btn-group">

				<!-- My reviews, where I am as an author -->
				<% def cssClassMy = (params.filter == ReviewFilter.ONLY_MINE.toString()) ? 'active' : '' %>
				<g:link controller="review" action="index" params="[filter: ReviewFilter.ONLY_MINE]" class="btn btn-default btn-primary ${cssClassMy}">
					<span class="glyphicon glyphicon-pencil"></span> 
					<g:message code="reviews.only.mine" default="My reviews" />
				</g:link>

				<!-- My invitations and reviews where I am a reviewer -->
				<% def cssClassInv = (params.filter == ReviewFilter.WHERE_I_AM_REVIEWER.toString()) ? 'active' : '' %>
				<g:link controller="review" action="index" params="[filter: ReviewFilter.WHERE_I_AM_REVIEWER]" class="btn btn-default btn-primary ${cssClassInv}">
					<span class="glyphicon glyphicon-thumbs-up"></span> 
					<g:message code="reviews.where.i.reviewer" default="My inspections" />
				</g:link>

				<!-- Finished (archived) reviews -->
				<% def cssClassArch = (params.filter == ReviewFilter.ARCHIVED.toString()) ? 'active' : '' %>
				<g:link controller="review" action="index" params="[filter: ReviewFilter.ARCHIVED]" class="btn btn-default btn-primary ${cssClassArch}">
					<span class="glyphicon glyphicon-inbox"></span> 
					<g:message code="reviews.archived" default="My reviews" />
				</g:link>

				<!-- All reviews -->
				<% def cssClassAll = (params.filter == ReviewFilter.ALL.toString()) ? 'active' : '' %>
				<g:link controller="review" action="index" params="[filter: ReviewFilter.ALL]" class="btn btn-default btn-primary ${cssClassAll}">
					<g:message code="reviews.all" default="All reviews" />
				</g:link>

			</div>

			<!-- Create new review -->
			<div class="btn-group" style="float: right;">
			  <button type="button" class="btn btn-success dropdown-toggle" data-toggle="dropdown">
			  	<g:message code="reviews.create.new" default="Create new" /> 
			  	<span class="caret"></span>
			  </button>
			  <ul class="dropdown-menu" role="menu">
			  	<g:each var="repo" in="${repos}">
			    	<li><a href="${createLink(controller: 'review', action: 'create', id: repo.ident())}">${repo.title}</a></li>
			    </g:each>
			  </ul>
			</div>

			<table class="table">
				<thead>
					<tr>
					
						<g:sortableColumn property="title" title="${message(code: 'review.title.label', default: 'Title')}" />
					
						<th><g:message code="review.repository.label" default="Repository" /></th>
					
						<th><g:message code="review.author.label" default="Author" /></th>
						
					
					</tr>
				</thead>
				<tbody>
					<g:each in="${reviewInstanceList}" status="i" var="reviewInstance">
						<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
						
							<td><g:link action="show" id="${reviewInstance.id}">${fieldValue(bean: reviewInstance, field: "title")}</g:link></td>
						
							<td class="reviewer">
							    <g:if test="${reviewInstance?.repository.image}">
							        <img height="32" width="32" class="avatar img-rounded" src="${createLink(controller:'repository', action: 'logo_image', id: reviewInstance?.repository.ident())}" />
							    </g:if>
								<span class="property-value" aria-labelledby="reviewers-label">${reviewInstance.repository.title}</span>
							</td>
						
							<td><g:render template="reviewer" model="['reviewer' : reviewInstance.author]" /></td>					
							
						</tr>
					</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${reviewInstanceCount ?: 0}" />
			</div>
		</div>
	</body>
</html>
