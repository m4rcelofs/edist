$(document).ready(
		function() {

			Reports.downloadFilters();
			
			ko.applyBindings(RequestsViewModel, document.getElementById('requests'));
			ko.applyBindings(WorkViewModel, document.getElementById('work'));

			$('#workTab').on('show.bs.tab', WorkViewModel.onWorkShow);

			crossroads.addRoute('reports/requests', RequestsViewModel.initRequests);
			crossroads.addRoute('reports/requests/date/:param:', RequestsViewModel.filterByDate);
			crossroads.addRoute('reports/requests/day/:param:', RequestsViewModel.filterByDay);
			crossroads.addRoute('reports/requests/distributor/:param:', RequestsViewModel.filterByDistributor);
			crossroads.addRoute('reports/requests/group/:param:', RequestsViewModel.filterByGroup);
			crossroads.addRoute('reports/requests/player/:param:', RequestsViewModel.filterByPlayer);
			crossroads.addRoute('reports/requests/status/:param:', RequestsViewModel.filterByStatus);
			crossroads.addRoute('reports/requests/type/:param:', RequestsViewModel.filterByType);

			crossroads.addRoute('reports/work', WorkViewModel.workTab);

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

	self.downloadFilters = function() {
		ReportService.getInitialInfo({
			callback : function(response) {
				self.groups(response.groups);
				self.distributors(response.distributors);
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
};
