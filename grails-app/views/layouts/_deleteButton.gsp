<%@ page import="revizor.HelpTagLib" %>

<%-- 
    	
--%>

<g:javascript>
    (function($) {
        $(document).ready(function(){
            $('#delete-btn').popover();
        });
    })(jQuery);

    function hidePopover() {
        $('#delete-btn').popover('hide')
    }
</g:javascript>

<%
    // Popuver body
    def template = """
            <div class="btn-group">
                <a href="${createLink(controller:'review', action:'delete', id:reviewInstance.ident())}" class="btn btn-default btn-danger btn-sm">
                    <span class="glyphicon glyphicon-trash"></span> Yes, delete it
                </a>
                <a href="#" class="btn btn-default btn-sm" onclick="hidePopover(); return false;">
                    <span class="glyphicon glyphicon-remove"></span> Cancel
                </a>
            </div>
        """;
%>

<button id="delete-btn" title data-original-title="Are you sure?" type="button" data-container="body" data-toggle="popover" data-html="true" data-content="<%= HelpTagLib.toSingleLine(template) %>" data-placement="right"  class="confirmation btn btn-default btn-xs">
    <span class="glyphicon glyphicon-trash"></span> 
</button>