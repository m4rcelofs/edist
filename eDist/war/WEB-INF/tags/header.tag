<%@tag language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div id="language-select" class="dropdown pull-right">
	<button class="btn dropdown-toggle" type="button" id="dropdownMenu1"
		data-toggle="dropdown"
		title="<fmt:message key="header.languageSelect"/>">
		<span class="caret"></span>
	</button>
	<ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1">
		<li role="presentation"><a role="menuitem" tabindex="-1"
			onclick="Navigation.setLanguage('en');"> <img
				src="${baseUrl}/img/flags/us.png"> English
		</a></li>
		<li role="presentation"><a role="menuitem" tabindex="-1"
			onclick="Navigation.setLanguage('es');" title="by @Zebosk"> <img
				src="${baseUrl}/img/flags/es.png"> Español
		</a></li>
		<li role="presentation"><a role="menuitem" tabindex="-1"
			onclick="Navigation.setLanguage('pt');"> <img
				src="${baseUrl}/img/flags/br.png"> Português
		</a></li>
	</ul>
</div>

<div class="header">
	<ul class="nav nav-pills pull-right">
		<li id="request"><a href="${baseUrl}/request/"><fmt:message
					key="header.form" /></a></li>
		<li id="admin" style="display: none;"><a href="${baseUrl}/admin/"><fmt:message
					key="header.admin" /></a></li>
		<li id="management" style="display: none;"><a
			href="${baseUrl}/management/"><fmt:message key="header.mod" /></a></li>
		<li id="reports" style="display: none;"><a
			href="${baseUrl}/reports/"><fmt:message key="header.reports" /></a></li>
		<li id="players" style="display: none;"><a
			href="${baseUrl}/players/"><fmt:message key="header.players" /></a></li>
		<li id="login" style="display: none;"><a href="#"><fmt:message
					key="header.login" /></a></li>
		<li id="logout" style="display: none;"><a href="#"><fmt:message
					key="header.logout" /></a></li>
	</ul>
	<h1 class="sr-only">eDist</h1>
	<h2 class="text-primary hidden-xs">
		<a id="muNameHeader" href="${baseUrl}"></a>
	</h2>
</div>