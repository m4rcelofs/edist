$(document).ready(function() {
	$('.help').popover();

	ko.applyBindings(DistConfigViewModel, document.getElementById('distConfigDialog'));
	
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