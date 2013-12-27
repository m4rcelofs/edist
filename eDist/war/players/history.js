function HistoryFiltersViewModel() {

	var self = this;

	self.searchText = ko.observable();
	self.searchParam = ko.observable();
	self.searchFct = null;

	self.groups = Players.groups;
}

var HistoryViewModel = new function() {
	var self = this;

	self.category = ko.observable('');

	self.statsHistory = ko.observableArray([]);
	self.cursor = ko.observable(null);
	self.filters = new HistoryFiltersViewModel();

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
		Navigation.setCurrentLocation('/players/history/' + self.category()
				+ '/' + searchText);
	}

	self.loadNextPage = function() {
		if (self.cursor() !== null) {
			queryServer();
		}
	}

	self.filterByDate = function(param) {
		reset('date', PlayerReportService.statsByDate, param,
				checkParam(param) ? new Date(param) : null);
		queryServer();
	}

	self.filterByDay = function(param) {
		param = checkParam(param) ? param : (edist.server.eRepDay - 1);
		reset('day', PlayerReportService.statsByDay, param, param);
		queryServer();
	}

	self.filterByGroup = function(param) {
		reset('group', PlayerReportService.statsByGroup, param, {
			encodedKey : param
		});
		queryServer();
	}

	self.filterByPlayer = function(param) {
		reset('player', PlayerReportService.statsByPlayer, param, param);
		queryServer();
	}

	function checkParam(param) {
		return param && param !== 'undefined' && param !== 'null' ? param
				: null;
	}

	function reset(category, searchFct, searchText, searchParam) {
		self.statsHistory([]);
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
			self.statsHistory.push(item);
		});
	}

	function showTab() {
		$('#historyTab').tab('show');
	}

};

var HistoryRoutes = new function() {
	var self = this;

	self.addRoutes = function() {
		crossroads.addRoute('players/history', HistoryViewModel.initRequests);
		crossroads.addRoute('players/history/date/:param:',
				HistoryViewModel.filterByDate);
		crossroads.addRoute('players/history/day/:param:',
				HistoryViewModel.filterByDay);
		crossroads.addRoute('players/history/group/:param:',
				HistoryViewModel.filterByGroup);
		crossroads.addRoute('players/history/player/:param:',
				HistoryViewModel.filterByPlayer);
	}
};
