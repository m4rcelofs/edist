<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>

<t:wrapper>

	<jsp:attribute name="imports">
		<script type='text/javascript' src='${baseUrl}/management/management.all.closure.js'></script>
		<script type='text/javascript' src='${baseUrl}/distconfig/distconfig.all.closure.js'></script>
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
				<%@ include file="groups.jspf"%>
				<%@ include file="players.jspf"%>
			</div>
			<%@ include file="../distconfig/dist-config-root.jspf"%>
	</jsp:body>
</t:wrapper>

