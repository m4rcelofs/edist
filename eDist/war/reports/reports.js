$(document).ready(function() {

	Reports.downloadFilters();

	ko.applyBindings(RequestsViewModel, document.getElementById('requests'));
	ko.applyBindings(WorkViewModel, document.getElementById('work'));

	RequestsRoutes.addRoutes();
	WorkRoutes.addRoutes();

	Navigation.configureRoutes('/reports/');

	if (Navigation.getCurrenLocation() === '/reports/') {
		Navigation.setCurrentLocation('requests/day');
	}

	if (Navigation.getCurrenLocation() === '/reports') {
		Navigation.setCurrentLocation('reports/requests/day');
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
		return item && item.playerInfo ? 'url('
				+ item.playerInfo.avatar.replace('142x142', '55x55') + ')'
				: null;
	}
};
