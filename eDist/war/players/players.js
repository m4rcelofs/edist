$(document).ready(function() {

	Players.downloadFilters();

	ko.applyBindings(HistoryViewModel, document.getElementById('history'));

	HistoryRoutes.addRoutes();

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
		return item.stats ? getAvatar(item.stats) : getAvatar(item);
	}

	self.calculateDamage = function(todayRank, yesterdayRank) {
		return $.format.number((todayRank - yesterdayRank) * 10,
				edist.format.number);
	}

	function getAvatar(stats) {
		if (stats && stats.avatar) {
			if (stats.avatar.indexOf('142x142') !== -1) {
				// from eR
				return 'url(' + stats.avatar.replace('142x142', '55x55') + ')';
			} else {
				// from blobstore
				return 'url(' + stats.avatar + '=s55)';
			}
		} else {
			return null;
		}
	}
};