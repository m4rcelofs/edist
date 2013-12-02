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
		self.rootEntity.workVerif = ko.mapping.toJS(self.currentWorkVerif);
		closeModal();
	}

	self.cancel = function() {
		closeModal();
	}

	function closeModal() {
		$('#workVerifDialog').modal('hide');
	}

}