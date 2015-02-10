if (typeof jQuery !== 'undefined') {
	(function($) {
        $(document).ready(function(){

            $('#spinner').ajaxStart(function() {
                $(this).fadeIn();
            }).ajaxStop(function() {
                $(this).fadeOut();
            });

            $('#delete-btn').popover();
        });


	})(jQuery);
}

function disableButtonWithLoading(btn) {
    btn.attr('disabled','disabled');
    btn.html("<span class='glyphicon glyphicon-refresh glyphicon-refresh-animate'></span>");
}

function releaseDownloadedButton(btn, buttonLabel, icon, leaveClosed) {
    btn.html( (icon ? "<span class='glyphicon glyphicon-" + icon + "'></span> " : "") + buttonLabel);
    if (!leaveClosed) btn.removeAttr("disabled");
}