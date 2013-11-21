/*
 * JavaScript file created by Rockstarapps Concatenation
*/

/*
 * START OF FILE - /edist/eDist/war/management/groups.js
 */
var GroupsViewModel = new function() {
	var self = this;

	self.groups = ko.observableArray([]);
	self.granularity = ko.observable();

	self.onGroupsShow = function() {
		loadGroupsTable();
	};

	self.saveGroup = function(group) {
		ManagementService.saveGroup(group, {
			callback : function(response) {
				if (response) {
					$('#' + group.encodedKey).removeClass('panel-danger');
					$('#' + group.encodedKey).addClass('panel-success');

					setTimeout(function() {
						$('#' + group.encodedKey).removeClass('panel-success');
					}, 3000);
				} else {
					$('#' + group.encodedKey).removeClass('panel-success');
					$('#' + group.encodedKey).addClass('panel-danger');
					Management.defaultError();
				}
			},
			onError : Management.defaultError
		});
	}

	self.addGroup = function() {
		var group = {
			groupName : $('#new-group-name').val()
		};
		ManagementService.saveGroup(group, {
			callback : function(response) {
				if (response) {
					$('#add-group-form').trigger('reset');
					loadGroupsTable();
				} else {
					Management.defaultError();
				}
			},
			onError : Management.defaultError
		});
	}

	self.editConfig = function(group) {
		DistConfigViewModel.setDistConfig(group.encodedKey, group);
		$('#distConfigDialog').modal('show');
	}

	self.addConfig = function(group) {
		group.config = new DistConfig();
		dirtyObservableArrayRefresh(self.groups);
		self.editConfig(group);
	}

	self.removeConfig = function(group) {
		group.config = null;
		dirtyObservableArrayRefresh(self.groups);
	}

	self.groupConfigEnabled = function() {
		return self.granularity() === 'GROUP'
				|| self.granularity() === 'PLAYER';
	}

	self.editFightVerif = function(group) {
		FightVerificationViewModel.setFightVerif(group.encodedKey, group);
		$('#fightVerifDialog').modal('show');
	}

	self.addFightVerif = function(group) {
		group.fightVerif = new FightVerification();
		dirtyObservableArrayRefresh(self.groups);
		self.editFightVerif(group);
	}

	self.removeFightVerif = function(group) {
		group.fightVerif = null;
		dirtyObservableArrayRefresh(self.groups);
	}

	self.editWorkVerif = function(group) {
		WorkVerificationViewModel.setWorkVerif(group.encodedKey, group);
		$('#workVerifDialog').modal('show');
	}

	self.addWorkVerif = function(group) {
		group.workVerif = new WorkVerification();
		dirtyObservableArrayRefresh(self.groups);
		self.editWorkVerif(group);
	}

	self.removeWorkVerif = function(group) {
		group.workVerif = null;
		dirtyObservableArrayRefresh(self.groups);
	}

	function dirtyObservableArrayRefresh(array) {
		// array.valueHasMutated(); doesn't work?
		var data = array().slice(0);
		array([]);
		array(data);
	}

	function loadGroupsTable() {
		self.groups([]);
		ManagementService.listGroups({
			callback : function(response) {
				response.results.sort(Management.groupSortFct);
				self.groups(response.results);
				self.granularity(response.granularity);				
			}
		});
	}
};

/*
 * END OF FILE - /edist/eDist/war/management/groups.js
 */

/*
 * START OF FILE - /edist/eDist/war/management/management.js
 */
$(document).ready(function() {
	
	ko.applyBindings(DistConfigViewModel, document.getElementById('distConfigDialog'));
	ko.applyBindings(FightVerificationViewModel, document.getElementById('fightVerifDialog'));
	ko.applyBindings(WorkVerificationViewModel, document.getElementById('workVerifDialog'));
	ko.applyBindings(GroupsViewModel, document.getElementById('groupsList'));

	$('#groupsListTab').on('show.bs.tab', GroupsViewModel.onGroupsShow);
	ko.applyBindings(PlayersViewModel, document.getElementById('playersList'));
	
	$('#playersListTab').on('show.bs.tab',PlayersViewModel.onPlayersShow);

	crossroads.addRoute('management/{tab}', Management.selectTab);
	Navigation.configureRoutes();
	
	if (Navigation.getCurrenLocation() === '/management/') {
		Navigation.setCurrentLocation('groupsList');
	}
});

var Management = new function() {

	var self = this;

	self.selectTab = function(tab) {
		$('#' + tab + 'Tab').tab('show');
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

	self.groupSortFct = function(a, b) {
		if (a.groupName === 'Default' || a.groupName < b.groupName) {
			return -1;
		}
		if (a.groupName > b.groupName) {
			return 1;
		}
		return 0;
	}
};
/*
 * END OF FILE - /edist/eDist/war/management/management.js
 */

/*
 * START OF FILE - /edist/eDist/war/management/players.js
 */
var PlayersViewModel = new function() {
	var self = this;

	self.players = ko.observableArray([]);
	self.groups = ko.observableArray([]);
	self.granularity = ko.observable();
	self.cursor = ko.observable();

	self.onPlayersShow = function() {
		loadGroupNames(loadPlayers);
	};

	self.addPlayer = function() {
		var player = new Player();
		player.erId = $('#new-player-id').val()

		ManagementService.addPlayer(player, {
			callback : function(response) {
				if (response) {
					$('#add-player-form').trigger('reset');

					player.dbStatus = 'NEW';
					self.players.push(player);
				} else {
					Management.defaultError();
				}
			},
			onError : Management.defaultError
		});
	}

	self.savePlayer = function(player) {
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

	function dirtyObservableArrayRefresh(array) {
		// array.valueHasMutated(); doesn't work?
		var data = array().slice(0);
		array([]);
		array(data);
	}

	function loadGroupNames(callback) {
		ManagementService.listGroupNames({
			callback : function(response) {
				response.results.sort(Management.groupSortFct);
				self.groups(response.results);
				callback();
			}
		});
	}

	function loadPlayers(cursor) {
		self.players([]);
		ManagementService.listPlayers(cursor, {
			callback : function(response) {
				self.players(response.results);
				self.granularity(response.granularity);
				self.cursor(response.encodedCursor);
			}
		});
	}
};
/*
 * END OF FILE - /edist/eDist/war/management/players.js
 */

/*
 * JavaScript file created by Rockstarapps Concatenation
*/
