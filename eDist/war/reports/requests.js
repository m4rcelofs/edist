var RequestsViewModel = new function() {
	var self = this;

	self.allRequests = function() {
		showTab();
		console.log('all');
		//TODO
	}
	
	self.filterByDate = function(param) {
		showTab();
		console.log('date');
		//TODO
	}
	
	self.filterByDistributor = function(param) {
		showTab();
		console.log('dist');
		//TODO
	}
	
	self.filterByGroup = function(param) {
		showTab();
		console.log('group');
		//TODO
	}
	
	self.filterByPlayer = function(param) {
		showTab();
		console.log('player');
		//TODO
	}
	
	self.filterByStatus = function(param) {
		showTab();
		console.log('status');
		//TODO
	}
	
	self.onRequestsShow = function() {
		//TODO
		console.log('onRequestsShow');
	}
	
	function showTab() {
		$('#requestsTab').tab('show');
	}
};
