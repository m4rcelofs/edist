/*
 * JavaScript file created by Rockstarapps Concatenation
*/

/*
 * START OF FILE - /edist/eDist/war/players/dwr-engine.js
 */

/*
 * END OF FILE - /edist/eDist/war/players/dwr-engine.js
 */

/*
 * START OF FILE - /edist/eDist/war/players/dwr-interface.js
 */

/*
 * END OF FILE - /edist/eDist/war/players/dwr-interface.js
 */

/*
 * START OF FILE - /edist/eDist/war/players/players.js
 */
$(document).ready(function() {

	Players.downloadFilters();

	ko.applyBindings(HistoryViewModel, document.getElementById('history'));
	// ko.applyBindings(FightsViewModel, document.getElementById('fights'));

	HistoryRoutes.addRoutes();
	// FightRoutes.addRoutes();

	Navigation.configureRoutes('/players/');

	if (Navigation.getCurrenLocation() === '/players/') {
		Navigation.setCurrentLocation('history/day');
	}

	if (Navigation.getCurrenLocation() === '/players') {
		Navigation.setCurrentLocation('players/history/day');
	}
});

var Players = new function() {
	var self = this;

	self.groups = ko.observableArray();

	self.downloadFilters = function() {
		PlayerReportService.getInitialInfo({
			callback : function(response) {
				self.groups(response.groups);
			}
		});
	}

	self.defaultSuccess = function() {
		$('#error-alert').fadeOut();
	}

	self.defaultError = function(message) {
		if (console.log) {
			console.log(message);
		}
		$('#error-alert').fadeIn();
	}

	self.avatar = function(item) {
		return item && item.playerInfo ? 'url('
				+ item.playerInfo.avatar.replace('142x142', '55x55') + ')'
				: null;
	}
};
/*
 * END OF FILE - /edist/eDist/war/players/players.js
 */

/*
 * START OF FILE - /edist/eDist/war/players/fights.js
 */

/*
 * END OF FILE - /edist/eDist/war/players/fights.js
 */

/*
 * START OF FILE - /edist/eDist/war/players/history.js
 */
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
		param = checkParam(param) ? param : edist.server.eRepDay;
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

	self.collectedAt = function(item) {
		return formatDate(item.collectedAt);
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

	function formatDate(date) {
		return date ? date.toLocaleString() : '';
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

/*
 * END OF FILE - /edist/eDist/war/players/history.js
 */

/*
 * JavaScript file created by Rockstarapps Concatenation
*/
