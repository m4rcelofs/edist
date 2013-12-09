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

//		$('#requestsTab').on('show.bs.tab', RequestsViewModel.onRequestsShow);
//		$('#workTab').on('show.bs.tab', WorkViewModel.onWorkShow);
		
		crossroads.addRoute('reports/requests/all', RequestsViewModel.allRequests);
		crossroads.addRoute('reports/requests/date/:param:', RequestsViewModel.filterByDate);
		crossroads.addRoute('reports/requests/distributor/:param:', RequestsViewModel.filterByDistributor);
		crossroads.addRoute('reports/requests/group/:param:', RequestsViewModel.filterByGroup);
		crossroads.addRoute('reports/requests/player/:param:', RequestsViewModel.filterByPlayer);
		crossroads.addRoute('reports/requests/status/:param:', RequestsViewModel.filterByStatus);
		
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

/*
 * END OF FILE - /edist/eDist/war/reports/reports.js
 */

/*
 * START OF FILE - /edist/eDist/war/reports/requests.js
 */
var RequestsViewModel = new function() {
	var self = this;

	self.allRequests = function() {
		showTab();
		console.log('all');
		//TODO
	}
	
	self.filterByDate = function(param) {
		showTab();
		console.log('date');
		//TODO
	}
	
	self.filterByDistributor = function(param) {
		showTab();
		console.log('dist');
		//TODO
	}
	
	self.filterByGroup = function(param) {
		showTab();
		console.log('group');
		//TODO
	}
	
	self.filterByPlayer = function(param) {
		showTab();
		console.log('player');
		//TODO
	}
	
	self.filterByStatus = function(param) {
		showTab();
		console.log('status');
		//TODO
	}
	
	self.onRequestsShow = function() {
		//TODO
		console.log('onRequestsShow');
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
