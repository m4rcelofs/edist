<%@tag language="java" description="Default Wrapper"
	pageEncoding="UTF-8"%>

<%@tag import="com.google.appengine.api.utils.SystemProperty"%>
<%@tag import="com.erep.dist.erep.eRepTimeCalculator"%>
<%@tag import="com.google.appengine.api.users.User"%>
<%@tag import="com.google.appengine.api.users.UserService"%>
<%@tag import="com.google.appengine.api.users.UserServiceFactory"%>

<%@attribute name="imports" fragment="true"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="pt-BR">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="">

<title>eDist <%=SystemProperty.applicationVersion.get()%></title>

<%@ include file="baseimports.tag"%>
<jsp:invoke fragment="imports" />

</head>
<body>

	<div class="container-narrow">

		<%@ include file="header.tag"%>

		<div class="row new">
			<jsp:doBody />
		</div>

		<%@ include file="footer.tag"%>
	</div>
</body>
</html>