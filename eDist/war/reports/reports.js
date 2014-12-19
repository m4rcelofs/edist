$(document).ready(function() {

	Reports.downloadFilters();

	ko.applyBindings(StatisticsViewModel, document.getElementById('statistics'));
	ko.applyBindings(RequestsViewModel, document.getElementById('requests'));
	ko.applyBindings(WorkViewModel, document.getElementById('work'));
	ko.applyBindings(FightsViewModel, document.getElementById('fights'));

	StatisticsRoutes.addRoutes();
	RequestsRoutes.addRoutes();
	WorkRoutes.addRoutes();
	FightRoutes.addRoutes(); 

	Navigation.configureRoutes('/reports/');

	if (Navigation.getCurrenLocation() === '/reports/') {
		Navigation.setCurrentLocation('statistics');
	}

	if (Navigation.getCurrenLocation() === '/reports') {
		Navigation.setCurrentLocation('reports/statistics');
	}
});

var Reports = new function() {
	var self = this;

	self.groups = ko.observableArray();
	self.distributors = ko.observableArray();
	self.employers = ko.observableArray();

	self.downloadFilters = function() {
		ReportService.getInitialInfo({
			callback : function(response) {
				self.groups(response.groups);
				self.distributors(response.distributors);
				self.employers(response.employers);
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

	function getAvatar(stats) {
		if (stats && stats.avatar) {
			if (stats.avatar.indexOf('142x142') !== -1) {
				// from eR
				return 'url(' + stats.avatar + ')';
			} else {
				// from blobstore
				return 'url(' + stats.avatar + '=s55)';
			}
		} else {
			return null;
		}
	}
};
