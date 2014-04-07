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
			self.dataTable.addRow([
					'' + item.day,
					item.enabledCount,
					StatisticsViewModel.countFromDivision('activeCount', item),
					item.activeCount.D1,
					item.activeCount.D2,
					item.activeCount.D3,
					item.activeCount.D4,
					StatisticsViewModel
							.countFromDivision('fightersCount', item),
					item.fightersCount.D1, item.fightersCount.D2,
					item.fightersCount.D3, item.fightersCount.D4,
					item.deadCount, item.permaBannedCount,
					item.tempBannedCount, item.influence.D1, item.influence.D2,
					item.influence.D3, item.influence.D4 ]);
		});

		self.dataView = new google.visualization.DataView(self.dataTable);
		self.draw([ 0, 1, 2, 7, 12, 13, 14 ], 'line');
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
		data.addColumn('number', edist.stats.enabled);
		data.addColumn('number', edist.stats.active + ' (' + edist.stats.total + ')');
		data.addColumn('number', edist.stats.active + ' (D1)');
		data.addColumn('number', edist.stats.active + ' (D2)');
		data.addColumn('number', edist.stats.active + ' (D3)');
		data.addColumn('number', edist.stats.active + ' (D4)');
		data.addColumn('number', edist.stats.fighters + ' (' + edist.stats.total + ')');
		data.addColumn('number', edist.stats.fighters + ' (D1)');
		data.addColumn('number', edist.stats.fighters + ' (D2)');
		data.addColumn('number', edist.stats.fighters + ' (D3)');
		data.addColumn('number', edist.stats.fighters + ' (D4)');
		data.addColumn('number', edist.stats.dead);
		data.addColumn('number', edist.stats.permaBan);
		data.addColumn('number', edist.stats.tempBan);
		data.addColumn('number', edist.stats.influence + ' (D1)');
		data.addColumn('number', edist.stats.influence + ' (D2)');
		data.addColumn('number', edist.stats.influence + ' (D3)');
		data.addColumn('number', edist.stats.influence + ' (D4)');
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
	self.divisions = [ 'D1', 'D2', 'D3', 'D4' ];

	// charts
	self.graphs = new StatisticsGraphics();
	self.chartCategory = ko.observable('1,2,7,12,13,14');
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
				.setCurrentLocation('/players/statistics/month/' + searchText);
	}

	self.filter = function(param) {
		if (!self.filters.loaded)
			showTab();

		self.isLoading(true);
		PlayerReportService.getPlayerStats(param, {
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

	self.countFromDivision = function(property, daily) {
		var count = 0;
		if (daily[property])
			self.divisions.forEach(function(item) {
				if (daily[property][item])
					count += daily[property][item];
			});
		return count;
	}

	function showTab() {
		$('#statisticsTab').tab('show');
		self.graphs.start();
		self.isLoading(true);
		PlayerReportService.getAvailableRequestStats({
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
		crossroads.addRoute('players/statistics', StatisticsViewModel.init);
		crossroads.addRoute('players/statistics/month/:param:',
				StatisticsViewModel.filter);
	}
};