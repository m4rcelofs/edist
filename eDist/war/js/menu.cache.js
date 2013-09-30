$(document).ready(function() {
	if (edist.user.loggedIn) {
		$('#logout').css("display", "");
		$('#logout > a').attr('href', edist.user.logoutURL);
	} else {
		$('#login').css("display", "");
		$('#login > a').attr('href', edist.user.loginURL);
	}

	if (edist.user.hasAdminAccess) {
		$('#admin').css("display", "");
	}

	if (edist.user.hasManagementAccess) {
		$('#management').css("display", "");
	}

	if (edist.user.hasReportsAccess) {
		$('#reports').css("display", "");
	}

	if (edist.user.hasPlayerReportsAccess) {
		$('#players').css("display", "");
	}

	var currentLoc = window.location.pathname.split('/')[1];
	$('#' + currentLoc).addClass('active');
});

function configureRoutes(section) {
	$('.nav-tabs a').click(function(event) {
		event.preventDefault();
		History.pushState(null, '', this.hash.replace('#', ''));
	});

	History.Adapter.bind(window, 'statechange', function() {
		doRouting(History.getState().hash.replace(section, ''));
	});

	doRouting(History.getState().hash.replace(section, ''));
}