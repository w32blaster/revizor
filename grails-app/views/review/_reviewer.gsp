<%@ page import="com.revizor.ReviwerStatus" %>

<%-- 
    One cell with author (avatar, his name and his decision)
--%>
<div class="reviewer">

    <div class="col-md-10">
        <g:render template="userWithAvatar" model="[user: reviewer]" />
    </div>

    <div class="col-md-2">
        <g:if test="${status == ReviwerStatus.INVITED}">
            <span id="reviewer-${reviewer.ident()}-status-icon-id" class="reviewer-status glyphicon glyphicon-time"></span>
        </g:if>
        <g:elseif test="${status == ReviwerStatus.APPROVE}">
            <span id="reviewer-${reviewer.ident()}-status-icon-id" class="reviewer-status glyphicon glyphicon-thumbs-up accepted"></span>
        </g:elseif>
        <g:elseif test="${status == ReviwerStatus.DISAPPROVE}">
            <span id="reviewer-${reviewer.ident()}-status-icon-id" class="reviewer-status glyphicon glyphicon-thumbs-down disaccepted"></span>
        </g:elseif>
    </div>
</div>