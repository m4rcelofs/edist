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

	// details
	self.qualities = [ 'Q1', 'Q2', 'Q3', 'Q4', 'Q5', 'Q6', 'Q7' ];
	self.itemsSent = ko.observable();
	self.detailsLoaded = ko.observable();

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

	self.loadDailyDetails = function(daily) {
		self.itemsSent(false);
		self.detailsLoaded('');
		ReportService.loadDailyDetails({
			encodedKey : daily.encodedKey
		}, {
			callback : function(response) {
				self.detailsLoaded(daily.day);
				self.itemsSent(response);
				$('#itemsSentModal').modal('show');
			}
		});
	}

	self.loadMonthlyDetails = function(root) {
		self.itemsSent(false);
		self.detailsLoaded('');
		ReportService.loadMonthlyDetails({
			encodedKey : root.monthly().encodedKey
		}, {
			callback : function(response) {
				self.detailsLoaded(self.filters.searchText());
				self.itemsSent(response);
				$('#itemsSentModal').modal('show');
			}
		});
	}

	self.inventoryContents = function(product, quality) {
		if (self.itemsSent() && self.itemsSent()[product]
				&& self.itemsSent()[product][quality])
			return self.itemsSent()[product][quality];
		return null;
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