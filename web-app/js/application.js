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