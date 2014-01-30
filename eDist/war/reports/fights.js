function FightFiltersViewModel() {

	var self = this;

	self.searchText = ko.observable();
	self.searchParam = ko.observable();
	self.searchFct = null;

	self.groups = Reports.groups;
}

var FightsViewModel = new function() {
	var self = this;

	self.category = ko.observable('');

	self.fightsHistory = ko.observableArray([]);
	self.cursor = ko.observable(null);
	self.filters = new FightFiltersViewModel();

	ko.computed(function() {
		if (self.category()) {
			showTab();
		}
	});

	self.initFights = function() {
		showTab();
	}

	self.doSearch = function() {
		var searchText = self.filters.searchText();
		Navigation.setCurrentLocation('/reports/fights/' + self.category()
				+ '/' + searchText);
	}

	self.loadNextPage = function() {
		if (self.cursor() !== null) {
			queryServer();
		}
	}

	self.filterByDate = function(param) {
		reset('date', ReportService.fightsByDate, param,
				checkParam(param) ? new Date(param) : null);
		queryServer();
	}

	self.filterByDay = function(param) {
		param = checkParam(param) ? param : (edist.server.eRepDay - 1);
		reset('day', ReportService.fightsByDay, param, param);
		queryServer();
	}

	self.filterByGroup = function(param) {
		reset('group', ReportService.fightsByGroup, param, {
			encodedKey : param
		});
		queryServer();
	}

	self.filterByPlayer = function(param) {
		reset('player', ReportService.fightsByPlayer, param, param);
		queryServer();
	}

	self.fightDifference = function(today, yesterday) {
		var fightDifference = today.calculatedFfFights
				- yesterday.calculatedFfFights;
		return fightDifference <= 0 ? $.format.number(fightDifference,
				edist.format.number) : '+'
				+ $.format.number(fightDifference, edist.format.number);
	}

	self.weaponsUsed = function(fights) {
		var quality = fights.calculatedWithWeaponQuality;
		var durability = 10;
		switch (quality) {
		case 'Q7':
			break;
		case 'Q6':
			durability = 6;
			break;
		case 'Q5':
			durability = 5;
			break;
		case 'Q4':
			durability = 4;
			break;
		case 'Q3':
			durability = 3;
			break;
		case 'Q2':
			durability = 2;
			break;
		case 'Q1':
			durability = 1;
			break;
		}

		return $.format.number(fights.calculatedFfFights / durability, edist.format.number);
	}

	function checkParam(param) {
		return param && param !== 'undefined' && param !== 'null' ? param
				: null;
	}

	function reset(category, searchFct, searchText, searchParam) {
		self.fightsHistory([]);
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
			self.fightsHistory.push(item);
		});
	}

	function showTab() {
		$('#fightTab').tab('show');
		if (Navigation.getCurrenLocation() === '/reports/fights') {
			Navigation.setCurrentLocation('/reports/fights/day');
		}
	}

};

var FightRoutes = new function() {
	var self = this;

	self.addRoutes = function() {
		crossroads.addRoute('reports/fights', FightsViewModel.initFights);
		crossroads.addRoute('reports/fights/date/:param:',
				FightsViewModel.filterByDate);
		crossroads.addRoute('reports/fights/day/:param:',
				FightsViewModel.filterByDay);
		crossroads.addRoute('reports/fights/group/:param:',
				FightsViewModel.filterByGroup);
		crossroads.addRoute('reports/fights/player/:param:',
				FightsViewModel.filterByPlayer);
	}
};