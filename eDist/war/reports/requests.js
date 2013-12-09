var RequestsViewModel = new function() {
	var self = this;

	self.requestsTab = function(type) {
		console.log('requests: ' + type);
		$('#requestsTab').tab('show');
		//TODO
	}
	
	self.onRequestsShow = function() {
		//TODO
		console.log('onRequestsShow');
	}
};
