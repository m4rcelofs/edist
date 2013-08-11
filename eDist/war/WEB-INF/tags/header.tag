<%@tag language="java" pageEncoding="UTF-8"%>

<%@tag import="com.google.appengine.api.users.User"%>
<%@tag import="com.google.appengine.api.users.UserService"%>
<%@tag import="com.google.appengine.api.users.UserServiceFactory"%>


<%
	UserService userService = UserServiceFactory.getUserService();
	User user = userService.getCurrentUser();
%>

<div class="header">
	<p>
		<%
			if (user != null) {
		%>
		<%=user.getNickname()%>
		<%
			}
		%>
	</p>
	<ul class="nav nav-pills pull-right">
		<li class="active"><a href="#">Contato</a></li>
		<%
			if (!userService.isUserLoggedIn()) {
		%>
		<li><a
			href="<%=userService.createLoginURL(request.getRequestURI())%>">Log
				In</a></li>
		<%
			} else {
		%>
		<li><a href="#">Gestao</a></li>
		<li><a
			href="<%=userService.createLogoutURL(request.getRequestURI())%>">Log
				Out</a></li>
		<%
			}
		%>
	</ul>
	<h3 class="text-muted">Exército Brasileiro</h3>
</div>