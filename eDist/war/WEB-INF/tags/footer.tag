<%@tag language="java" pageEncoding="UTF-8"%>
<%@tag import="com.erep.dist.erep.eRepTimeCalculator"%>
<%@tag import="com.google.appengine.api.utils.SystemProperty"%>

<div class="footer">
	<p>
		No eRepublik, hoje é dia
		<%=eRepTimeCalculator.geteRepDay()%></p>
	<p>
		eDist <%=SystemProperty.applicationVersion.get()%> - &copy; 2013, 
		<a href="http://www.erepublik.com/en/citizen/profile/3885795">Guilpas</a> e
		<a href="http://www.erepublik.com/en/citizen/profile/5313713">nW0lf</a>
	</p>
</div>