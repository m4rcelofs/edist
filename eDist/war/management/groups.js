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
