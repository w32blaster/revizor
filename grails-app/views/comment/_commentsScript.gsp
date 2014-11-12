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

                    /**
                     * Show the form "Add new comment" under a selected line. It inserts the
                     * html code from the common GSP template.
                     */
                    function showForm(button, idContainer, lineType, lineNo, commentContainerID, commentType) {
                    	debugger;
                    	var container = $("#" + idContainer);
                        container.show();

                        var closeBtnHtml = '<button class="btn btn-default btn-xs" onclick="closeForm(\'' + button.id + '\' ,this, \'' + idContainer + '\')"><span class="glyphicon glyphicon-remove"></span></button>';

						$(button).hide().parent().append($(closeBtnHtml));

						<%-- Those values, that are not yet available during GSP generation, we need to add on the fly in JS --%>

                        var additionalFieldHtml = lineNo ? '<input type="hidden" name="lineOfCode" value="' + lineNo + '" id="lineOfCode">' +
												'<input type="hidden" name="typeOfLine" value="' + lineType + '" id="typeOfLine">' : "";

                        var formHtml = '<h:renderInOneLine
                                            template="/comment/addNewCommentForm"
                                            model="[
                                                    'reviewId' : review.id,
                                                    'commentType' : commentType,
                                                    'commit' : review.commits[0],
                                                    'fileName' : fileName
                                            ]" />';

                        container.get()[0].innerHTML = '<form id="${formId}" data-comment-container="' + commentContainerID + '">' + additionalFieldHtml + formHtml + '</form>';
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

</r:script>