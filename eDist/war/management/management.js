$(document).ready(function() {
	
	ko.applyBindings(DistConfigViewModel, document.getElementById('distConfigDialog'));
	ko.applyBindings(FightVerificationViewModel, document.getElementById('fightVerifDialog'));
	ko.applyBindings(WorkVerificationViewModel, document.getElementById('workVerifDialog'));
	ko.applyBindings(GroupsViewModel, document.getElementById('groupsList'));
	ko.applyBindings(PlayersViewModel, document.getElementById('playersList'));
		
	$('#groupsListTab').on('show.bs.tab', GroupsViewModel.onGroupsShow);
	$('#playersListTab').on('show.bs.tab', PlayersViewModel.onPlayersShow);

	crossroads.addRoute('management/{tab}', Management.selectTab);
	Navigation.configureRoutes();
	
	if (Navigation.getCurrenLocation() === '/management/') {
		Navigation.setCurrentLocation('groupsList');
	}
	if (Navigation.getCurrenLocation() === '/management') {
		Navigation.setCurrentLocation('management/groupsList');
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