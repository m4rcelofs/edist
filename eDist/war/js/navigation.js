$(document).ready(function() {
	if (edist.user.loggedIn) {
		$('#logout').css("display", "");
		$('#logout > a').attr('href', edist.user.logoutURL);
	} else {
		$('#login').css("display", "");
		$('#login > a').attr('href', edist.user.loginURL);
	}

	if (edist.user.hasAdminAccess) {
		$('#admin').css("display", "");
	}

	if (edist.user.hasManagementAccess) {
		$('#management').css("display", "");
	}

	if (edist.user.hasReportsAccess) {
		$('#reports').css("display", "");
	}

	if (edist.user.hasPlayerReportsAccess) {
		$('#players').css("display", "");
	}

	var currentLoc = window.location.pathname.split('/')[1];
	$('#' + currentLoc).addClass('active');
	
	//binding handler for Bootstrap's Popup trigger on help icons
	ko.bindingHandlers.updatePopup = {
			update: function(element) {
				$(element).popover({
					trigger: 'hover'
				});
			}
	};
	
	//binding handler for bootstrap-select trigger
	ko.bindingHandlers.updateSelectPicker = {
			update: function(element) {
				$(element).selectpicker();
			}
	};
});

var Navigation = new function() {
	var nav = this;
	
	nav.configureRoutes = function(route) {
		$('.nav-tabs a').click(function(event) {
			event.preventDefault();
			History.pushState(null, '', this.hash.replace('#', ''));
		});

		History.Adapter.bind(window, 'statechange', function() {
			crossroads.parse(History.getState().hash);
		});

		crossroads.parse(History.getState().hash);
	}
	
	nav.setCurrentLocation = function(path) {
		History.pushState(null, '', path);  
	}

	nav.getCurrenLocation = function() {
		return History.getState().hash; 
	}
};