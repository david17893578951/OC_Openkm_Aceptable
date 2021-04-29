<!DOCTYPE html>
<%@page import="com.openkm.sdk4j.bean.Document"%>
<%@page import="com.openkm.aceptable.util.PathUtils"%>
<html lang="en">
<head>
<%@include file="../include/header.jsp"%>
<script type="text/javascript">
	//resize preview Iframe
   	function calculatePreviewHeight() {
		$("#preview-doc").height( $(window).height() - $("footer").height() - $("nav").height() - $(".page-header").height()- 110);
	}
</script>
</head>
<body>
	<c:set var="breadcrumView" value="public" scope="request" />
	<jsp:include page="../include/menu.jsp"/>
	<!-- Begin page content -->
	<div class="container-fluid">
		<!-- Title -->
		<div class="page-header">
			<c:choose>
				<c:when test="${total == 1}">
					<h1>There is ${total} pending document</h1>
				</c:when>
				<c:otherwise>
					<h1>There are ${total} pending documents</h1>
				</c:otherwise>
			</c:choose>
		</div>
		<div class="col-lg-3">
			<div class="panel panel-primary file-list">
				<div class="panel-heading"> <h3 class="panel-title">File list</h3>
					<p style="margin: 10px 0 0;">Cataloging process could take time in background <a class="btn btn-primary" alt="Refresh the list" title="Refresh the list" href="<c:url value="/catalog/next"/>">Refresh</a></p>
				</div>
				<div class="panel-body">
					<ul class="non-decorated">
						<c:forEach var="document" items="${documents}">
							<li class="vertical-gap">														
								<img src='${appUrl}/mime/${document.mimeType}' alt='' title='' /><a href="${pageContext.request.contextPath}/catalog/next?element=${document.uuid}">
									<%= PathUtils.shortenFileName(((Document)(pageContext.findAttribute("document"))).getPath(), 50) %>
								</a>
							</li>
						</c:forEach>
					</ul>
				</div>
			</div>
		</div>
		<div class="col-lg-3">			
			<form method="post">
				<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
				<input type="hidden" value="${doc.uuid}" name="document_uuid" />
				<label for="type_select" class="col-lg-3 col-form-label">Type</label>
				<div class="col-lg-9">
					<select name="type_select" class="form-control">
						<option value=""></option>
						<c:forEach var="type" items="${types}">
							<option value="${type.key}">${type.name}</option>
						</c:forEach>
					</select>
				</div>
				<br><br><br>				
				<div class="btn-group pull-right">
					<button class="btn btn-primary" formaction="${pageContext.request.contextPath}/catalog/assignType">
						<span class="glyphicon glyphicon-save" aria-hidden="true"></span> Select
					</button>
				</div>
			</form>
		</div>
		<div class="col-lg-6">		
			<p><span class="glyphicon glyphicon-file" aria-hidden="true"></span> <b>${doc.path}</b></p>	
			<iframe id="preview-doc" style="width: 99%; border: 0px; height: 99%; display: inline;" src="${previewUrl}"></iframe>
		</div>

	</div>
	<script type="text/javascript">
		window.onload = calculatePreviewHeight;	
		window.onresize = calculatePreviewHeight;
	</script>
	<jsp:include page="../include/footer.jsp" />
</body>
</html>