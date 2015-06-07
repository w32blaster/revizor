<%--
	Button "Refresh a repository" with javascript.
--%>

<g:javascript>
    (function($) {
        $(window).load(function(){

            var refreshRepositoryUrl = "${createLink(controller: 'repository', action: 'refreshRepository', id: repoId )}";

            $('#update-repository-button').on('click', function () {
                var $btn = $(this).button('toggle');
                $btn.attr('disabled','disabled');
                $btn.children().toggleClass("glyphicon-refresh-animate");

                // clean the repository container
                $('#repo-tree-id').empty();

                // make a query
                $.get( refreshRepositoryUrl )
                    .done(function(html) {
                        $('#repo-tree-id').append(html);
                        $('.graph-tooltip').popover();
                    })
                    .fail(function() {
                        alert( "Error, can't load updated tree" );
                    })
                    .always(function() {
                        $btn.children().toggleClass("glyphicon-refresh-animate");
                        $btn.removeAttr("disabled");
                        $btn.button('toggle');
                    });
            })
        });
    })(jQuery);
</g:javascript>

<button id="update-repository-button" class="btn btn-primary">
    <span class="glyphicon glyphicon-refresh"></span>
</button>