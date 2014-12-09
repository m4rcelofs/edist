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
		return false;
	}
	
	self.getCustomTextTitle = function() {
		return '';
	}
	
	self.getCustomText = function(works) {
		return '';
	} 
}