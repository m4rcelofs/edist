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
		ko.mapping.fromJS(root.fightVerif, self.currentFightVerif);
	}
	
	self.updateRoot = function() {
		// TODO test with serverConfig
		console.log('updateRoot fightverif');
		self.rootEntity.fightVerif = ko.mapping.toJS(self.currentFightVerif);
		console.log(self.rootEntity);
		closeModal();
	}
	
	self.cancel = function() {
		closeModal();
	}

	function closeModal() {
		$('#fightVerifDialog').modal('hide');
	}
}