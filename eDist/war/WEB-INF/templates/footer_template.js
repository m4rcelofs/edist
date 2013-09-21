if (!window.edist) {
	window.edist = {};
}

window.edist.server = {
	appId : '{appId}',
	version : '{version}',
	eRepDay : {eRepDay},
	pageSize : {pageSize}
};

$(document).ready(function() {
	$('#eRepDay').text(edist.server.eRepDay);
	$('#appVersion').text(edist.server.version);
});