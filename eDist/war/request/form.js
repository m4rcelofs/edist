$(document).ready(function() {

	ko.applyBindings(FormViewModel, document.getElementById('playerForm'));

	crossroads.addRoute('request/{id}', function(id) {
		FormViewModel.selectedName(id);
	});
	Navigation.configureRoutes();
});

var FormViewModel = new function() {

	var self = this;

	self.player = ko.observable(ko.mapping.fromJS(new Player()));
	self.requestAttempt = ko
			.observable(ko.mapping.fromJS(new RequestAttempt()));

	self.selectedFFs = ko.observable(0);
	self.selectedType = ko.observable('');
	self.refreshCountdown = ko.observable();
	self.requestStatus = ko.observable();

	self.selectedName = ko.observable();
	ko.computed(function() {
		fetchPlayer(self.selectedName());
	}, this).extend({
		throttle : 750
	});

	self.selectedGroup = ko.observable();
	ko.computed(function() {
		fetchGroupConfig(self.selectedGroup());
		self.selectedFFs(0);
	}, this).extend({
		throttle : 100
	});

	self.avatarImage = ko.computed(function() {
		if (self.player() && self.player().avatar()) {
			return 'url(' + self.player().avatar() + ') no-repeat center';
		}
		return 'url(../img/default_male.cache.gif) no-repeat center'
	});

	self.sendRequest = function(formElement) {
		var player = {
			encodedKey : ko.unwrap(self.player().encodedKey())
		};
		var group = {
			encodedKey : ko.unwrap(self.selectedGroup().encodedKey)
		};

		$('#sendRequestBtn').button('loading');
		RequestService.sendRequest(player, group, self.selectedFFs(), self
				.selectedType(), {
			callback : function(response) {
				console.log(response);
				if (response === true) {
					$('#sendRequestBtn').button('complete');

					self.requestStatus('OK');
					self.requestAttempt().availableFF(
							self.requestAttempt().availableFF()
									- self.selectedFFs());
					self.selectedFFs(0);
				} else {
					$('#sendRequestBtn').button('reset');
					self.requestStatus('ERROR');
				}

			}
		});
	}

	function fetchPlayer(playerString) {
		if (playerString) {
			RequestService.identify(playerString, {
				callback : function(response) {
					Navigation.setCurrentLocation(response.erId ? '/request/'
							+ response.erId : '/request');
					if (response.groups) {
						response.groups.sort(function(left, right) {
							return left.groupName == right.groupName ? 0
									: (left.groupName < right.groupName ? -1
											: 1)
						});
						self.selectedGroup(response.groups[0]);
					}

					self.player(ko.mapping.fromJS(response, self.player()));
					self
							.requestAttempt(ko.mapping
									.fromJS(new RequestAttempt()));
				}
			});
		}
	}

	function fetchGroupConfig(group) {
		if (self.player() && self.player().encodedKey() && group
				&& group.encodedKey && self.player().dbStatus() === 'ENABLED') {
			var playerDTO = {
				encodedKey : self.player().encodedKey()
			};
			var groupDTO = {
				encodedKey : ko.unwrap(group.encodedKey)
			};

			RequestService
					.getCurrentStatus(
							playerDTO,
							groupDTO,
							{
								callback : function(response) {
									if (ko
											.unwrap(self.selectedGroup().encodedKey) === response.group.encodedKey) {

										preSelectDistType(response);

										self.requestAttempt(ko.mapping
												.fromJS(response, self
														.requestAttempt()));

										startCounter(response);
									}
								}
							});
		}
	}

	function preSelectDistType(requestAttempt) {
		if (requestAttempt.rocketAllowed === true) {
			self.selectedType('ROCKET');
		} else {
			self.selectedType(requestAttempt.allowedType);
		}
	}

	function startCounter(requestAttempt) {
		if (!self.refreshCountdown()) {
			setInterval(function() {

				var count = self.requestAttempt().rechargeTime();

				var seconds = fixIntegers(count % 60);
				count = Math.floor(count / 60);

				var minutes = fixIntegers(count % 60);
				count = Math.floor(count / 60);

				var hours = fixIntegers(count % 24);

				self.refreshCountdown(hours + "h " + minutes + "m " + seconds
						+ "s");

				self.requestAttempt().rechargeTime(
						self.requestAttempt().rechargeTime() - 1);

			}, 1000);
		}
	}

	function fixIntegers(integer) {
		if (integer < 0) {
			integer = 0;
		}
		if (integer < 10) {
			return "0" + integer;
		}
		return "" + integer;
	}
};