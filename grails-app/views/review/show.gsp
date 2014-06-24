
<%@ page import="com.revizor.Review" %>
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
        <nav class="navbar navbar-default" role="navigation">
            <ul class="nav navbar-nav">
                <li><a href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
                <li class="active"><g:link action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
            </ul>
        </nav>
        
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
            
                <h1>
                    <g:message code="default.show.label" args="[entityName]" />
                </h1>
                
                <g:if test="${flash.message}">
                    <div class="message" role="status">${flash.message}</div>
                </g:if>
                                
                <g:if test="${reviewInstance?.author?.image}">
                    <img height="64" width="64" class="avatar img-rounded" src="${createLink(controller:'user', action:'avatar_image', id:reviewInstance?.author?.ident())}" />
                </g:if>
                    
                <g:render template="resolutionButtons" model="['reviewInstance': reviewInstance]" />

                <g:render template="closeReviewButton" model="['reviewInstance': reviewInstance]" />
                    
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
                
                    <g:if test="${reviewInstance?.title}">
                        <dt>
                            <span id="title-label" class="property-label"><g:message code="review.title.label" default="Title" /></span>
                        </dt>
                        <dd>
                            <span class="property-value" aria-labelledby="title-label"><g:fieldValue bean="${reviewInstance}" field="title"/></span>
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
                
                    <g:if test="${reviewInstance?.description}">
                        <dt class="form-group">
                            <span id="description-label" class="property-label"><g:message code="review.description.label" default="Description" /></span>
                        </dt>
                        <dd>    
                            <span class="property-value" aria-labelledby="description-label"><g:fieldValue bean="${reviewInstance}" field="description"/></span>
                        </dd>
                    </g:if>
                

                    <dt class="form-group">
                        <span id="reviewers-label" class="property-label"><g:message code="review.reviewers.label" default="Reviewers" /></span>
                    </dt>
                    <dd>    
                            <div id="${Id.REVIEWER_CONTAINER}">
                            <g:each in="${reviewInstance.reviewers}" var="r">
                                <g:render template="reviewer" model="['reviewer' : r.reviewer, 'status': r.status]" />
                            </g:each>
                            </div>
                            
                            <%-- Buttons "Invite Reviewers" --%>
                            <g:render template="inviteReviewersButton" model="['reviewInstance': reviewInstance]" />
                    </dd>
                        
                </dl>


                <div class="panel panel-default" style="width: 800px;">
                    <div class="panel-body">
                        <g:form url="[resource:reviewInstance, action:'delete']" method="DELETE">
                            <fieldset class="buttons">
                                <g:link class="edit" action="edit" resource="${reviewInstance}">
                                    <g:message code="default.button.edit.label" default="Edit" />
                                </g:link>
                                <g:actionSubmit class="delete" action="delete"
                                    value="${message(code: 'default.button.delete.label', default: 'Delete')}"
                                    onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
                            </fieldset>
                        </g:form>
                    </div>
                </div>              
                
                <div class="row">   
                    <g:render template="/comment/comments" model="['commentType': CommentType.REVIEW]" />
                </div>
                
            </div>
            
            
        </div>
    </body>
</html>
