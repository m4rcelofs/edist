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
function RequestsFiltersViewModel(cat) {

	var category = cat;
	var self = this;

	self.searchText = ko.observable();
	self.searchParam = ko.observable();
	self.searchFct = null;

	self.showDateFilter = ko.computed(function() {
		return category() === 'date';
	});

	self.showDayFilter = ko.computed(function() {
		return category() === 'day';
	});

	self.showStatusFilter = ko.computed(function() {
		return category() === 'status';
	});

	self.showTypeFilter = ko.computed(function() {
		return category() === 'type';
	});

	self.showFilters = ko.computed(function() {
		return self.showDateFilter() || self.showDayFilter()
				|| self.showStatusFilter() || self.showTypeFilter();
	});

}

var RequestsViewModel = new function() {
	var self = this;

	self.category = ko.observable('');

	self.dists = ko.observableArray([]);
	self.cursor = ko.observable(null);
	self.filters = new RequestsFiltersViewModel(self.category);

	ko.computed(function() {
		if (self.category()) {
			showTab();
		}
	});

	self.initRequests = function() {
		showTab();
	}

	self.doSearch = function() {
		var searchText = self.filters.searchText();
		if (typeof searchText == 'string') {
			searchText = searchText.toLowerCase();
		}
		Navigation.setCurrentLocation('/reports/requests/' + self.category()
				+ '/' + searchText);
	}

	self.loadNextPage = function() {
		if (self.cursor() !== null) {
			queryServer();
		}
	}

	self.filterByDate = function(param) {
		reset('date', ReportService.listRequestsByDate, param,
				checkParam(param) ? new Date(param) : null);
		queryServer();
	}

	self.filterByDay = function(param) {
		param = checkParam(param) ? param : edist.server.eRepDay;
		reset('day', ReportService.listRequestsByDay, param, param);
		queryServer();
	}

	self.filterByDistributor = function(param) {
		// reset('distributor', ReportService.listRequestsByDistributor, param,
		// checkParam(param) ? param : edist.server.eRepDay);
		// queryServer();
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
		reset('status', ReportService.listRequestsByStatus, param,
				checkParam(param) ? param.toUpperCase() : 'REQUESTED');
		queryServer();
	}

	self.filterByType = function(param) {
		reset('type', ReportService.listRequestsByType, param,
				checkParam(param) ? param.toUpperCase() : 'WEAPONS');
		queryServer();
	}

	self.requestedAt = function(item) {
		return formatDate(item.requestedAt);
	}

	self.completedAt = function(item) {
		return formatDate(item.completedAt);
	}

	function checkParam(param) {
		return param && param !== 'undefined' && param !== 'null' ? param
				: null;
	}

	function reset(category, searchFct, searchText, searchParam) {
		self.dists([]);
		self.cursor('');
		self.category(category);
		self.filters.searchFct = searchFct;
		self.filters.searchText(searchText);
		self.filters.searchParam(searchParam);
	}

	function queryServer() {
		self.filters.searchFct(self.filters.searchParam(), self.cursor(), {
			callback : function(response) {
				console.log(response);
				setResults(response);
			}
		});
	}

	function setResults(response) {
		var cursor = response.results.length >= window.edist.server.pageSize ? response.encodedCursor
				: null
		self.cursor(cursor);
		response.results.forEach(function(item) {
			self.dists.push(item);
		});
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
