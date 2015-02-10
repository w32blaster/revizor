<%@ page import="com.revizor.ReviwerStatus" %>
<%@ page import="com.revizor.utils.Id" %>

<%-- 
	Buttons "Approve" and "Disaprove" with javascript.
--%>
<g:if test="${reviewInstance?.author?.id != session.user.id}">

    <g:set var="voted" value="${reviewInstance.findReviewerByUser(session.user)}" />
    <g:set var="isAccepted" value="${voted && voted.status == ReviwerStatus.APPROVE}" />
    <g:set var="isDeclined" value="${voted && voted.status == ReviwerStatus.DISAPPROVE}" />

	<r:script>

			/**
			 * Toggles the button group after the decision was made
			 */
			var _changeStatusOfButtonGroup = function(isAcceptedEnabled) {
				if (isAcceptedEnabled) {
					$("#approve-btn-id").removeAttr("disabled");
					$("#disapprove-btn-id").attr("disabled","disabled");
				}
				else {
					$("#approve-btn-id").attr("disabled", "disabled");
					$("#disapprove-btn-id").removeAttr("disabled");
				}
			};

			/**
			 * Update the icon from the user's decision
			 */
			var _rebuildTheListOfReviewers = function(wasAccepted) {
				var icon = $('#reviewer-${session.user.ident()}-status-icon-id');
				if (icon.length) {
					// user changes his decision
					icon.removeClass();
					if (wasAccepted === "${ReviwerStatus.APPROVE}") {
						icon.addClass("glyphicon glyphicon-thumbs-up accepted");
					}
					else {
						icon.addClass("glyphicon glyphicon-thumbs-down disaccepted");
					}
				}
				else {
					// user resolves this review in the first time
                    var reviewerHtml = '<h:renderInOneLine template="reviewer" model="['reviewer' : session.user, 
                    													'status': ReviwerStatus.APPROVE]" />';
					
					$('#${Id.REVIEWER_CONTAINER}').append(reviewerHtml);

					if (wasAccepted === "${ReviwerStatus.DISAPPROVE}") {
						_rebuildTheListOfReviewers(false);
					}
				}
			};

			/**
			 * Sends the request to finish the given review with any certain decision
			 */
			var _fnSendRequestWithResolution = function(resolution) {
				_changeStatusOfButtonGroup(false);

				$.post( "${createLink(controller: 'review', action: 'resolve')}", { status: resolution, review: ${reviewInstance.id} })
				  .done(function() {
				    toastr.success('<g:message code="your.resolution.was.saved.successfully" default="Your vote was saved" />')
				    _rebuildTheListOfReviewers(resolution);
				  })
				  .fail(function(data) {
				    toastr.error( "error: " + data.statusText );
				  })
				  .always(function() {
				    _changeStatusOfButtonGroup(resolution === "${ReviwerStatus.DISAPPROVE}");
				});
			};

			$("#approve-btn-id").click(function(){
				_fnSendRequestWithResolution("${ReviwerStatus.APPROVE}");
			});
			$("#disapprove-btn-id").click(function(){
				_fnSendRequestWithResolution("${ReviwerStatus.DISAPPROVE}");
			});
	</r:script>

	<div class="btn-group">
	  <button id="approve-btn-id" class="btn btn-default btn-success" <%= isAccepted ? 'disabled="disabled"' : '' %>>
	  		<span class="glyphicon glyphicon-thumbs-up"></span>
	  </button>
	  <button id="disapprove-btn-id" class="btn btn-default btn-danger"  <%= isDeclined ? 'disabled="disabled"' : '' %>>
	  		<span class="glyphicon glyphicon-thumbs-down"></span>
	  </button>
	</div>

</g:if>