<%@tag language="java" pageEncoding="UTF-8"%>
<%@tag import="com.erep.dist.erep.eRepTimeCalculator"%>
<%@tag import="com.google.appengine.api.utils.SystemProperty"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div class="footer">
	<p>
		<fmt:message key="footer.todayIs" />
		<span id="eRepDay">?</span>
	</p>
	<p>
		eDist <a id="appVersion"
			href="http://edist-licence.appspot.com/changelog"></a> - &copy; 2013,
		<a href="http://www.erepublik.com/en/citizen/profile/5313713">nW0lf</a>
		&
		<a href="http://www.erepublik.com/en/citizen/profile/3885795">Guilpas</a>
	</p>
	<p>
		<a
			href="https://bitbucket.org/marcelofs/edist/issues?status=new&status=open"><fmt:message
				key="footer.bugs" /></a> | <a href="https://edist-licence.appspot.com/"><fmt:message
				key="footer.homepage" /></a>
	</p>

</div>