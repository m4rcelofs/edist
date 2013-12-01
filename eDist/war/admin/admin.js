$(document).ready(function() {
	// $('.help').popover();

	ko.applyBindings(ModeratorsViewModel, document.getElementById('users'));
	ko.applyBindings(DistributorsViewModel, document.getElementById('distributors'));

	$('#usersTab').on('show.bs.tab', ModeratorsViewModel.onModeratorsShow);
	$('#distributorsTab').on('show.bs.tab', DistributorsViewModel.onDistributorsShow);
	
	// TODO
	// $('#employersTab').on('show.bs.tab', onEmployersShow);
	// $('#configTab').on('show.bs.tab', onConfigShow);
	$('#controlPanelTab').on('show.bs.tab', Admin.onControlPanelShow);

	crossroads.addRoute('admin/{tab}', Admin.selectTab);
	Navigation.configureRoutes();

	if (Navigation.getCurrenLocation() === '/admin/') {
		Navigation.setCurrentLocation('users');
	}
});

var Admin = new function() {
	var self = this;

	self.selectTab = function(tab) {
		$('#' + tab + 'Tab').tab('show');
	}

	self.defaultSuccess = function() {
		$('#error-alert').fadeOut();
	}

	self.defaultError = function(message) {
		if (console.log) {
			console.log(message);
		}
		$('#error-alert').fadeIn();
	}

	self.onControlPanelShow = function() {
		var loc = 'https://appengine.google.com';
		if (edist.server.appId) {
			loc += '/dashboard?&app_id=s~' + edist.server.appId;
		}
		window.location.href = loc;
	}

};
