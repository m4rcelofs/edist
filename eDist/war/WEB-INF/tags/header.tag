<%@tag language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div class="header">
	<ul class="nav nav-pills pull-right">
		<li id="request"><a href="${baseUrl}/request/"><fmt:message key="header.form"/></a></li>
		<li id="admin" style="display: none;"><a href="${baseUrl}/admin/"><fmt:message key="header.admin"/></a></li>
		<li id="management" style="display: none;"><a href="${baseUrl}/management/"><fmt:message key="header.mod"/></a></li>
		<li id="reports" style="display: none;"><a href="${baseUrl}/reports/"><fmt:message key="header.reports"/></a></li>
		<li id="players" style="display: none;"><a href="${baseUrl}/players/"><fmt:message key="header.players"/></a></li>
		<li id="login" style="display: none;"><a href="#"><fmt:message key="header.login"/></a></li>
		<li id="logout" style="display: none;"><a href="#"><fmt:message key="header.logout"/></a></li>
	</ul>
	<h3 class="text-muted">eDist <span id="muNameHeader"></span></h3>
</div>