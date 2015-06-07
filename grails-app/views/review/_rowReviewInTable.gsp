<%@ page import="revizor.HelpTagLib; com.revizor.ReviewStatus" %>
<%--
    Row in table, representing one Review
--%>
<tr <% if (isUnread) {%>class="unread" <%} %>>

    <%-- Mark new --%>
    <td width="32">
        <g:if test="${isUnread}">
            <span class="label label-danger" title="${message(code:"unread.review")}">N</span>
        </g:if>
    </td>

    <%-- Review Title --%>
    <td>
        <g:link action="show" controller="review" id="${review.ident()}">

            <g:if test="${review.status == com.revizor.ReviewStatus.CLOSED}">
                <del>${review.ident()}: ${fieldValue(bean: review, field: "title")}</del>
            </g:if>
            <g:else>
                ${review.ident()}: ${fieldValue(bean: review, field: "title")}
            </g:else>

        </g:link>
    </td>

    <%-- Repository Name --%>
    <td>
        <g:if test="${review?.repository.image}">
            <img height="16" width="16" class="avatar img-rounded" src="${createLink(controller:'repository', action: 'logo_image', id: review?.repository.ident())}" />
        </g:if>
        <span class="property-value" aria-labelledby="reviewers-label">
           ${review.repository.title}
        </span>
    </td>

    <%-- Author image --%>
    <td width="32">
        <g:render template="/user/userAvatar" model="['user' : review.author, 'size': 16]" />
    </td>

    <%-- If you are reviewer, then show the mark here --%>
    <td width="32">
        <g:if test="${revizor.HelpTagLib.isUserIsReviewer(session.user, review.reviewers)}">
            <span class="label label-primary" title="${message(code:"you.are.reviewer.in.this.review")}">R</span>
        </g:if>
    </td>

    <%-- Count of comments --%>
    <td width="64">
        <g:if test="${commentsByReview.containsKey(review.ident())}">
            <span class="glyphicon glyphicon-comment" aria-hidden="true"></span> ${commentsByReview.get(review.ident()).size()}
        </g:if>
        <g:else>
            <span class="faded">
                <span class="glyphicon glyphicon-comment" aria-hidden="true"></span> 0
            </span>
        </g:else>
    </td>

</tr>