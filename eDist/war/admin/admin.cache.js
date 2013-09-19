$(document).ready(function() {
	$('.help').popover();

	$('#moderatorsTab').on('show.bs.tab', onModeratorsShow);
	$('#distributorsTab').on('show.bs.tab', onDistributorsShow);
	$('#configTab').on('show.bs.tab', onConfigShow);
	$('#controlPanelTab').on('show.bs.tab', onControlPanelShow);

	onModeratorsShow();
});

function onControlPanelShow() {
	var loc = 'https://appengine.google.com';
	if (edist.server.appId) {
		loc += '/dashboard?&app_id=s~' + edist.server.appId;
	}
	window.location.href = loc;
}

function defaultSuccess() {
	$('#error-alert').fadeOut();
}

function defaultError() {
	$('#error-alert').fadeIn();
}