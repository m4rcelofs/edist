<%@ tag language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


<link href='//fonts.googleapis.com/css?family=Source+Sans+Pro' rel='stylesheet' type='text/css'>
<link href="//netdna.bootstrapcdn.com/bootstrap/3.0.2/css/bootstrap.min.css" rel="stylesheet">

<link href="${baseUrl}/css/jumbotron-narrow.cache.css" rel="stylesheet">
<link href="${baseUrl}/css/bootstrap-select.min.cache.css" rel="stylesheet">

<script type="text/javascript" src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
<script type="text/javascript" src="//netdna.bootstrapcdn.com/bootstrap/3.0.2/js/bootstrap.min.js"></script>

<script type="text/javascript" src="${baseUrl}/js/bootstrap-select.min.cache.js"></script>

<script type="text/javascript" src="${baseUrl}/js/jquery.history.min.cache.js"></script>
<script type="text/javascript" src="${baseUrl}/js/jquery.validate.min.cache.js"></script>
<script type='text/javascript' src='${baseUrl}/js/jquery.format.min.cache.js'></script>
<script type='text/javascript' src='${baseUrl}/js/jquery.cookie.min.cache.js'></script>

<script type="text/javascript" src="${baseUrl}/js/signals.min.cache.js"></script>
<script type="text/javascript" src="${baseUrl}/js/crossroads.min.cache.js"></script>

<script type="text/javascript" src="${baseUrl}/js/knockout.min.cache.js"></script>
<script type="text/javascript" src="${baseUrl}/js/knockout.mapping-2.4.1.cache.js"></script>

<script type="text/javascript" src="${baseUrl}/js/navigation.closure.js"></script>

<script type="text/javascript" src="${baseUrl}/js/dynamic/user.nocache.js"></script>
<script type="text/javascript" src="${baseUrl}/js/dynamic/header.nocache.js"></script>
<script type="text/javascript" src="${baseUrl}/js/dynamic/footer.nocache.js"></script>

<script type='text/javascript'>
$.format.locale({
    number: {
        groupingSeparator: '<fmt:message key="shared.groupingSeparator" />',
        decimalSeparator: '<fmt:message key="shared.decimalSeparator" />'
    }
});

window.edist.format = {
	number : '<fmt:message key="shared.numberFormat" />', 
	date : '<fmt:message key="shared.dateFormat" />',
	simpleDate : '<fmt:message key="shared.dateFormat.simple" />'
};
</script>

<script>
  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
  })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

  ga('create', 'UA-46284231-2', 'dist-eb.appspot.com');
  ga('send', 'pageview');
</script>