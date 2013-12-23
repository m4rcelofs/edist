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