var DistConfigViewModel = new function() {
	var self = this;

	self.rootEntity = null;
	self.currentKey = ko.observable();
	self.currentConfig = ko.mapping.fromJS(new DistConfig());
	self.distributors = ko.observableArray();

	self.isServerConfig = ko.computed(function() {
		return this.currentKey() === 'server-config';
	}, this);

	self.setDistConfig = function(key, root) {
		loadDistributors();
		self.rootEntity = root;
		self.currentKey(key);
		ko.mapping.fromJS(ko.unwrap(root.config), self.currentConfig);
	}

	self.updateRoot = function() {
		var config = ko.mapping.toJS(self.currentConfig);
		config.distributor = {
			encodedKey : ko.unwrap(self.currentConfig.distributor)
		};
		if (typeof self.rootEntity.config === "function") {
			self.rootEntity.config(config);
		} else {
			self.rootEntity.config = config;
		}
		closeModal();
	}

	self.cancel = function() {
		closeModal();
	}

	function loadDistributors() {
		if (self.distributors().length == 0) {

			var loadingFct;
			if (typeof AdminService !== 'undefined') {
				loadingFct = AdminService.listDistributors;
			} else {
				loadingFct = ManagementService.listDistributors;
			}

			loadingFct({
				callback : function(response) {
					self.distributors(response);
				}
			});
		}
	}

	function closeModal() {
		$('#distConfigDialog').modal('hide');
	}
}