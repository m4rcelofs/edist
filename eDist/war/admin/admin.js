$(document).ready(function() {
	$('.help').popover();

	$('#usersTab').on('show.bs.tab', onModeratorsShow);
	$('#distributorsTab').on('show.bs.tab', onDistributorsShow);
	$('#employersTab').on('show.bs.tab', onEmployersShow);
	$('#configTab').on('show.bs.tab', onConfigShow);
	$('#controlPanelTab').on('show.bs.tab', onControlPanelShow);

	crossroads.addRoute('admin/{tab}', selectTab);
	configureRoutes();

	if (getCurrenLocation() === '/admin/') {
		setCurrentLocation('users'); 
	}
});

function selectTab(tab) {
	$('#' + tab + 'Tab').tab('show');
}

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

function defaultError(message) {
	if (console.log) {
		console.log(message);
	}
	$('#error-alert').fadeIn();
}