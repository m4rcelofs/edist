function WorkTextProvider() {
	var self = this;

	self.getWorkWeekPct = function(works) {
		var i = 0;
		var worked = 0;
		works.forEach(function(item) {
			if (item.worked != null) {
				if (item.worked == true) {
					worked++;
				}
				i++;
			}
		});

		if (i != 0) {
			var pct = (worked / i) * 100;
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
		works.forEach(function(item) {
			if (item.worked != null) {
				if (item.worked == true) {
					worked++;
				}
			}
		});

		if (worked < 4) {
			return '';
		} else {
			return worked * 100 + ' BRL';
		}
	}
}