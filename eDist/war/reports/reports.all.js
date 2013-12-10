/*
 * JavaScript file created by Rockstarapps Concatenation
*/

/*
 * START OF FILE - /edist/eDist/war/reports/reports.js
 */
$(document).ready(
	function() {
		ko.applyBindings(RequestsViewModel, document.getElementById('requests'));
		ko.applyBindings(WorkViewModel, document.getElementById('work'));

		$('#workTab').on('show.bs.tab', WorkViewModel.onWorkShow);

		crossroads.addRoute('reports/requests', RequestsViewModel.initRequests);
		crossroads.addRoute('reports/requests/all', RequestsViewModel.allRequests);
		crossroads.addRoute('reports/requests/date/:param:', RequestsViewModel.filterByDate);
		crossroads.addRoute('reports/requests/distributor/:param:', RequestsViewModel.filterByDistributor);
		crossroads.addRoute('reports/requests/group/:param:', RequestsViewModel.filterByGroup);
		crossroads.addRoute('reports/requests/player/:param:', RequestsViewModel.filterByPlayer);
		crossroads.addRoute('reports/requests/status/:param:', RequestsViewModel.filterByStatus);
		crossroads.addRoute('reports/requests/type/:param:', RequestsViewModel.filterByType);
		
		crossroads.addRoute('reports/work', WorkViewModel.workTab);
		
		Navigation.configureRoutes('/reports/');

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

/*
 * END OF FILE - /edist/eDist/war/reports/reports.js
 */

/*
 * START OF FILE - /edist/eDist/war/reports/requests.js
 */
var RequestsViewModel = new function() {
	var self = this;

	self.category = ko.observable('');
	self.dists = ko.observableArray([]);
	self.cursor = ko.observable(null);

	ko.computed(function() {
		if (self.category()) {
			showTab();
		}
	});

	self.initRequests = function() {
		showTab();
	}

	self.allRequests = function() {
		self.category('all');

		// TODO

		ReportService.listRequests(self.cursor(), {
			callback : function(response) {
				console.log(response);
				self.cursor(response.encodedCursor);
				response.results.forEach(function(item) {
					self.dists.push(item);
				});
			}
		});
	}

	self.filterByDate = function(param) {
		self.category('date');

		// TODO
	}

	self.filterByDistributor = function(param) {
		self.category('distributor');

		// TODO
	}

	self.filterByGroup = function(param) {
		self.category('group');

		// TODO
	}

	self.filterByPlayer = function(param) {
		self.category('player');

		// TODO
	}

	self.filterByStatus = function(param) {
		self.category('status');

		// TODO
	}

	self.filterByType = function(param) {
		self.category('type');

		// TODO
	}

	self.requestedAt = function(item) {
		return formatDate(item.requestedAt);
	}

	self.completedAt = function(item) {
		return formatDate(item.completedAt);
	}

	function formatDate(date) {
		return date ? date.toLocaleString() : '';
	}

	function showTab() {
		$('#requestsTab').tab('show');
	}

};

/*
 * END OF FILE - /edist/eDist/war/reports/requests.js
 */

/*
 * START OF FILE - /edist/eDist/war/reports/work.js
 */
var WorkViewModel = new function() {
	var self = this;


	self.workTab = function(type) {
		console.log('work');
		$('#workTab').tab('show');
		//TODO
	}
	
	
	self.onWorkShow = function() {
		// TODO
		console.log('onWorkShow');
	}
};

/*
 * END OF FILE - /edist/eDist/war/reports/work.js
 */

/*
 * JavaScript file created by Rockstarapps Concatenation
*/
