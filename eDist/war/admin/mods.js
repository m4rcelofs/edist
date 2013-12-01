var ModeratorsViewModel = new function() {
	var self = this;

	self.selectedUser = ko.observable();
	self.selectedUserEmail = ko.computed(function() {
		return self.selectedUser() ? self.selectedUser().gEmail : '';
	});

	self.cursor = ko.observable(null);
	self.appId = ko.observable();
	self.controlPanelLocation = ko.computed(function() {
		return self.appId() == null ? 'https://appengine.google.com'
				: 'https://appengine.google.com/permissions?&app_id=s~'
						+ self.appId();
	});

	self.registeredUsers = ko.observableArray([]);

	self.onModeratorsShow = function() {
		if (edist.server.appId) {
			self.appId(edist.server.appId);
		}
		loadModeratorsTable();
	}

	self.registeredDate = function(item) {
		if (item.registeredAt) {
			return item.registeredAt.toLocaleString();
		} else {
			return '';
		}
	}

	self.loadNextPage = function() {
		if (self.cursor() !== null) {
			loadModeratorsTable(self.cursor());
		}
	}

	self.confirmUserRemoval = function() {
		if (self.selectedUser()) {
			removeUser(self.selectedUser());
		}
	}

	self.saveUser = function(user) {
		var user = ko.unwrap(user);
		AdminService.saveRegisteredUser(user, {
			callback : function(response) {
				if (response) {
					$('#' + user.gId).addClass('success');
					setTimeout(function() {
						$('#' + user.gId).removeClass('success');
					}, 3000);
				} else {
					Admin.defaultError();
				}
			},
			onError : Admin.defaultError
		});
	}

	function loadModeratorsTable(encodedCursor) {
		self.registeredUsers([]);
		AdminService.listRegisteredUsers(encodedCursor, {
			callback : function(response) {

				response.results.forEach(function(item) {
					self.registeredUsers.push(item);
				});

				if (response.results.length >= window.edist.server.pageSize) {
					self.cursor(response.encodedCursor);
				} else {
					self.cursor(null);
				}

			},
			onError : Admin.defaultError
		});
	}

	function removeUser(user) {
		AdminService.removeRegisteredUser({
			gId : user.gId
		}, {
			callback : function(response) {
				if (response) {
					self.registeredUsers.remove(user);
				} else {
					Admin.defaultError();
				}
			},
			onError : Admin.defaultError
		});
	}
};