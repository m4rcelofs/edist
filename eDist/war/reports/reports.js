$(document).ready(
	function() {
		ko.applyBindings(RequestsViewModel, document.getElementById('requests'));
		ko.applyBindings(WorkViewModel, document.getElementById('work'));

		$('#requestsTab').on('show.bs.tab', RequestsViewModel.onRequestsShow);
		$('#workTab').on('show.bs.tab', WorkViewModel.onWorkShow);
		
		crossroads.addRoute('reports/requests/:type:', RequestsViewModel.requestsTab);
		crossroads.addRoute('reports/work/:type:', WorkViewModel.workTab);
		Navigation.configureRoutes();

		if (Navigation.getCurrenLocation() === '/reports/') {
			Navigation.setCurrentLocation('requests');
		}

		if (Navigation.getCurrenLocation() === '/reports') {
			Navigation.setCurrentLocation('reports/requests');
		}
	}
);

var Reports = new function() {
	var self = this;

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
