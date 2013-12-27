function FightFiltersViewModel() {

	var self = this;

	self.searchText = ko.observable();
	self.searchParam = ko.observable();
	self.searchFct = null;

	self.groups = Players.groups;
}

var FightsViewModel = new function() {
	var self = this;

	self.category = ko.observable('');

	self.fightsHistory = ko.observableArray([]);
	self.cursor = ko.observable(null);
	self.filters = new HistoryFiltersViewModel();

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
		Navigation.setCurrentLocation('/players/fights/' + self.category()
				+ '/' + searchText);
	}

	self.loadNextPage = function() {
		if (self.cursor() !== null) {
			queryServer();
		}
	}

	self.filterByDate = function(param) {
		reset('date', PlayerReportService.fightsByDate, param,
				checkParam(param) ? new Date(param) : null);
		queryServer();
	}

	self.filterByDay = function(param) {
		param = checkParam(param) ? param : edist.server.eRepDay;
		reset('day', PlayerReportService.fightsByDay, param, param);
		queryServer();
	}

	self.filterByGroup = function(param) {
		reset('group', PlayerReportService.fightsByGroup, param, {
			encodedKey : param
		});
		queryServer();
	}

	self.filterByPlayer = function(param) {
		reset('player', PlayerReportService.fightsByPlayer, param, param);
		queryServer();
	}

	self.fightDifference = function(today, yesterday) {
		var fightDifference = today.calculatedFfFights
				- yesterday.calculatedFfFights;
		return fightDifference < 0 ? '-' + fightDifference : '+'
				+ fightDifference;
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

		return (fights.calculatedFfFights / durability);
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
		if (Navigation.getCurrenLocation() === '/players/fights') {
			Navigation.setCurrentLocation('/players/fights/day');
		}
	}

};

var FightRoutes = new function() {
	var self = this;

	self.addRoutes = function() {
		crossroads.addRoute('players/fights', FightsViewModel.initFights);
		crossroads.addRoute('players/fights/date/:param:',
				FightsViewModel.filterByDate);
		crossroads.addRoute('players/fights/day/:param:',
				FightsViewModel.filterByDay);
		crossroads.addRoute('players/fights/group/:param:',
				FightsViewModel.filterByGroup);
		crossroads.addRoute('players/fights/player/:param:',
				FightsViewModel.filterByPlayer);
	}
};