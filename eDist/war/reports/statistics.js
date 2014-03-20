function StatisticsFiltersViewModel() {

	var self = this;

	self.searchText = ko.observable();
	self.searchParam = ko.observable();

	self.loaded = false;
	self.availableRequests = ko.observableArray();

}

var StatisticsViewModel = new function() {
	var self = this;

	self.isLoading = ko.observable();
	self.filters = new StatisticsFiltersViewModel();

	self.monthly = ko.observable();
	self.dailies = ko.observableArray();

	self.init = function() {
		showTab();
	}

	self.doSearch = function() {
		var searchText = self.filters.searchText();
		Navigation
				.setCurrentLocation('/reports/statistics/month/' + searchText);
	}

	self.filter = function(param) {
		if (!self.filters.loaded)
			showTab();

		self.isLoading(true);
		ReportService.getRequestStats(param, {
			callback : function(response) {
				// TODO remove
				console.log(response);

				self.monthly(response);
				if (response && response.dailies)
					response.dailies.sort(orderDaysfct);
				self.dailies(response.dailies);
				self.isLoading(false);
			}
		});
	}

	self.sumRockets = function(daily) {
		var rockets = 0;
		if (daily && daily.rockets)
			for ( var i = 1; i <= 5; i++)
				if (daily.rockets['Q' + i])
					rockets += daily.rockets['Q' + i];
		return rockets;
	}

	self.sumRequests = function(daily) {
		var requests = 0;
		var types = [ 'WEAPONS', 'FOOD', 'WEAPONS_FOOD', 'ROCKET' ];
		if (daily && daily.requests)
			types.forEach(function(item) {
				if (daily.requests[item])
					requests += daily.requests[item];
			});
		return requests;
	}

	self.loadDetails = function(daily) {
		// ReportService.requestDetails({
		// encodedKey : request.encodedKey
		// }, {
		// callback : function(response) {
		// self.requestDetails(response);
		// $('#requestDetails').modal('show');
		// }
		// });
	}

	function showTab() {
		$('#statisticsTab').tab('show');
		self.isLoading(true);
		ReportService.getAvailableRequestStats({
			callback : function(response) {
				self.filters.loaded = true;
				response.sort(orderMonthsFct);
				self.filters.availableRequests(response);
				self.isLoading(false);
			}
		});
	}

	function orderMonthsFct(a, b) {
		var aSplit = a.split('-');
		var bSplit = b.split('-');
		// year
		if (bSplit[0] < aSplit[0])
			return -1;
		if (bSplit[0] > aSplit[0])
			return 1;
		// month
		if (bSplit[1] < aSplit[1])
			return -1;
		if (bSplit[1] > aSplit[1])
			return 1;
		return 0;
	}

	function orderDaysfct(a, b) {
		if (b.day < a.day)
			return -1;
		if (b.day > a.day)
			return 1;
		return 0;
	}

};

var StatisticsRoutes = new function() {
	var self = this;

	self.addRoutes = function() {
		crossroads.addRoute('reports/statistics', StatisticsViewModel.init);
		crossroads.addRoute('reports/statistics/month/:param:',
				StatisticsViewModel.filter);
	}
};