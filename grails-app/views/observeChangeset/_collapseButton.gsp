<%--

    The button "collapse/expand" with script

--%>

<r:script>

    $("#collapse-button-id").click(function() {
        var thisBtn = $(this);
        thisBtn.find('span').toggleClass('glyphicon-chevron-left glyphicon-chevron-right');
        if(thisBtn.hasClass("active")) {
            $("#code-review-column-id").removeClass("col-md-12").addClass("col-md-9");
            $("#tree-files-id").show();
        }
        else {
            $("#code-review-column-id").removeClass("col-md-9").addClass("col-md-12");
            $("#tree-files-id").hide();
        }
    });

</r:script>

<button id="collapse-button-id" type="button" class="btn btn-default btn-xs" data-toggle="button" aria-pressed="false" autocomplete="off" title="${message(code:"collapse.expand.button.title")}">
    <span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
</button>