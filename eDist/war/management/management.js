$(document).ready(function() {
	
	ko.applyBindings(DistConfigViewModel, document.getElementById('distConfigDialog'));
	ko.applyBindings(FightVerificationViewModel, document.getElementById('fightVerifDialog'));
	ko.applyBindings(WorkVerificationViewModel, document.getElementById('workVerifDialog'));
	ko.applyBindings(GroupsViewModel, document.getElementById('listGroups'));
	ko.applyBindings(PlayersViewModel, document.getElementById('playersList'));
		
	$('#listGroupsTab').on('show.bs.tab', GroupsViewModel.onGroupsShow);

	crossroads.addRoute('management/listGroups', Management.selectTab);
	PlayersRoutes.addRoutes();
	
	
	Navigation.configureRoutes('/management/');
	
	if (Navigation.getCurrenLocation() === '/management/') {
		Navigation.setCurrentLocation('listGroups');
	}
	if (Navigation.getCurrenLocation() === '/management') {
		Navigation.setCurrentLocation('management/listGroups');
	} 
});

var Management = new function() {

	var self = this;

	self.selectTab = function() {
		$('#listGroupsTab').tab('show');
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