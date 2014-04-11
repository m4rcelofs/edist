$(document).ready(function() {
	
	if(edist.licence && edist.licence.hasExpired) {
		$('#licence-warning').css("display", "");
	}
	
	if (edist.user.loggedIn) {
		$('#logout').css("display", "");
	} else {
		$('#login').css("display", "");
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

	// binding handler for Bootstrap's Popup trigger on help icons
	ko.bindingHandlers.updatePopup = {
		update : function(element) {
			$(element).popover({
				trigger : 'hover'
			});
		}
	};

	// binding handler for bootstrap-select trigger
	ko.bindingHandlers.updateSelectPicker = {
		update : function(element, valueAccessor) {
			$(element).selectpicker();

			// this is necessary to force ko's binding
			if (ko.unwrap(valueAccessor())) {
				$(element).selectpicker('refresh');
			}
		}
	};

	// loads bigger avatar
	ko.bindingHandlers.avatarEnhance = {
		update : function(element, valueAccessor) {
			$(element).click(function() {
				if(!$(element).attr('avatar-loaded')) {
					var newStyle = $(element).attr('style');
					newStyle = newStyle.replace('55x55', '142x142');
					newStyle = newStyle.replace('=s55', '=s142');
					$(element).attr('style', newStyle);
					$(element).attr('avatar-loaded', true);
				}
				$(element).toggleClass('player-avatar-enhanced');
			});
		}
	};
});

var Navigation = new function() {
	var nav = this;

	nav.configureRoutes = function(route) {
		$('.nav-tabs a').click(function(event) {
			event.preventDefault();
			var nav = '';
			if (route) {
				nav += route;
			}
			nav += this.hash.replace('#', '');
			History.pushState(null, '', nav);
		});

		History.Adapter.bind(window, 'statechange', function() {
			crossroads.parse(History.getState().hash);
		});

		crossroads.parse(History.getState().hash);
	}

	nav.setCurrentLocation = function(path) {
		History.pushState(null, '', path);
		ga('send', 'pageview');
	}

	nav.getCurrenLocation = function() {
		return History.getState().hash;
	}

	nav.getLanguage = function() {
		var lang = $.cookie('language');
		if(!lang)
			lang = navigator.language || navigator.userLanguage;
		return lang;
	}

	nav.setLanguage = function(lang) {
		$.cookie('language', lang, {
			expires : 14,
			path : '/'
		});
		location.reload(true);
	}
};