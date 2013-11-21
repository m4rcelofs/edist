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
					$('#' + group.encodedKey).removeClass('panel-danger');
					$('#' + group.encodedKey).addClass('panel-success');

					setTimeout(function() {
						$('#' + group.encodedKey).removeClass('panel-success');
					}, 3000);
				} else {
					$('#' + group.encodedKey).removeClass('panel-success');
					$('#' + group.encodedKey).addClass('panel-danger');
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
					$('#add-group-form').trigger('reset');
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
		self.groups([]);
		ManagementService.listGroups({
			callback : function(response) {
				response.results.sort(Management.groupSortFct);
				self.groups(response.results);
				self.granularity(response.granularity);				
			}
		});
	}
};
