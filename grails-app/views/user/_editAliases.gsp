<%--

  The Form to update aliases

--%>
<g:javascript>

    /**
    * Calls ajax request to remove alias
    *
    * @param id
    * @param button
    */
    function deleteAlias(id, button) {
        var deleteAliasUrl = "${createLink(controller: 'alias', action: 'delete' )}/" + id;
        var rowId = "rowAlias-" + id + "-Id";

        var $btn = $(button).button('toggle');
        $btn.attr('disabled','disabled');
        $btn.children().toggleClass("glyphicon-refresh-animate");

        $.ajax({
            url: deleteAliasUrl,
            type: 'DELETE'
          }).done(function() {
                $('#' + rowId).remove();
            })
            .fail(function() {
                alert( "Error, can't delete alias" );
            });
    };

    /**
     * Calls Ajax request to create a new alias
     */
    function addAlias() {
        var newAlias = $("#newAliasEmail1").val();
        var addAliasUrl = "${createLink(controller: 'alias', action: 'createAjax' )}";

        $.ajax({
                type: "GET",
                url: addAliasUrl,
                data: { 'aliasEmail' : newAlias, 'user.id': ${userId} }
            })
            .done(function(alias) {
                var htmlRow = '<tr><td>' + alias.id + '</td>' +
                               '<td>' + alias.aliasEmail + '</td>' +
                               '<td><button type="button" class="btn btn-default" onclick="deleteAlias(' + alias.id + ', this);">' +
                               '<span class="glyphicon glyphicon-trash"></span></button>' +
                               '</td></tr>';
                $('#aliasesTableId tr:last').after(htmlRow);
                $('#newAliasEmail1').val('');
            })
            .fail(function() {
                alert( "Error, can't add new alias" );
            });

    };

</g:javascript>


<div class="panel panel-primary">
    <div class="panel-heading">
        <h3 class="panel-title">Panel primary</h3>
    </div>
    <div class="panel-body">

        <table id="aliasesTableId" class="table table-striped table-hover ">
            <thead>
            <tr>
                <th>#</th>
                <th>Alias</th>
                <th>#</th>
            </tr>
            </thead>
            <tbody>
                <g:each var="alias" in="${aliases.sort({ it.id })}">
                    <tr id="rowAlias-${alias.ident()}-Id">
                        <td>${alias.ident()}</td>
                        <td>${alias.aliasEmail}</td>
                        <td>
                            <button type="button" class="btn btn-default" onclick="deleteAlias(${alias.ident()}, this);">
                                <span class="glyphicon glyphicon-trash"></span>
                            </button>
                        </td>
                    </tr>
                </g:each>
            </tbody>
        </table>

        <input id="newAliasEmail1" type="email" class="form-control"  placeholder="Enter email">

        <button type="button" class="btn btn-default" onclick="addAlias();">
            <span class="glyphicon glyphicon-plus"></span>
        </button>

    </div>
</div>