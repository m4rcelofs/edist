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