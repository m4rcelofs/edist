<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>

<t:wrapper>

	<jsp:attribute name="imports">
		<link href="../css/bootstrap-glyphicons.css" rel="stylesheet">
	
		<script type='text/javascript' src='dwr/engine.js'></script>
		<script type='text/javascript' src='dwr/interface/AdminService.js'></script>
		
		<script type='text/javascript' src='admin-mods.cache.js'></script>
		<script type='text/javascript' src='admin-dists.cache.js'></script>
		<script type='text/javascript' src='admin-config.cache.js'></script>
		<script type='text/javascript' src='admin.cache.js'></script>
	</jsp:attribute>

	<jsp:body>
			<div id="error-alert" class="alert alert-danger alert-dismissable"
			style="display: none;">
				<button type="button" class="close" aria-hidden="true"
				onclick="$('#error-alert').fadeOut();">&times;</button>
				<strong>Ops!</strong> Algo deu errado, por favor tente novamente. <strong>):</strong>
			</div>
	
			<ul class="nav nav-tabs">
				<%@ include file="tabs.jspf"%>
			</ul>

			<div class="tab-content">
				<%@ include file="mods.jspf"%>
				<%@ include file="dists.jspf"%>
				<%@ include file="config.jspf"%>
			</div>
	</jsp:body>
</t:wrapper>