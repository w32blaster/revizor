<%@ page import="com.revizor.Review" %>


<g:javascript library="markdown"/>

<div class="form-group ${hasErrors(bean: reviewInstance, field: 'title', 'error')} ">

    <div class="row">
        <div class="col-lg-7">
            <label for="title">
                <g:message code="review.title.label" default="Title" />

            </label>
            <g:textField class="form-control" name="title" value="${reviewInstance?.title}"/>
        </div>
        <div class="col-lg-5">
            <label for="author">
                <g:message code="review.author.label" default="Author" />
            </label>
            <g:render name="author" template="reviewer" model="['reviewer' : reviewInstance?.author ? reviewInstance.author : session.user]" />
        </div>
    </div>
</div>


<div class="form-group required listOfCommits">
    <label for="commits">
        <g:message code="review.commits.label" default="Commits" />
    </label>
    <sc:selectCommitsForReview repo="${repository}"
                               selected="${params.selected}"
                               checkedItems="${reviewInstance?.commits}" />
</div>


<div class="form-group">
    <g:render template="issueTickets" model="['issueTickets': reviewInstance?.issueTickets, 'isEdit': isEdit, 'reviewId': reviewInstance?.ident()]" />
</div>

<div class="form-group ${hasErrors(bean: reviewInstance, field: 'description', 'error')} ">
	<label for="description">
		<g:message code="review.description.label" default="Description" />
	</label>
    <textarea data-provide="markdown" name="description" class="form-control" rows="5">${reviewInstance?.description}</textarea>
	
</div>
<%--
<div class="form-group ${hasErrors(bean: reviewInstance, field: 'reviewers', 'error')} ">
	<label for="reviewers">
		<g:message code="review.reviewers.label" default="Reviewers" />
	</label>
    <br />

	<g:select name="reviewers" from="${com.revizor.User.list()}" multiple="multiple" optionKey="id" size="5" value="${reviewInstance?.reviewers*.id}" class="many-to-many"/>
</div>
--%>

<!-- Will be removed with https://github.com/w32blaster/revizor/issues/3  -->
<div id="modal-many-commits-id" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog modal-sm">
        <div class="modal-content">
            <div class="modal-header">
                <button class="close" aria-label="Close" data-dismiss="modal" type="button">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 id="mySmallModalLabel" class="modal-title">Unsupported feature</h4>
            </div>
            <div class="modal-body">Sorry, but you can't select several commits,
            because this feature is not implemented yet.
            Please, <a href="https://github.com/w32blaster/revizor/issues/3" target="_blank">follow the issue ticket on GitHub</a>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>

<r:script>

        $('.commit-checkbox').change(function() {
            var len = $(".commit-checkbox:checked").length;
            if(len > 1) {
                $(this).prop('checked', false);
                $('#modal-many-commits-id').modal('show')
            }
        });

        $('.selectpicker').selectpicker();

</r:script>




