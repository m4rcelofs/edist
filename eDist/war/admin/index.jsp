<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:wrapper>

	<jsp:attribute name="imports">
		<script type='text/javascript' src='${baseUrl}/admin/admin-engine-20130921.cache.js'></script>
		<script type='text/javascript' src='${baseUrl}/admin/admin-interface-20130921.cache.js'></script>
		
		<script type='text/javascript' src='${baseUrl}/admin/admin.cache.js'></script>
		<script type='text/javascript' src='${baseUrl}/admin/admin-mods.cache.js'></script>
		<script type='text/javascript' src='${baseUrl}/admin/admin-dists.cache.js'></script>
		<script type='text/javascript' src='${baseUrl}/admin/admin-employers.cache.js'></script>
		<script type='text/javascript' src='${baseUrl}/admin/admin-config.cache.js'></script>
		<script type='text/javascript' src='${baseUrl}/distconfig/dist-config.cache.js'></script>
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
				<%@ include file="employers.jspf"%>
				<%@ include file="config.jspf"%>
			</div>
	</jsp:body>
</t:wrapper>