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
	var version = edist.server.version;
	version = version.substring(0, version.lastIndexOf('.'));
	version = version.replace("--", " ");
	version = version.replace("-", ".");
	$('#appVersion').text(version);
});