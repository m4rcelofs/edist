function WorkFiltersViewModel() {

	var self = this;

	self.searchText = ko.observable();
	self.searchParam = ko.observable();
	self.searchFct = null;

	self.groups = Reports.groups;
	self.employers = Reports.employers;
}

var WorkViewModel = new function() {
	var self = this;

	self.category = ko.observable('');
	self.works = ko.observableArray([]);
	self.cursor = ko.observable(null);

	self.filters = new WorkFiltersViewModel();

	ko.computed(function() {
		if (self.category()) {
			showTab();
		}
	});

	self.initWork = function() {
		showTab();
	}

	self.doSearch = function() {
		var searchText = self.filters.searchText();
		Navigation.setCurrentLocation('/reports/work/' + self.category() + '/'
				+ searchText);
	}

	self.loadNextPage = function() {
		if (self.cursor() !== null) {
			queryServer();
		}
	}

	self.filterByDate = function(param) {
		reset('date', ReportService.listWorkByDate, param,
				checkParam(param) ? new Date(param) : null);
		queryServer();
	}

	self.filterByDay = function(param) {
		param = checkParam(param) ? param : edist.server.eRepDay;
		reset('day', ReportService.listWorkByDay, param, param);
		queryServer();
	}

	self.filterByEmployer = function(param) {
		reset('employer', ReportService.listWorkByEmployer, param, {
			encodedKey : param
		});
		queryServer();
	}

	self.filterByGroup = function(param) {
		reset('group', ReportService.listWorkByGroup, param, {
			encodedKey : param
		});
		queryServer();
	}

	self.filterByPlayer = function(param) {
		reset('player', ReportService.listWorkByPlayer, param, param);
		queryServer();
	}

	function checkParam(param) {
		return param && param !== 'undefined' && param !== 'null' ? param
				: null;
	}

	function reset(category, searchFct, searchText, searchParam) {
		self.works([]);
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
			self.works.push(item);
		});
	}

	function showTab() {
		$('#workTab').tab('show');
	}
};

var WorkRoutes = new function() {
	var self = this;

	self.addRoutes = function() {
		crossroads.addRoute('reports/work', WorkViewModel.initWork);
		crossroads.addRoute('reports/work/date/:param:',
				WorkViewModel.filterByDate);
		crossroads.addRoute('reports/work/day/:param:',
				WorkViewModel.filterByDay);
		crossroads.addRoute('reports/work/employer/:param:',
				WorkViewModel.filterByEmployer);
		crossroads.addRoute('reports/work/group/:param:',
				WorkViewModel.filterByGroup);
		crossroads.addRoute('reports/work/player/:param:',
				WorkViewModel.filterByPlayer);
	}
};
