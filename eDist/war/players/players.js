$(document).ready(function() {

	Players.downloadFilters();

	ko.applyBindings(HistoryViewModel, document.getElementById('history'));
	ko.applyBindings(FightsViewModel, document.getElementById('fights'));

	HistoryRoutes.addRoutes();
	FightRoutes.addRoutes();

	Navigation.configureRoutes('/players/');

	if (Navigation.getCurrenLocation() === '/players/') {
		Navigation.setCurrentLocation('history/day');
	}

	if (Navigation.getCurrenLocation() === '/players') {
		Navigation.setCurrentLocation('players/history/day');
	}
});

var Players = new function() {
	var self = this;

	self.groups = ko.observableArray();

	self.downloadFilters = function() {
		PlayerReportService.getInitialInfo({
			callback : function(response) {
				self.groups(response.groups);
			}
		});
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

	self.avatar = function(item) {
		if (item && item.stats) {
			if (item.stats.indexOf('142x142') !== -1) {
				// getting from eR
				return 'url(' + item.stats.avatar.replace('142x142', '55x55')
						+ ')';
			} else {
				return 'url(' + item.stats.avatar + '=s55)';
			}
		}
	}
};