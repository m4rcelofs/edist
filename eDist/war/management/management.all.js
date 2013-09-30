/*
 * JavaScript file created by Rockstarapps Concatenation
*/

/*
 * START OF FILE - /edist/eDist/war/management/groups.js
 */
var GroupsViewModel = new function() {
	var self = this;

	self.groups = ko.observableArray([]);

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
				if(response) {
					loadGroupsTable();
				} else {
					Management.defaultError();
				}
			}, 
			onError: Management.defaultError
		});
	}
	
	self.editConfig = function(group) {
		// TODO
	}

	function loadGroupsTable() {
		ManagementService.listGroups({
			callback : function(response) {
				self.groups(response);
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

	ko.applyBindings(GroupsViewModel);
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
