function HistoryFiltersViewModel() {

	var self = this;

	self.searchText = ko.observable();
	self.searchParam = ko.observable();
	self.searchDay = ko.observable();
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
		var searchText = "";

		switch (self.category()) {
		case 'group':
			searchText += self.filters.searchDay() + '/'
					+ self.filters.searchText();
			break;
		case 'day':
			searchText += self.filters.searchDay();
			break;
		case 'player':
			searchText += self.filters.searchText();
			break;
		case 'division':
			searchText += self.filters.searchDay() + '/'
					+ self.filters.searchParam();
			break;
		}

		Navigation.setCurrentLocation('/players/history/' + self.category()
				+ '/' + searchText);
	}

	self.loadNextPage = function() {
		if (self.cursor() !== null) {
			queryServer();
		}
	}

	self.filterByDay = function(param) {
		param = checkParam(param) ? param : (edist.server.eRepDay - 1);
		reset('day', PlayerReportService.statsByDay, param, param, param);
		queryServer();
	}

	self.filterByGroup = function(day, param) {
		day = checkParam(day) ? day : (edist.server.eRepDay - 1);
		reset('group', PlayerReportService.statsByGroup, param, {
			encodedKey : param
		}, day);
		queryServer();
	}

	self.filterByPlayer = function(param) {
		reset('player', PlayerReportService.statsByPlayer, param, param, '');
		queryServer();
	}

	self.filterByDivision = function(day, param) {
		day = checkParam(day) ? day : (edist.server.eRepDay - 1);
		reset('division', PlayerReportService.statsByDivision, param, param,
				day);
		queryServer();
	}

	function checkParam(param) {
		return param && param !== 'undefined' && param !== 'null' ? param
				: null;
	}

	function reset(category, searchFct, searchText, searchParam, searchDay) {
		self.statsHistory([]);
		self.cursor('');
		self.category(category);
		self.filters.searchFct = searchFct;
		self.filters.searchText(searchText);
		self.filters.searchParam(searchParam);
		self.filters.searchDay(searchDay);
	}

	function queryServer() {
		if (self.filters.searchParam()) {
			if ((self.category() === 'group' || self.category() === 'division') && self.filters.searchDay()) {
				self.filters.searchFct(self.filters.searchDay(), self.filters
						.searchParam(), self.cursor(), {
					callback : function(response) {
						setResults(response);
					}
				});
			} else {
				self.filters.searchFct(self.filters.searchParam(), self
						.cursor(), {
					callback : function(response) {
						setResults(response);
					}
				});
			}
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
		crossroads.addRoute('players/history/group/:day:/:param:',
				HistoryViewModel.filterByGroup);
		crossroads.addRoute('players/history/player/:param:',
				HistoryViewModel.filterByPlayer);
		crossroads.addRoute('players/history/division/:day:/:param:',
				HistoryViewModel.filterByDivision);
	}
};
