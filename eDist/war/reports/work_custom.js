function WorkTextProvider() {
	var self = this;
	
	self.getWorkWeekPct = function(works) {
		var i = 0;
		var worked = 0;
		works.forEach(function(item){
			if(item.worked != null) {
				if(item.worked == true) {
					worked += item.nbrWorks;
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
		return 'Overtime';
	}
	
	self.getCustomText = function(works) {
		var overtime = 0;
		works.forEach(function(item){
			if(item.worked != null) {
				overtime += item.nbrWorks - 1;
			}
		});
		return '' + overtime;
	} 
}