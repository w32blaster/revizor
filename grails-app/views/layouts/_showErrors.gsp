<%--
    Snippet shows the validation errors while saving attempt.
--%>
<g:hasErrors bean="${instance}">
    <div class="alert alert-dismissable alert-danger">
        <ul>
            <g:eachError bean="${instance}" var="error">
                <li><g:message error="${error}"/></li>
            </g:eachError>
        </ul>
    </div>
</g:hasErrors>