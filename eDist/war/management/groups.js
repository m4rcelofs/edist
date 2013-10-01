var GroupsViewModel = new function() {
	var self = this;

	self.groups = ko.observableArray([]);
	self.granularity = ko.observable();

	self.onGroupsShow = function() {
		loadGroupsTable();
	};

	self.saveGroup = function(group) {
		group.config = getDistConfig(group.encodedKey);
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
		ManagementService.loadGroupDistConfig(group, {
			callback : function(response) {
				console.log(response);
				setDistConfig(group.encodedKey, response);
				$('#distConfigDialog').modal('show');
			}, 
			onError : Management.defaultError
		});
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
