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
						reviewId="${reviewInstance.id}"/>

			</div>
			
			<div class="col-md-9">

                  <small>
                      <g:link controller="review" action="show" params="[id: reviewInstance.id]" class="btn btn-default btn-success btn-xs">
                          <span class="glyphicon glyphicon-arrow-left"></span>
                          <g:message code="default.back.label" args="[entityName]" />
                      </g:link>
                  </small>
                    
                    <%-- 
                    
                    Is not implemented yet.
                    Issue: https://github.com/w32blaster/revizor/issues/8

				  	<g:render template="viewTypeButtons"></g:render>
				  	
                    --%>
				
				
				<g:if test="${flash.message}">
					<div class="alert alert-warning">${flash.message}</div>
				</g:if>
				
				<g:render template="reviewHeader"></g:render>
					
				<g:set var="formId" value="form-new-comment-id" />
                <g:javascript>

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
                    function showForm(button, idContainer, lineType, lineNo, commentContainerID) {
                    	var container = $("#" + idContainer);
                        container.show();

                        var closeBtnHtml = '<button class="btn btn-default btn-xs" onclick="closeForm(\'' + button.id + '\' ,this, \'' + idContainer + '\')"><span class="glyphicon glyphicon-remove"></span></button>';

						$(button).hide().parent().append($(closeBtnHtml));

						<%-- Those values, that are not yet available during GSP generation, we need to add on the fly in JS --%>
						var additionalFieldHtml = '<input type="hidden" name="lineOfCode" value="' + lineNo + '" id="lineOfCode">' +
												'<input type="hidden" name="typeOfLine" value="' + lineType + '" id="typeOfLine">';

                        var formHtml = '<h:renderInOneLine 
                        					template="/comment/addNewCommentForm" 
                        					model="[
                        						'reviewId' : reviewInstance.id, 
                        						'commentType' : CommentType.LINE_OF_CODE.name(),
                        						'commit' : reviewInstance.commits[0],
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

                    (function($) {
                        $(document).ready(function(){
                            prettyPrint();
                        });

                    })(jQuery);

                </g:javascript>

                <!-- Print the Diff of the considered file -->	
				<sc:showDiffForCommit 
                    repo="${reviewInstance.repository}" 
                    commitID="${reviewInstance.commits[0]}" 
                    fileName="${fileName}" 
                    review="${reviewInstance}" />

			</div>
			
		</div>
	</body>
</html>
