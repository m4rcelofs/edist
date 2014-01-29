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
		if (self.category() === 'fight' || self.category() === 'status'
				|| self.category() === 'work') {
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

	self.filterByStatus = function(param) {
		var searchParam = checkParam(param) ? param.toUpperCase() : 'NEW';
		reset('status', ManagementService.listPlayersByStatus, param,
				searchParam);
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
		groupIdsToGroups(self.newPlayer);
		var jsPlayer = ko.mapping.toJS(self.newPlayer);
		ManagementService.addPlayer(jsPlayer, {
			callback : function(response) {
				if (response) {
					$('#add-player-form').trigger('reset');
					self.newPlayer.dbStatus('NEW');
					self.players.unshift(self.newPlayer);
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
		var jsPlayer = ko.mapping.toJS(player);
		ManagementService.editPlayer(jsPlayer, {
			callback : function(response) {
				if (response) {
					$('#' + jsPlayer.encodedKey).removeClass('panel-danger');
					$('#' + jsPlayer.encodedKey).addClass('panel-success');

					setTimeout(function() {
						$('#' + jsPlayer.encodedKey).removeClass(
								'panel-success');
					}, 3000);
				} else {
					$('#' + jsPlayer.encodedKey).removeClass('panel-success');
					$('#' + jsPlayer.encodedKey).addClass('panel-danger');
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
		player.config(new DistConfig());
		self.editConfig(player);
	}

	self.removeConfig = function(player) {
		player.config(null);
	}

	self.editFightVerif = function(player) {
		FightVerificationViewModel.setFightVerif(player.encodedKey, player);
		$('#fightVerifDialog').modal('show');
	}

	self.addFightVerif = function(player) {
		player.fightVerif(new FightVerification());
		self.editFightVerif(player);
	}

	self.removeFightVerif = function(player) {
		player.fightVerif(null);
	}

	self.editWorkVerif = function(player) {
		WorkVerificationViewModel.setWorkVerif(player.encodedKey, player);
		$('#workVerifDialog').modal('show');
	}

	self.addWorkVerif = function(player) {
		player.workVerif(new WorkVerification());
		self.editWorkVerif(player);
	}

	self.removeWorkVerif = function(player) {
		player.workVerif(null);
	}

	self.clearFightStatus = function(player) {
		player.fightStatus(null);
	}

	self.clearWorkStatus = function(player) {
		player.workStatus(null);
	}

	self.enable = function(player) {
		player.dbStatus('ENABLED');
		self.clearFightStatus(player);
		self.clearWorkStatus(player);
	}

	self.disable = function(player) {
		player.dbStatus('DISABLED');
	}

	self.remove = function(player) {
		var jsPlayer = ko.mapping.toJS(player);
		ManagementService.removePlayer(jsPlayer, {
			callback : function(response) {
				if (response) {
					self.players.remove(player);
				} else {
					Management.defaultError();
				}
			},
			onError : Management.defaultError
		});
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

		var noRecursion = function(options) {
			// avoids recursive convertion
			return new ko.observable(options.data);
		};
		var mapping = {
			'config' : {
				create : noRecursion
			},
			'fightVerif' : {
				create : noRecursion
			},
			'workVerif' : {
				create : noRecursion
			}
		};
		var newArray = self.players();

		response.results.forEach(function(item) {
			newArray.push(ko.mapping.fromJS(item, mapping));
		});
		self.players(newArray);
	}

	function loadGroupNames() {
		ManagementService.listGroupNames({
			callback : function(response) {
				response.results.sort(Management.groupSortFct);
				self.groups(response.results);
			}
		});
	}

	function groupIdsToGroups(player) {
		var groups = [];
		if (player.groupIds() && player.groupIds().forEach) {
			player.groupIds().forEach(function(groupId) {
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
		crossroads.addRoute('management/playersList/status/:param:',
				PlayersViewModel.filterByStatus);
		crossroads.addRoute('management/playersList/work/:param:',
				PlayersViewModel.filterByWork);
	}
};