<%@ page import="com.revizor.utils.Constants; com.revizor.Review" %>
<%--
     Template renders two buttons: "single view" and "side by side view".
     It is intended to switch between different review modes
--%>
<div class="btn-group pull-right">

   <a href="${createLink(action: 'show', controller: 'review', id: params.id)}/${Constants.REVIEW_SINGLE_VIEW}?${Constants.PARAM_FILE_NAME}=${params[Constants.PARAM_FILE_NAME]}"
      class="btn btn-default btn-info btn-xs <g:if test="${Constants.REVIEW_SINGLE_VIEW.equals(params["viewType"])}">active</g:if>">
      <span class="glyphicon glyphicon-align-left"></span> <g:message code="single.view" />
   </a>

   <a href="${createLink(action: 'show', controller: 'review', id: params.id)}/${Constants.REVIEW_SIDE_BY_SIDE_VIEW}?${Constants.PARAM_FILE_NAME}=${params[Constants.PARAM_FILE_NAME]}"
      class="btn btn-default btn-info btn-xs <g:if test="${Constants.REVIEW_SIDE_BY_SIDE_VIEW.equals(params["viewType"])}">active</g:if>">
      <g:message code="side.by.side.view" /> <span class="glyphicon glyphicon-list"></span>
   </a>

</div>
