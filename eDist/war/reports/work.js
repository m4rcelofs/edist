var WorkViewModel = new function() {
	var self = this;

	self.category = ko.observable('');
	
	self.workTab = function(type) {
		console.log('work');
		$('#workTab').tab('show');
		//TODO
	}
	
	
	self.onWorkShow = function() {
		// TODO
		console.log('onWorkShow');
	}
};
