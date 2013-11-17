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
					$('#' + group.encodedKey).removeClass('danger');
					$('#' + group.encodedKey).addClass('success');

					setTimeout(function() {
						$('#' + group.encodedKey).removeClass('success');
					}, 3000);
				} else {
					$('#' + group.encodedKey).removeClass('success');
					$('#' + group.encodedKey).addClass('danger');
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
		ManagementService.listGroups({
			callback : function(response) {
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
	$('.help').popover();

	ko.applyBindings(DistConfigViewModel, document.getElementById('distConfigDialog'));
	ko.applyBindings(FightVerificationViewModel, document.getElementById('fightVerifDialog'));
	ko.applyBindings(WorkVerificationViewModel, document.getElementById('workVerifDialog'));
	
	ko.applyBindings(GroupsViewModel, document.getElementById('groupsList'));
	$('#groupsListTab').on('show.bs.tab', GroupsViewModel.onGroupsShow);

	// ko.applyBindings(PlayersViewModel);
	$('#playersListTab').on('show.bs.tab', PlayersController.onPlayersShow);
	
	crossroads.addRoute('management/{tab}', Management.selectTab);
	Navigation.configureRoutes();

	if (Navigation.getCurrenLocation() === '/management/') {
		Navigation.setCurrentLocation('groupsList');
	}
});

var Management = new function() {

	var mgm = this;

	mgm.selectTab = function(tab) {
		$('#' + tab + 'Tab').tab('show');
	}

	mgm.defaultSuccess = function() {
		$('#error-alert').fadeOut();
	}

	mgm.defaultError = function(message) {
		if (console.log) {
			console.log(message);
		}
		$('#error-alert').fadeIn();
	}

};
/*
 * END OF FILE - /edist/eDist/war/management/management.js
 */

/*
 * START OF FILE - /edist/eDist/war/management/players.js
 */
var PlayersController = new function() {
	var self = this;
	
	self.onPlayersShow = function() {
		console.log('onPlayersShow');
	};
	
};
/*
 * END OF FILE - /edist/eDist/war/management/players.js
 */

/*
 * JavaScript file created by Rockstarapps Concatenation
*/
