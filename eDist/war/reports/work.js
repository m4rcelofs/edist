function WorkFiltersViewModel() {

	var self = this;

	self.searchText = ko.observable();
	self.searchParam = ko.observable();
	self.searchFct = null;
	self.callbackFct = null;

	self.groups = Reports.groups;
	self.employers = Reports.employers;
}

function WorkTextProvider() {
	var self = this;
	
	self.getWorkWeekPct = function(works) {
		var i = 0;
		var worked = 0;
		works.forEach(function(item){
			if(item.worked != null) {
				if(item.worked == true) {
					worked++;
				}
				i++;
			}
		});
		
		if(i != 0) {
			var pct = (worked/i) * 100;
			return $.format.number(pct, edist.format.number) + '%';
		}
		return '';
	}
	
	self.hasCustomText = function() {
		return true;
	}
	
	self.getCustomTextTitle = function() {
		return 'SuperHUE';
	}
	
	self.getCustomText = function(works) {
		var worked = 0;
		works.forEach(function(item){
			if(item.worked != null) {
				if(item.worked == true) {
					worked++;
				}
			}
		});
		
		if(worked < 4) {
			return '';
		} else {
			return worked * 100 + ' BRL';
		}
	} 
}

var WorkViewModel = new function() {
	var self = this;

	self.category = ko.observable('');
	self.works = ko.observableArray([]);
	self.weeklyWorks = ko.observableArray([]);
	self.cursor = ko.observable(null);

	self.filters = new WorkFiltersViewModel();
	self.textProvider = new WorkTextProvider();

	ko.computed(function() {
		if (self.category()) {
			showTab();
		}
	});

	self.initWork = function() {
		showTab();
	}

	self.doSearch = function() {
		var searchText = self.filters.searchText();
		Navigation.setCurrentLocation('/reports/work/' + self.category() + '/'
				+ searchText);
	}

	self.loadNextPage = function() {
		if (self.cursor() !== null) {
			queryServer();
		}
	}

	self.filterByEmployer = function(param) {
		reset('employer', ReportService.listWorkByEmployer, param, {
			encodedKey : param
		}, setWeekResults);
		queryServer();
	}

	self.filterByGroup = function(param) {
		reset('group', ReportService.listWorkByGroup, param, {
			encodedKey : param
		}, setWeekResults);
		queryServer();
	}

	self.filterByPlayer = function(param) {
		reset('player', ReportService.listWorkByPlayer, param, param,
				setListResults);
		queryServer();
	}
	
	self.isTempBanned = function(works, days) {
		return checkVerificationStatus(works, days, 'TEMP_BANNED');
	}
	
	self.isWarning = function(works, days) {
		return checkVerificationStatus(works, days, 'WARNING');
	}
	
	self.isOk = function(works, days) {
		return checkVerificationStatus(works, days, 'OK');
	}
	
	self.hasWorked = function(works, days) {
		return checkWorked(works, days, true);
	}
	
	self.hasNotWorked = function(works, days) {
		return checkWorked(works, days, false);
	}
	
	self.noWorkInfo = function(works, days) {
		return checkWorked(works, days, null);
	}
	
	self.setEmployerOptionText = function(option, item) {
		if(item.lastUpdatedAt) {
			$(option).attr('data-subtext', $.format.date(item.lastUpdatedAt, edist.format.date));
		}
	}
	
	function checkWorked(works, days, status) {
		if(works && works[edist.server.eRepDay - days]) {
			return works[edist.server.eRepDay - days].worked === status; 
		} else {
			//no work info, text-muted
			return status === null;
		}
	}
	
	function checkVerificationStatus(works, days, status) {
		if(!works) {
			return false;
		}
		
		return works[edist.server.eRepDay - days] && works[edist.server.eRepDay - days].verificationStatus === status;
	}

	function checkParam(param) {
		return param && param !== 'undefined' && param !== 'null' ? param
				: null;
	}

	function reset(category, searchFct, searchText, searchParam, callbackFct) {
		self.works([]);
		self.weeklyWorks([]);
		self.cursor('');
		self.category(category);
		self.filters.searchFct = searchFct;
		self.filters.callbackFct = callbackFct;
		self.filters.searchText(searchText);
		self.filters.searchParam(searchParam);
	}

	function queryServer() {
		if (self.filters.searchParam()) {
			self.filters.searchFct(self.filters.searchParam(), self.cursor(), {
				callback : function(response) {
					self.filters.callbackFct(response);
				}
			});
		}
	}

	function setListResults(response) {
		var cursor = response.results.length >= window.edist.server.pageSize ? response.encodedCursor
				: null;
		self.cursor(cursor);
		response.results.forEach(function(item) {
			self.works.push(item);
		});
	}

	function setWeekResults(response) {
		var cursor = response.results.length >= window.edist.server.pageSize ? response.encodedCursor
				: null;
		self.cursor(cursor);

		response.results.forEach(function(workWeek){
			var weekMap = {};
			workWeek.results.forEach(function(dailyWork){
				weekMap[dailyWork.day] = dailyWork;
			});
			workWeek.resultsMap = weekMap;
			self.weeklyWorks.push(workWeek);
		});
	}

	function showTab() {
		$('#workTab').tab('show');
	}
};

var WorkRoutes = new function() {
	var self = this;

	self.addRoutes = function() {
		crossroads.addRoute('reports/work', WorkViewModel.initWork);
		crossroads.addRoute('reports/work/employer/:param:',
				WorkViewModel.filterByEmployer);
		crossroads.addRoute('reports/work/group/:param:',
				WorkViewModel.filterByGroup);
		crossroads.addRoute('reports/work/player/:param:',
				WorkViewModel.filterByPlayer);
	}
};