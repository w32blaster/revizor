<%@ page import="com.revizor.ReviewFilter" %>
<!--

    Group of buttons with filters for reviews

-->
<div class="btn-group">


    <!-- My reviews, where I am as an author -->
    <% def cssClassMy = (params.filter == ReviewFilter.ONLY_MINE.toString()) ? 'active' : '' %>
    <g:link action="index" params="[filter: ReviewFilter.ONLY_MINE]" class="btn btn-default btn-primary ${cssClassMy}">
        <span class="glyphicon glyphicon-pencil"></span>
        <g:message code="reviews.only.mine" default="My reviews" />
    </g:link>

    <!-- My invitations and reviews where I am a reviewer -->
    <% def cssClassInv = (params.filter == ReviewFilter.WHERE_I_AM_REVIEWER.toString()) ? 'active' : '' %>
    <g:link action="index" params="[filter: ReviewFilter.WHERE_I_AM_REVIEWER]" class="btn btn-default btn-primary ${cssClassInv}">
        <span class="glyphicon glyphicon-thumbs-up"></span>
        <g:message code="reviews.where.i.reviewer" default="My inspections" />
    </g:link>



    <!-- Finished (archived) reviews -->
    <% def cssClassArch = (params.filter == ReviewFilter.ARCHIVED.toString()) ? 'active' : '' %>
    <g:link action="index" params="[filter: ReviewFilter.ARCHIVED]" class="btn btn-default btn-primary ${cssClassArch}">
        <span class="glyphicon glyphicon-inbox"></span>
        <g:message code="reviews.archived" default="My reviews" />
    </g:link>



    <!-- reviews grouped by Issue Ticket -->
    <g:if test="${com.revizor.IssueTracker.all.size() > 0}">

        <% def cssClassGrouped = (params.filter == ReviewFilter.GROUPED_BY_ISSUE_TICKETS.toString()) ? 'active' : '' %>
        <g:link action="index" params="[filter: ReviewFilter.GROUPED_BY_ISSUE_TICKETS]" class="btn btn-default btn-primary ${cssClassGrouped}">
            <span class="glyphicon glyphicon-tags"></span>
            <g:message code="reviews.grouped.by.issue.tickets" default="by Isssue Tickets" />
        </g:link>

    </g:if>
    <g:else>
        <span id="disabled-button-group-by-issues" rel="tooltip" data-toggle="tooltip" data-placement="top"
                data-title="${message(code: 'no.issue.trackers.found')}"
                style="float: left; position: relative;">
            <button class="btn btn-default btn-primary" style="border-radius: 0;" disabled="disabled">
                <span class="glyphicon glyphicon-tags"></span>
                <g:message code="reviews.grouped.by.issue.tickets" default="by Isssue Tickets" />
            </button>
        </span>
        <r:script>
            (function($) {
                $('#disabled-button-group-by-issues').tooltip();
            })(jQuery);
        </r:script>

    </g:else>


    <!-- All reviews -->
    <% def cssClassAll = (params.filter == ReviewFilter.ALL.toString()) ? 'active' : '' %>
    <g:link action="index" params="[filter: ReviewFilter.ALL]" class="btn btn-default btn-primary ${cssClassAll}">
        <g:message code="reviews.all" default="All reviews" />
    </g:link>
</div>
