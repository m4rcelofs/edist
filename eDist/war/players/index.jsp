<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>

<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<fmt:setLocale value="${language}" />
<fmt:setBundle basename="com.erep.dist.i18n.edist" />

<t:wrapper>

	<jsp:attribute name="imports">
		<script type="text/javascript">
			edist.stats = {
				day : '<fmt:message key="filters.dayPlaceholder"/>',
				enabled : '<fmt:message key="header.enabled"/>',
				active : '<fmt:message key="header.active"/>',
				total : '<fmt:message key="shared.total"/>',
				fighters : '<fmt:message key="header.fighters"/>',
				dead : '<fmt:message key="header.dead"/>',
				permaBan : '<fmt:message key="header.permaBan"/>',
				tempBan : '<fmt:message key="header.tempBan"/>',
				influence : '<fmt:message key="header.influence"/>'
			};
		</script>
		<script type='text/javascript'
			src='${baseUrl}/players/players.all.closure.js'></script>
		<script type="text/javascript"
			src="//www.google.com/jsapi?autoload=%7B%22modules%22%3A%5B%7B%22name%22%3A%22visualization%22%2C%22version%22%3A%221%22%2C%22packages%22%3A%5B%22corechart%22%5D%7D%5D%7D"></script>
    </jsp:attribute>

	<jsp:body>
		<div id="error-alert" class="alert alert-danger alert-dismissable"
			style="display: none;">
				<button type="button" class="close" aria-hidden="true"
				onclick="$('#error-alert').fadeOut();">&times;</button>
				<fmt:message key="admin.index.error" />
			</div>
	
			<ul class="nav nav-tabs">
				<%@ include file="tabs.jspf"%>
			</ul>

			<div class="tab-content">
				<%@ include file="statistics.jspf"%>
				<%@ include file="history.jspf"%>
			</div>
	</jsp:body>
</t:wrapper>

