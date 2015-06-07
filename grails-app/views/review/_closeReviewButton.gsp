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

                    var $btn = $(this).button('toggle');
                    disableButtonWithLoading($btn);

					$.post("${createLink(controller: 'review', action: 'close')}", { review: ${reviewInstance.id} })
					  .done(function() {
					    	$("#${Id.REVIEW_STATUS}").text('<g:message code="${ReviewStatus.CLOSED.value()}" default="Closed" />');
					    	releaseDownloadedButton($btn, "<g:message code="review.is.already.closed" default="Review is closed" />", "ok", true);
					    	toastr.success('<g:message code="review.is.closed.successfully" default="Review was finished." />')

                            // hide the button "invite reviewers"
					    	$("#invite-reviewers-btn").hide();
					  })
					  .fail(function(data) {
					        $btn.addClass("btn-danger");
					        $btn.html("<span class='glyphicon glyphicon-exclamation-sign'></span> Error");
					        toastr.error("error: " + data.statusText)
					  });

				});
			});
		</g:javascript>

	</g:if>

	<button id="close-review-id" class="btn btn-default btn-primary btn-block" <%= isAlreadyClosed ? 'disabled="disabled"' : '' %>>
		<span class="glyphicon glyphicon-ok"></span>
        <g:if test="${isAlreadyClosed}">
            <g:message code="review.is.already.closed" default="Review is closed" />
        </g:if>
        <g:else>
            <g:message code="review.close" default="Close review" />
        </g:else>
	</button>

</g:if>