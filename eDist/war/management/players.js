function PlayersFiltersViewModel() {

	var self = this;

	self.searchText = ko.observable();
	self.searchParam = ko.observable();
	self.searchFct = null;

}

var PlayersViewModel = new function() {
	var self = this;

	self.players = ko.observableArray([]);
	self.groups = ko.observableArray([]);
	self.granularity = ko.observable();
	self.playersCursor = ko.observable('');

	self.newPlayer = ko.mapping.fromJS(new Player());

	self.category = ko.observable();
	self.filters = new PlayersFiltersViewModel();

	ko.computed(function() {
		if (self.category()) {
			showTab();
		}
	});

	self.initPlayers = function() {
		showTab();
	}

	self.doSearch = function() {
		var searchText = self.filters.searchText();
		if (self.category() === 'fight' || self.category() === 'work') {
			searchText = searchText.toLowerCase();
		}
		Navigation.setCurrentLocation('/management/playersList/'
				+ self.category() + '/' + searchText);
	}

	self.loadNextPage = function() {
		if (self.playersCursor() !== null) {
			queryServer();
		}
	}

	self.showAll = function() {
		reset('all', ManagementService.listPlayers, '', '');
		queryServer();
	}

	self.filterByGroup = function(param) {
		reset('group', ManagementService.listPlayersByGroup, param,
				checkParam(param) ? {
					encodedKey : param
				} : null);
		queryServer();
	}

	self.filterByString = function(param) {
		reset('search', ManagementService.listPlayersByString, param, param);
		queryServer();
	}

	self.filterByFight = function(param) {
		var searchParam = checkParam(param) ? param.toUpperCase()
				: 'TEMP_BANNED';
		if (searchParam == 'UNKNOWN')
			searchParam = null;

		reset('fight', ManagementService.listPlayersByFight, param, searchParam);
		queryServer();
	}

	self.filterByWork = function(param) {
		var searchParam = checkParam(param) ? param.toUpperCase()
				: 'TEMP_BANNED';
		if (searchParam == 'UNKNOWN')
			searchParam = null;

		reset('work', ManagementService.listPlayersByWork, param, searchParam);
		queryServer();
	}

	self.addPlayer = function() {
		var player = ko.mapping.toJS(self.newPlayer);
		groupIdsToGroups(player);
		ManagementService.addPlayer(player, {
			callback : function(response) {
				if (response) {
					$('#add-player-form').trigger('reset');
					player.dbStatus = 'NEW';
					self.players.unshift(player);
				} else {
					Management.defaultError();
				}
			},
			onError : Management.defaultError
		});
	}

	self.savePlayer = function(player) {
		// converts back to PlayerGroup objects because of funny transforms
		groupIdsToGroups(player);
		ManagementService.editPlayer(player, {
			callback : function(response) {
				if (response) {
					$('#' + player.encodedKey).removeClass('panel-danger');
					$('#' + player.encodedKey).addClass('panel-success');

					setTimeout(
							function() {
								$('#' + player.encodedKey).removeClass(
										'panel-success');
							}, 3000);
				} else {
					$('#' + player.encodedKey).removeClass('panel-success');
					$('#' + player.encodedKey).addClass('panel-danger');
					Management.defaultError();
				}
			},
			onError : Management.defaultError
		});
	}

	self.playerConfigEnabled = function() {
		return self.granularity() === 'PLAYER';
	}

	self.editConfig = function(player) {
		DistConfigViewModel.setDistConfig(player.encodedKey, player);
		$('#distConfigDialog').modal('show');
	}

	self.addConfig = function(player) {
		player.config = new DistConfig();
		dirtyObservableArrayRefresh(self.players);
		self.editConfig(player);
	}

	self.removeConfig = function(player) {
		player.config = null;
		dirtyObservableArrayRefresh(self.players);
	}

	self.editFightVerif = function(player) {
		FightVerificationViewModel.setFightVerif(player.encodedKey, player);
		$('#fightVerifDialog').modal('show');
	}

	self.addFightVerif = function(player) {
		player.fightVerif = new FightVerification();
		dirtyObservableArrayRefresh(self.players);
		self.editFightVerif(player);
	}

	self.removeFightVerif = function(player) {
		player.fightVerif = null;
		dirtyObservableArrayRefresh(self.players);
	}

	self.editWorkVerif = function(player) {
		WorkVerificationViewModel.setWorkVerif(player.encodedKey, player);
		$('#workVerifDialog').modal('show');
	}

	self.addWorkVerif = function(player) {
		player.workVerif = new WorkVerification();
		dirtyObservableArrayRefresh(self.players);
		self.editWorkVerif(player);
	}

	self.removeWorkVerif = function(player) {
		player.workVerif = null;
		dirtyObservableArrayRefresh(self.players);
	}

	function checkParam(param) {
		return param && param !== 'undefined' && param !== 'null' ? param
				: null;
	}

	function reset(category, searchFct, searchText, searchParam) {
		self.players([]);
		self.playersCursor('');
		self.category(category);
		self.filters.searchFct = searchFct;
		self.filters.searchText(searchText);
		self.filters.searchParam(searchParam);
	}

	function queryServer() {
		if (self.category() == 'all' || self.category() == 'fight'
				|| self.category() == 'work' || self.filters.searchParam()) {
			self.filters.searchFct(self.filters.searchParam(), self
					.playersCursor(), {
				callback : function(response) {
					setResults(response);
				}
			});
		}
	}

	function setResults(response) {
		var cursor = response.results.length >= window.edist.server.pageSize ? response.encodedCursor
				: null
		self.playersCursor(cursor);
		self.granularity(response.granularity);
		response.results.forEach(function(item) {
			self.players.push(item);
		});
	}

	function dirtyObservableArrayRefresh(array) {
		// array.valueHasMutated(); doesn't work?
		var data = array().slice(0);
		array([]);
		array(data);
	}

	function loadGroupNames() {
		if (self.groups().length === 0) {
			ManagementService.listGroupNames({
				callback : function(response) {
					response.results.sort(Management.groupSortFct);
					self.groups(response.results);
				}
			});
		}
	}

	function groupIdsToGroups(player) {
		var groups = [];
		if (player.groupIds) {
			player.groupIds.forEach(function(groupId) {
				groups.push({
					encodedKey : groupId
				});
			});
		}
		player.groups = groups;
	}

	function showTab() {
		loadGroupNames();

		if (Navigation.getCurrenLocation() === '/management/playersList') {
			Navigation.setCurrentLocation('/management/playersList/search');
		}

		if (Navigation.getCurrenLocation() === '/management/playersList/') {
			Navigation.setCurrentLocation('/management/playersList/search');
		}

		$('#playersListTab').tab('show');
	}
};

var PlayersRoutes = new function() {
	var self = this;

	self.addRoutes = function() {
		crossroads.addRoute('management/playersList',
				PlayersViewModel.initPlayers);
		crossroads.addRoute('management/playersList/all',
				PlayersViewModel.showAll);
		crossroads.addRoute('management/playersList/date/:param:',
				PlayersViewModel.filterByDate);
		crossroads.addRoute('management/playersList/day/:param:',
				PlayersViewModel.filterByDay);
		crossroads.addRoute('management/playersList/group/:param:',
				PlayersViewModel.filterByGroup);
		crossroads.addRoute('management/playersList/search/:param:',
				PlayersViewModel.filterByString);
		crossroads.addRoute('management/playersList/fight/:param:',
				PlayersViewModel.filterByFight);
		crossroads.addRoute('management/playersList/work/:param:',
				PlayersViewModel.filterByWork);
	}
};