var RequestsViewModel = new function() {
	var self = this;

	self.category = ko.observable('');
	self.dists = ko.observableArray([]);
	self.cursor = ko.observable(null);

	ko.computed(function() {
		if (self.category()) {
			showTab();
		}
	});

	self.initRequests = function() {
		showTab();
	}

	self.allRequests = function() {
		self.category('all');

		// TODO

		ReportService.listRequests(self.cursor(), {
			callback : function(response) {
				console.log(response);
				self.cursor(response.encodedCursor);
				response.results.forEach(function(item) {
					self.dists.push(item);
				});
			}
		});
	}

	self.filterByDate = function(param) {
		self.category('date');

		// TODO
	}

	self.filterByDistributor = function(param) {
		self.category('distributor');

		// TODO
	}

	self.filterByGroup = function(param) {
		self.category('group');

		// TODO
	}

	self.filterByPlayer = function(param) {
		self.category('player');

		// TODO
	}

	self.filterByStatus = function(param) {
		self.category('status');

		// TODO
	}

	self.filterByType = function(param) {
		self.category('type');

		// TODO
	}

	self.requestedAt = function(item) {
		return formatDate(item.requestedAt);
	}

	self.completedAt = function(item) {
		return formatDate(item.completedAt);
	}

	function formatDate(date) {
		return date ? date.toLocaleString() : '';
	}

	function showTab() {
		$('#requestsTab').tab('show');
	}

};
