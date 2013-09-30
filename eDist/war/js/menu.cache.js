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

function loadRoutes() {
	$('.nav-tabs a').click(function(e) {
		e.preventDefault();
		hasher.setHash(this.hash);
	});
	
	crossroads.addRoute('{tab}/:query:', loadTab);

	hasher.initialized.add(parseHash);
	hasher.changed.add(parseHash);
	hasher.init();
}

function loadTab(tab, query) {
	if (console.log) {
		console.log('Tab: ' + tab + ' - ' + 'Query: ' + query);
	}
	$('#' + tab + 'Tab').tab('show');
}

function parseHash(newHash, oldHash) {
	crossroads.parse(newHash);
}