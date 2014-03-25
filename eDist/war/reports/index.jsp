<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>

<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<fmt:setLocale value="${language}" />
<fmt:setBundle basename="com.erep.dist.i18n.edist" />

<t:wrapper>

	<jsp:attribute name="imports">
		<script type='text/javascript' src='${baseUrl}/reports/work_custom.closure.js'></script>
		<script type='text/javascript' src='${baseUrl}/reports/reports.all.closure.js'></script>
		<script type="text/javascript" src="//www.google.com/jsapi?autoload=%7B%22modules%22%3A%5B%7B%22name%22%3A%22visualization%22%2C%22version%22%3A%221%22%2C%22packages%22%3A%5B%22corechart%22%5D%7D%5D%7D"></script>
    </jsp:attribute>

	<jsp:body>
			<div id="error-alert" class="alert alert-danger alert-dismissable"
			style="display: none;">
				<button type="button" class="close" aria-hidden="true"
				onclick="$('#error-alert').fadeOut();">&times;</button>
				<fmt:message key="admin.index.error"/>
			</div>
	
			<ul class="nav nav-tabs nav-justified">
				<%@ include file="tabs.jspf"%>
			</ul>

			<div class="tab-content">
				<%@ include file="statistics.jspf"%>
				<%@ include file="requests.jspf"%>
				<%@ include file="work.jspf"%>
				<%@ include file="fights.jspf"%>
			</div>
	</jsp:body>
</t:wrapper>

