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

/*
 * END OF FILE - /edist/eDist/war/reports/reports.js
 */

/*
 * START OF FILE - /edist/eDist/war/reports/requests.js
 */
var RequestsViewModel = new function() {
	var self = this;

	self.requestsTab = function(type) {
		console.log('requests: ' + type);
		$('#requestsTab').tab('show');
		//TODO
	}
	
	self.onRequestsShow = function() {
		//TODO
		console.log('onRequestsShow');
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
