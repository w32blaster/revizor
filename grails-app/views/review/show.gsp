
<%@ page import="com.revizor.ReviewFilter; com.revizor.Review" %>
<%@ page import="com.revizor.CommentType" %>
<%@ page import="com.revizor.ReviwerStatus" %>
<%@ page import="com.revizor.utils.Id" %>

<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <g:set var="entityName" value="${message(code: 'review.label', default: 'Review')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>

        <div class="row">
        
            <div class="col-md-3">
            
                <!-- The list of files -->
                <div class="panel panel-default">
                  <div class="panel-body">
                    <sc:showFilesForReview 
                        repo="${reviewInstance.repository}" 
                        commitID="${reviewInstance.commits[0]}" 
                        reviewId="${reviewInstance.id}"/>
                  </div>
                </div>
                
            </div>
            
            <div class="col-md-9">
            
                <div class="row">

                    <g:if test="${reviewInstance?.author?.id == session.user.id}">
                        <div class="btn-group">
                            <g:link action="edit" resource="${reviewInstance}" class="btn btn-default btn-xs">
                                <span class="glyphicon glyphicon-pencil"></span>
                            </g:link>

                            <g:render template="/layouts/deleteButton" />
                        </div>
                    </g:if>

                    <h1>                    
                        <small>
                            <g:link action="index" params="[filter: ReviewFilter.ONLY_MINE]" class="btn btn-default btn-success btn-xs">
                                <span class="glyphicon glyphicon-arrow-left"></span>  
                                <g:message code="default.list.label" args="[entityName]" />
                            </g:link>
                        </small>

                        <g:fieldValue bean="${reviewInstance}" field="title"/>

                    </h1>
                </div>

                <g:if test="${flash.message}">
                    <div class="message" role="status">${flash.message}</div>
                </g:if>
                       
                <div class="row">        
                    <div class="col-md-9">

                        <div class="row"> 
                            <div class="col-md-2">

                                <g:if test="${reviewInstance?.author?.image}">
                                    <img height="64" width="64" class="avatar img-rounded" src="${createLink(controller:'user', action:'avatar_image', id:reviewInstance?.author?.ident())}" />
                                </g:if>
                                    
                                
                            </div>   

                            <div class="col-md-10">
                                <dl class="dl-horizontal">

                                    <g:if test="${reviewInstance?.status}">
                                    
                                        <dt>
                                            <span id="status-label" class="property-label"><g:message code="review.status.label" default="Status" /></span>
                                        </dt>
                                        <dd>                            
                                            <span id="${Id.REVIEW_STATUS}" class="property-value" aria-labelledby="status-label">
                                                <g:message code="${reviewInstance?.status.value()}" default="Status" />
                                            </span>
                                        </dd>
                                    </g:if>
                                
                                    <g:if test="${reviewInstance?.repository}">
                                        <dt>
                                            <span id="repository-label" class="property-label"><g:message code="review.repository.label" default="Repository" /></span>
                                        </dt>
                                        <dd>    
                                            <span class="property-value" aria-labelledby="repository-label">${reviewInstance?.repository?.title}</span>
                                        </dd>
                                    </g:if>
                                
                                    <g:if test="${reviewInstance?.author}">
                                    
                                        <dt>
                                            <span id="author-label" class="property-label"><g:message code="review.author.label" default="Author" /></span>
                                        </dt>
                                        <dd>                            
                                            <span class="property-value" aria-labelledby="author-label">${reviewInstance?.author?.username}</span>
                                        </dd>
                                    </g:if>
                                
                                    <g:if test="${reviewInstance?.commits}">
                                        <dt>Commit count</dt>
                                        <dd>    
                                            <span id="commits-label" class="property-label">${reviewInstance?.commits.length}</span>                
                                        </dd>
                                    </g:if>
                                
                                </dl>
                            </div>

                            <div class="row">
                                <h3><g:message code="review.description.label" default="Description" /></h3>
                                <g:fieldValue bean="${reviewInstance}" field="description"/>
                            </div>
                        </div>
                    </div>                 
                    <div class="col-md-3">

                        <g:render template="resolutionButtons" model="['reviewInstance': reviewInstance]" />

                        <g:render template="closeReviewButton" model="['reviewInstance': reviewInstance]" />

                        <h3><g:message code="review.reviewers.label" default="Reviewers" /></h3>
                    
                            <div id="${Id.REVIEWER_CONTAINER}">
                            <g:each in="${reviewInstance.reviewers}" var="r">
                                <g:render template="reviewer" model="['reviewer' : r.reviewer, 'status': r.status]" />
                            </g:each>
                            </div>
                            
                            <%-- Buttons "Invite Reviewers" --%>
                            <g:render template="inviteReviewersButton" model="['reviewInstance': reviewInstance]" />
                    
                     
                </div>
                
                <div class="row">   
                    <g:render template="/comment/comments" model="['commentType': CommentType.REVIEW]" />
                </div>
                
            </div>
            
            
        </div>
    </body>
</html>
