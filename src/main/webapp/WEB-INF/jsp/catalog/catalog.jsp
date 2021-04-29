<!DOCTYPE html>
<%@page import="com.openkm.sdk4j.bean.Document"%>
<%@page import="com.openkm.aceptable.util.PathUtils"%>
<html lang="en">
<head>
<%@include file="../include/header.jsp"%>
<script type="text/javascript">
	function init() {
		$(".datetimepicker").datetimepicker({
			format : 'YYYY/MM/DD'
		});

		calculatePreviewHeight();
	}

	function calculatePreviewHeight() {
		$("#preview-doc").height( $(window).height() - $("footer").height() - $("nav").height() - $(".page-header").height()- 110);
	}
	
    function getName(path) {
        return path.replace(/^.*[\\\/]/, '');
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
		<c:choose>
			<c:when test="${total > 0}">
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
					<div class="row">
						<h3 class="catalog-type"><b>${type.name}</b></h3>
					</div>
					<form method="post">
						<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
						<input type="hidden" name="document_type"
							value="${type.metadataGroup}" /> <input type="hidden"
							name="document_uuid" value="${document.uuid}" />
						
						<div class="btn-group pull-right">							
							<button class="btn btn-danger" formaction="${pageContext.request.contextPath}/catalog/removeType?element=${document.uuid}">
								<span class="glyphicon glyphicon-remove" aria-hidden="true"></span> Change type
							</button>
							<button class="btn btn-primary"
								formaction="${pageContext.request.contextPath}/catalog/catalog">
								<span class="glyphicon glyphicon-save" aria-hidden="true"></span>
								Catalog
							</button>
						</div>
						<div class="row">
							<p>
								<span class="glyphicon glyphicon-list" aria-hidden="true"></span>
								Required data
							</p>
						</div>						
						<hr>
						<c:forEach var="element" items="${elements}">
							<div class="form-group row">
								<label for="${element.name}" class="col-lg-4 col-form-label">${element.label}</label>
								<div class="col-lg-8">																		
									<c:choose>									
										<c:when test="${element.getClass().name == 'com.openkm.sdk4j.bean.form.CheckBox'}">
											<input type="checkbox" name="${element.name}" value="true"/>
										</c:when>														
										<c:when test="${element.getClass().name == 'com.openkm.sdk4j.bean.form.TextArea'}">
											<textarea name="${element.name}" class="form-control"></textarea>
										</c:when>
										<c:when test="${element.getClass().name == 'com.openkm.sdk4j.bean.form.Select'}">
											<select class="form-control" name="${element.name}">
												<option value=""></option>
												<c:forEach var="option" items="${element.options}">
													<option value="${option.value}">
														${option.label}
													</option>
												</c:forEach>
											</select>
										</c:when>
										<c:when test="${element.getClass().name == 'com.openkm.sdk4j.bean.form.Input'}">																						
											<c:choose>						
												<c:when test="${'date' == element.type}">
													<div class="input-group date">
														<input type="text" class="form-control datetimepicker"
															name="${element.name}" /> <span class="input-group-addon">
															<span class="glyphicon glyphicon-calendar"></span>
														</span>
													</div>
												</c:when>												
												<c:otherwise>
													<input class="form-control" type="text"
														name="${element.name}" />
												</c:otherwise>	
											</c:choose>
										</c:when>
										<c:otherwise>
											<p>This type is not supported yet</p>
										</c:otherwise>											
									</c:choose>
								</div>
							</div>
						</c:forEach>
					</form>
					<br> <br>

				</div>
				<div class="col-lg-6">					
					<div class="row">										
						<p>
							<span class="glyphicon glyphicon-file" aria-hidden="true"></span>
							<b>${document.path}</b>
						</p>
					</div>
					<iframe id="preview-doc"
						style="width: 99%; border: 0px; height: 99%; display: inline;"
						src="${previewUrl}"></iframe>
				</div>
			</c:when>
			<c:otherwise>
				<p>There is no document to catalog</p>
			</c:otherwise>
		</c:choose>
	</div>
	<script type="text/javascript">
		window.onload = init;
		window.onresize = calculatePreviewHeight;	
	</script>
	<jsp:include page="../include/footer.jsp" />
</body>
</html>