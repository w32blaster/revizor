
<h1>${reviewInstance?.title}</h1>

<div class="row">

  	<g:if test="${reviewInstance?.author}">				
			
			<g:if test="${reviewInstance?.author?.image}">
 					<img height="64" width="64" class="avatar img-rounded" src="${createLink(controller:'user', action:'avatar_image', id:reviewInstance?.author?.ident())}" />
			</g:if>
			
			${reviewInstance?.author?.username.encodeAsHTML()}
				
		</g:if>
</div>