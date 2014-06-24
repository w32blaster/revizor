<%@ page import="com.revizor.User" %>
<%@ page import="com.revizor.ReviwerStatus" %>
<%@ page import="com.revizor.ReviewStatus" %>
<%@ page import="com.revizor.utils.Id" %>

<%-- 
    Buttons "Invite Reviewers" with javascript.
--%>
<g:set var="isAlreadyClosed" value="${reviewInstance.status == ReviewStatus.CLOSED}" />

<g:if test="${reviewInstance?.author?.id == session.user.id && !isAlreadyClosed}">

      <g:javascript>
          var lastChecked = -1;

          function selectUser(id) {
              if (-1 == lastChecked) {
                  $('#invite-reviewer-btn-id').removeAttr("disabled");
                  $('#invite-reviewer-caret-id').removeAttr("disabled");
              }
              lastChecked = id;
          }

          $('#invite-reviewer-btn-id').click(function(){
              if (-1 != lastChecked) {
                  var jqxhr = $.post( "${createLink(controller: 'review', action: 'inviteReviewer')}", { user: lastChecked, review: ${reviewInstance.id} })
                        .done(function(data) {
                          $('#${Id.REVIEWER_CONTAINER}').append(data);
                        })
                        .fail(function(data) {
                          alert(data.statusText + ": " + data.responseText);
                        })
                        .always(function() {
                          _changeStatusOfButtonGroup(resolution === "${ReviwerStatus.DISAPPROVE}");
                      });

              }
              return false;
          });

      </g:javascript>

      <div class="btn-group">
        <button id="invite-reviewer-btn-id" class="btn btn-default btn-info" disabled="disabled">
              <span class="glyphicon glyphicon-plus"></span> <g:message code="review.invite.reviwer"/>
        </button>
        <button id="invite-reviewer-caret-id" type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" disabled="disabled">
          <span class="caret"></span>
          <span class="sr-only">Toggle Dropdown</span>
        </button>
        <ul class="dropdown-menu" role="menu">
          <g:each in="${User.list()}" var="user">
              <g:if test="${user.id != session.user.id}">
                  <li role="presentation"><a href="#" onclick="selectUser(${user.id}); return false;">${user.username}</a></li>
              </g:if>
          </g:each>
        </ul>
      </div>

</g:if>