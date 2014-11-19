<%@ page import="com.revizor.CommentType" %>
<%@ page import="com.revizor.Comment" %>

<%-- 
	The JS script that is used to add a new comments and show them dynamically.
	It is used by both Comments in a Review and Comments in a Line
--%>

<g:set var="formId" value="form-new-comment-id" />
<r:script>

	/**
	 * Hide and destroy a form "Add new comment"
	 */
	function closeForm(buttonIdToRestore, closeButton, idContainer) {
		$("#" + idContainer).empty().hide();
		closeButton.remove();
		$("#" + buttonIdToRestore).show();
	}

	function closeForm() {
		$('#${formId}').remove();
	};

	/**
	 * Show the form "Add new comment" under a selected line. It inserts the
	 * html code from the common GSP template.
	 */
	function showForm(idContainer, lineType, lineNo, commentContainerID, replyToCommentID, indent) {

		closeForm();

		var params = {
		 		commentType: "${commentType}",
                commit: "${review.commits[0]}",
                fileName: "${fileName}",
                reviewId: ${review.id},
                replyTo: replyToCommentID,
                lineNo: lineNo,
                lineType: lineType,
                indent: indent
		};

		$.ajax({
		   type: "GET",
		   url: "${createLink(controller: 'comment', action:'getAddNewFormLayout')}",
		   data: params,
		   success: function(formHtml)
		   {
				var container = $("#" + idContainer);
				container.show();
				container.get()[0].innerHTML = '<form id="${formId}" data-comment-container="' + commentContainerID + '">' + formHtml + '</form>';
				initMentionsInTextarea();
		   }
		 });
	}

	function initMentionsInTextarea() {
	<g:set var="allUsers" value="${com.revizor.User.getAll()}" />

		$('#${formId} .form-control').mention({
			delimiter: '~',
			users: [
		<g:each in="${allUsers}" var="user">
				{
					username: "${user.email}",
					name: "${user.username}",
					image: "${createLink(controller:'user', action:'avatar_image', id: user?.ident())}"
				}<g:if test='${user != allUsers.last()}'>,</g:if>
		</g:each>
			]
		});
	}

/**
 * Method will be fired by the "Add new comment" form submitting
 */
function createNewComment() {
    var form = $('#${formId}');
		var idCommentContainer = form.data("comment-container");

		$.ajax({
			   type: "POST",
			   url: "${createLink(controller: 'comment', action:'save')}.html",
			   data: form.serialize(),
			   success: function(data)
			   {
					var comment = $(data);
					$('#' + idCommentContainer).show().append(comment);
					form.remove();
			   }
			 });

		return false;
	}

	$('.mention-popover').popover();

</r:script>