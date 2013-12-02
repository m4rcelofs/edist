$(document).ready(function() {
	
	ko.applyBindings(DistConfigViewModel, document.getElementById('distConfigDialog'));
	ko.applyBindings(FightVerificationViewModel, document.getElementById('fightVerifDialog'));
	ko.applyBindings(WorkVerificationViewModel, document.getElementById('workVerifDialog'));
	
	ko.applyBindings(ModeratorsViewModel, document.getElementById('users'));
	ko.applyBindings(DistributorsViewModel, document.getElementById('distributors'));
	ko.applyBindings(EmployersViewModel, document.getElementById('employers'));
	ko.applyBindings(ConfigViewModel, document.getElementById('config'));

	$('#usersTab').on('show.bs.tab', ModeratorsViewModel.onModeratorsShow);
	$('#distributorsTab').on('show.bs.tab', DistributorsViewModel.onDistributorsShow);
	$('#employersTab').on('show.bs.tab', EmployersViewModel.onEmployersShow);
	$('#configTab').on('show.bs.tab', ConfigViewModel.onConfigShow);
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
