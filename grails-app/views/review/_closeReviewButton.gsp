<%@ page import="com.revizor.ReviewStatus" %>
<%@ page import="com.revizor.utils.Id" %>

<%-- 
	Buttons "Close Review" with javascript.
--%>
<g:if test="${reviewInstance?.author?.id == session.user.id}">

	<g:set var="isAlreadyClosed" value="${reviewInstance.status == ReviewStatus.CLOSED}" />
	
	<g:if test="${!isAlreadyClosed}">

		<g:javascript>

			$( document ).ready(function() {
				
				$("#close-review-id").click(function(){
					var jqxhr = $.post("${createLink(controller: 'review', action: 'close')}", { review: ${reviewInstance.id} })
					  .done(function() {
					    	$("#${Id.REVIEW_STATUS}").text('<g:message code="${ReviewStatus.CLOSED.value()}" default="Closed" />');
					    	$("#close-review-id").attr("disabled", "disabled");
					  })
					  .fail(function(data) {
					    alert( "error: " + data.statusText );
					  });

				});
			});
		</g:javascript>
	</g:if>

	<button id="close-review-id" class="btn btn-default btn-primary btn-block" <%= isAlreadyClosed ? 'disabled="disabled"' : '' %>>
		<span class="glyphicon glyphicon-ok"></span> 
		<g:message code="review.close" default="Close review" />
	</button>

</g:if>