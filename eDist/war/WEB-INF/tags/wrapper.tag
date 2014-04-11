<%@tag language="java" description="Default Wrapper"
	pageEncoding="UTF-8"%>

<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%@tag import="java.util.Date"%>
<%@tag import="com.erep.dist.instances.frontend.ServletHelper"%>

<%@attribute name="imports" fragment="true"%>

<%
	// 48h
	// https://groups.google.com/forum/#!topic/google-appengine/6xAV2Q5x8AU
	response.setDateHeader("Expires", new Date().getTime() + 172800000L);
	response.setHeader("Cache-Control", "public, max-age=172800000");
	response.setHeader("Pragma", "Public");

// 	response.setHeader("X-Frame-Options", "DENY");
	response.setHeader("Strict-Transport-Security",
			"max-age=172800000; includeSubDomains");
	response.setHeader("X-Content-Type-Options", "nosniff");
	response.setHeader("X-XSS-Protection", "1; mode=block");
	response.setHeader(
			"X-Content-Security-Policy",
			"default-src 'self'; options inline-script eval-script; "
					+ "script-src 'self' 'unsafe-inline' 'unsafe-eval' http://ajax.googleapis.com https://ajax.googleapis.com http://www.google.com https://www.google.com "
					+ "http://www.google-analytics.com https://www.google-analytics.com http://netdna.bootstrapcdn.com https://netdna.bootstrapcdn.com; "
					+ "font-src 'self' http://netdna.bootstrapcdn.com https://netdna.bootstrapcdn.com "
					+ "http://themes.googleusercontent.com https://themes.googleusercontent.com "
					+ "http://fonts.googleapis.com https://fonts.googleapis.com; "
					+ "media-src 'none'; object-src 'none'; xhr-src 'self'; connect-src 'self'; "
					+ "frame-src 'none'; frame-ancestors 'none'; "
					+ "style-src 'self' http://netdna.bootstrapcdn.com https://netdna.bootstrapcdn.com "
					+ "http://fonts.googleapis.com https://fonts.googleapis.com; "
					+ "img-src 'self' http://*.ggpht.com https://*.ggpht.com http://static.erepublik.net; "
					+ "report-uri /csp");

	request.setAttribute("baseUrl",
			new ServletHelper().getBaseUrl(request));
%>

<fmt:setLocale value="${language}" />
<fmt:setBundle basename="com.erep.dist.i18n.edist" />

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="${language}">
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description"
	content="eDist, distribution and management app for eRepublik military units">
<meta name="author" content="Guilpas and nW0lf">
<link rel="shortcut icon" href="${baseUrl}/favicon.ico"
	type="image/x-icon">
<link rel="icon" href="${baseUrl}/favicon.ico" type="image/x-icon">

<title>eDist</title>

<%@ include file="baseimports.tag"%>
<jsp:invoke fragment="imports" />

</head>
<body>
	<div class="container">

		<%@ include file="header.tag"%>

		<noscript>
			<div class="alert alert-danger text-center">
				<span class="glyphicon glyphicon-warning-sign"></span> <strong>
					<fmt:message key="header.noscript" />
				</strong>
			</div>
		</noscript>

		<div id="licence-warning" class="alert alert-warning text-center" style="display: none;">
			<span class="glyphicon glyphicon-warning-sign"></span> <strong>
				<fmt:message key="header.licenceExpired" /> 
			</strong>
		</div>

		<jsp:doBody />

		<%@ include file="footer.tag"%>
	</div>
</body>
</html>