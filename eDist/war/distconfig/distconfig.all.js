/*
 * JavaScript file created by Rockstarapps Concatenation
*/

/*
 * START OF FILE - /edist/eDist/war/distconfig/dist-config.js
 */
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
/*
 * END OF FILE - /edist/eDist/war/distconfig/dist-config.js
 */

/*
 * START OF FILE - /edist/eDist/war/distconfig/fight-verification.js
 */
var FightVerificationViewModel = new function() {
	var self = this;
	
	self.rootEntity = null;
	self.currentKey = ko.observable();
	self.currentFightVerif = ko.mapping.fromJS(new FightVerification());
	
	self.isServerFightVerif = ko.computed(function() {
		return this.currentKey() === 'server-config';
	}, this);
	
	self.setFightVerif = function(key, root) {
		self.rootEntity = root;
		self.currentKey(key);
		ko.mapping.fromJS(ko.unwrap(root.fightVerif), self.currentFightVerif);
	}
	
	self.updateRoot = function() {
		if (typeof self.rootEntity.fightVerif === "function") {
			self.rootEntity.fightVerif(ko.mapping.toJS(self.currentFightVerif));	
		} else {
			self.rootEntity.fightVerif = ko.mapping.toJS(self.currentFightVerif);
		}
		closeModal();
	}
	
	self.cancel = function() {
		closeModal();
	}

	function closeModal() {
		$('#fightVerifDialog').modal('hide');
	}
}
/*
 * END OF FILE - /edist/eDist/war/distconfig/fight-verification.js
 */

/*
 * START OF FILE - /edist/eDist/war/distconfig/work-verification.js
 */
var WorkVerificationViewModel = new function() {
	var self = this;

	self.rootEntity = null;
	self.currentKey = ko.observable();
	self.currentWorkVerif = ko.mapping.fromJS(new WorkVerification());

	self.isServerWorkVerif = ko.computed(function() {
		return this.currentKey() === 'server-config';
	}, this);

	self.setWorkVerif = function(key, root) {
		self.rootEntity = root;
		self.currentKey(key);
		ko.mapping.fromJS(ko.unwrap(root.workVerif), self.currentWorkVerif);
	}

	self.updateRoot = function() {
		if (typeof self.rootEntity.workVerif === "function") {
			self.rootEntity.workVerif(ko.mapping.toJS(self.currentWorkVerif));
		} else {
			self.rootEntity.workVerif = ko.mapping.toJS(self.currentWorkVerif);
		}
		closeModal();
	}

	self.cancel = function() {
		closeModal();
	}

	function closeModal() {
		$('#workVerifDialog').modal('hide');
	}

}
/*
 * END OF FILE - /edist/eDist/war/distconfig/work-verification.js
 */

/*
 * JavaScript file created by Rockstarapps Concatenation
*/
