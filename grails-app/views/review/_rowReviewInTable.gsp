<%@ page import="com.revizor.ReviewStatus" %>
<%--
    Row in table, representing one Review
--%>
<tr>
    <td>
        <g:link action="show" id="${review.ident()}">
            <% if (review.status == com.revizor.ReviewStatus.CLOSED) %> <del>

            ${fieldValue(bean: review, field: "title")}

            <% if (review.status == com.revizor.ReviewStatus.CLOSED) %> </del>

        </g:link>
    </td>

    <td>
        <g:if test="${review?.repository.image}">
            <img height="32" width="32" class="avatar img-rounded" src="${createLink(controller:'repository', action: 'logo_image', id: review?.repository.ident())}" />
        </g:if>
        <span class="property-value" aria-labelledby="reviewers-label">
           ${review.repository.title}
        </span>
    </td>

    <td><g:render template="/review/reviewer" model="['reviewer' : review.author]" /></td>

</tr>