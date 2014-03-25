function StatisticsGraphics() {

	var self = this;

	self.options = {
		curveType : "function",
		isStacked : true,
		legend : {
			position : 'top',
			alignment : 'center'
		},
		hAxis : {
			title : edist.stats.day
		}
	};

	self.chart = null;
	self.dataTable = null;
	self.dataView = null;

	self.start = function() {
		if (!self.chart)
			self.chart = new google.visualization.ComboChart(document
					.getElementById('ff_chart'));
	}

	self.createTable = function(dailies) {
		self.dataTable = newEmptyDataTable();

		dailies.reverse();
		dailies.forEach(function(item) {
			self.dataTable.addRow([ '' + item.day, item.playersCount, item.ffs,
					item.rockets.Q1, item.rockets.Q2, item.rockets.Q3,
					item.rockets.Q4, item.rockets.Q5, item.requests.ROCKET,
					item.requests.WEAPONS, item.requests.FOOD,
					item.requests.WEAPONS_FOOD ]);
		});

		self.dataView = new google.visualization.DataView(self.dataTable);
		self.draw([ 0, 1 ], 'line');
	}

	self.draw = function(columns, chartType) {
		if (!self.dataView)
			return;
		self.options.seriesType = chartType ? chartType : 'line';
		self.dataView.setColumns(columns);
		self.chart.draw(self.dataView, self.options);
	}

	function newEmptyDataTable() {
		var data = new google.visualization.DataTable();
		data.addColumn('string', edist.stats.day);
		data.addColumn('number', edist.stats.players);
		data.addColumn('number', edist.stats.ffs);
		data.addColumn('number', edist.stats.rockets + ' Q1');
		data.addColumn('number', edist.stats.rockets + ' Q2');
		data.addColumn('number', edist.stats.rockets + ' Q3');
		data.addColumn('number', edist.stats.rockets + ' Q4');
		data.addColumn('number', edist.stats.rockets + ' Q5');
		data.addColumn('number', edist.stats.rockets);
		data.addColumn('number', edist.stats.weapons);
		data.addColumn('number', edist.stats.food);
		data.addColumn('number', edist.stats.both);
		return data;
	}
}

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

	// charts
	self.graphs = new StatisticsGraphics();
	self.chartCategory = ko.observable('1');
	ko.computed(function() {
		var stringArray = self.chartCategory().split(',');
		var catArray = [ 0 ]; // day
		stringArray.forEach(function(item) {
			catArray = catArray.concat(parseInt(item));
		});

		var chartType = $('input[name=category]:checked', '#categoryForm')
				.attr('data-chart-type');

		self.graphs.draw(catArray, chartType);
	});

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
				self.graphs.createTable(response.dailies);
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
		self.graphs.start();
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