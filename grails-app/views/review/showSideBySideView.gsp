<%@ page import="com.revizor.CommentType" %>
<%@ page import="com.revizor.Review" %>

<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="main">
	<g:set var="entityName" value="${message(code: 'review.label', default: 'Review')}" />
	<title><g:message code="default.show.label" args="[entityName]" /></title>

	<link rel="stylesheet" href="${resource(dir: 'css/prettify', file: 'prettify.css')}" type="text/css">
	<script type="text/javascript" src="${resource(dir: 'js/prettify', file: 'prettify.js')}"></script>
</head>
<body>

<div class="row">

	<div class="col-md-3">

		<ft:showFilesForReview
				repo="${reviewInstance.repository}"
				commitID="${reviewInstance.commits[0]}"
				reviewId="${reviewInstance.id}"
				urlPrefix="${urlPrefix}"/>

	</div>

	<div class="col-md-9">

		<small>
			<g:link controller="review" action="show" params="[id: reviewInstance.id]" class="btn btn-default btn-success btn-xs">
				<span class="glyphicon glyphicon-arrow-left"></span>
				<g:message code="default.back.label" args="[entityName]" />
			</g:link>
		</small>

		<g:render template="viewTypeButtons"></g:render>


		<g:if test="${flash.message}">
			<div class="alert alert-warning">${flash.message}</div>
		</g:if>

		<g:render template="reviewHeader"></g:render>

	<!-- Print the Diff of the considered file -->
		<r:script>
			prettyPrint();
		</r:script>

		<g:render template="/comment/commentsScript" model="['commentType': CommentType.LINE_OF_CODE.name(),
															 'review': reviewInstance]" />
		<g:javascript library="markdown"/>

		<sc:showDiffForCommit
				repo="${reviewInstance.repository}"
				commitID="${reviewInstance.commits[0]}"
				fileName="${fileName}"
				review="${reviewInstance}" />

	</div>

</div>
</body>
</html>
