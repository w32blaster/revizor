<h4>${message(code: "review.with.title", args: [reviewInstance.title])}</h4>

<%-- The button "Expand/collapse comments" --%>
<button id="collapse-extend-btn-id" type="button" class="btn btn-default btn-xs pull-right" title="<g:message code="button.collapse.expand.comments" />">
    <span class="glyphicon glyphicon-resize-small"></span>
</button>

<r:script>
    $("#collapse-extend-btn-id").click(function() {
        var btn = $(this);
        btn.find('span').toggleClass("glyphicon-resize-small");
        btn.find('span').toggleClass("glyphicon-resize-full");

        $(".code-line-comments").each(function (i) {
            $(this).toggleClass("container-collapsed");
        });
    });

</r:script>