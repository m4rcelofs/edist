function RequestsFiltersViewModel() {

	var self = this;

	self.searchText = ko.observable();
	self.searchParam = ko.observable();
	self.searchFct = null;

	self.groups = Reports.groups;
	self.distributors = Reports.distributors;
}

var RequestsViewModel = new function() {
	var self = this;

	self.category = ko.observable('');

	self.dists = ko.observableArray([]);
	self.cursor = ko.observable(null);
	self.filters = new RequestsFiltersViewModel();

	self.requestDetails = ko.observableArray();

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
		if (self.category() === 'status' || self.category() === 'type') {
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

	self.loadDetails = function(request) {
		ReportService.requestDetails({
			encodedKey : request.encodedKey
		}, {
			callback : function(response) {
				self.requestDetails(response);
				$('#requestDetails').modal('show');
			}
		});
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
		reset('distributor', ReportService.listRequestsByDistributor, param, {
			encodedKey : param
		});
		queryServer();
	}

	self.filterByGroup = function(param) {
		reset('group', ReportService.listRequestsByGroup, param, {
			encodedKey : param
		});
		queryServer();
	}

	self.filterByPlayer = function(param) {
		reset('player', ReportService.listRequestsByPlayer, param, param);
		queryServer();
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
		if (self.filters.searchParam()) {
			self.filters.searchFct(self.filters.searchParam(), self.cursor(), {
				callback : function(response) {
					setResults(response);
				}
			});
		}
	}

	function setResults(response) {
		var cursor = response.results.length >= window.edist.server.pageSize ? response.encodedCursor
				: null
		self.cursor(cursor);
		response.results.forEach(function(item) {
			self.dists.push(item);
		});
	}

	function showTab() {
		$('#requestsTab').tab('show');
	}

};

var RequestsRoutes = new function() {
	var self = this;

	self.addRoutes = function() {
		crossroads.addRoute('reports/requests', RequestsViewModel.initRequests);
		crossroads.addRoute('reports/requests/date/:param:',
				RequestsViewModel.filterByDate);
		crossroads.addRoute('reports/requests/day/:param:',
				RequestsViewModel.filterByDay);
		crossroads.addRoute('reports/requests/distributor/:param:',
				RequestsViewModel.filterByDistributor);
		crossroads.addRoute('reports/requests/group/:param:',
				RequestsViewModel.filterByGroup);
		crossroads.addRoute('reports/requests/player/:param:',
				RequestsViewModel.filterByPlayer);
		crossroads.addRoute('reports/requests/status/:param:',
				RequestsViewModel.filterByStatus);
		crossroads.addRoute('reports/requests/type/:param:',
				RequestsViewModel.filterByType);
	}
};
