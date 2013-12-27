var RankCalculator = new function() {
	
	var self = this;
	
	self.ranks = [{rank: '1', name: 'Recruit', points: 0},
	              {rank: '2', name: 'Private', points: 15},
	              {rank: '3', name: 'Private*', points: 45},
	              {rank: '4', name: 'Private**', points: 80},
	              {rank: '5', name: 'Private***', points: 120},
	              {rank: '6', name: 'Corporal', points: 170},
	              {rank: '7', name: 'Corporal*', points: 250},
	              {rank: '8', name: 'Corporal**', points: 350},
	              {rank: '9', name: 'Corporal***', points: 450},
	              {rank: '10', name: 'Sergeant', points: 600},
	              {rank: '11', name: 'Sergeant*', points: 800},
	              {rank: '12', name: 'Sergeant**', points: 1000},
	              {rank: '13', name: 'Sergeant***', points: 1400},
	              {rank: '14', name: 'Lieutenant', points: 1850},
	              {rank: '15', name: 'Lieutenant*', points: 2350},
	              {rank: '16', name: 'Lieutenant**', points: 3000},
	              {rank: '17', name: 'Lieutenant***', points: 3750},
	              {rank: '18', name: 'Captain', points: 5000},
	              {rank: '19', name: 'Captain*', points: 6500},
	              {rank: '20', name: 'Captain**', points: 9000},
	              {rank: '21', name: 'Captain***', points: 12000},
	              {rank: '22', name: 'Major', points: 15500}, 
	              {rank: '23', name: 'Major*', points: 20000},
	              {rank: '24', name: 'Major**', points: 25000},
	              {rank: '25', name: 'Major***', points: 31000},
	              {rank: '26', name: 'Commander', points: 40000},
	              {rank: '27', name: 'Commander *', points: 52000},
	              {rank: '28', name: 'Commander **', points: 67000},
	              {rank: '29', name: 'Commander ***', points: 85000},
	              {rank: '30', name: 'Lt Colonel', points: 110000},
	              {rank: '31', name: 'Lt Colonel*', points: 140000},
	              {rank: '32', name: 'Lt Colonel**', points: 180000},
	              {rank: '33', name: 'Lt Colonel***', points: 225000},
	              {rank: '34', name: 'Colonel', points: 285000},
	              {rank: '35', name: 'Colonel*', points: 355000},
	              {rank: '36', name: 'Colonel**', points: 435000},
	              {rank: '37', name: 'Colonel***', points: 540000},
	              {rank: '38', name: 'General', points: 660000},
	              {rank: '39', name: 'General*', points: 800000},
	              {rank: '40', name: 'General**', points: 950000},
	              {rank: '41', name: 'General***', points: 1140000},
	              {rank: '42', name: 'Field Marshal', points: 1350000},
	              {rank: '43', name: 'Field Marshal*', points: 1600000},
	              {rank: '44', name: 'Field Marshal**', points: 1875000},
	              {rank: '45', name: 'Field Marshal***', points: 2185000},
	              {rank: '46', name: 'Supreme Marshal', points: 2550000},
	              {rank: '47', name: 'Supreme Marshal*', points: 3000000},
	              {rank: '48', name: 'Supreme Marshal**', points: 3500000},
	              {rank: '49', name: 'Supreme Marshal***', points: 4150000},
	              {rank: '50', name: 'National Force', points: 4900000},
	              {rank: '51', name: 'National Force*', points: 5800000},
	              {rank: '52', name: 'National Force**', points: 7000000},
	              {rank: '53', name: 'National Force***', points: 9000000},
	              {rank: '54', name: 'World Class Force', points: 11500000},
	              {rank: '55', name: 'World Class Force*', points: 14500000},
	              {rank: '56', name: 'World Class Force**', points: 18000000},
	              {rank: '57', name: 'World Class Force***', points: 22000000},
	              {rank: '58', name: 'Legendary Force', points: 26500000},
	              {rank: '59', name: 'Legendary Force*', points: 31500000},
	              {rank: '60', name: 'Legendary Force**', points: 37000000},
	              {rank: '61', name: 'Legendary Force***', points: 43000000},
	              {rank: '62', name: 'God of War', points: 50000000},
	              {rank: '63', name: 'God of War*', points: 100000000},
	              {rank: '64', name: 'God of War**', points: 200000000},
	              {rank: '65', name: 'God of War***', points: 500000000},
	              {rank: '66', name: 'Titan', points: 1000000000},
	              {rank: '67', name: 'Titan*', points: 2000000000},
	              {rank: '68', name: 'Titan**', points: 4000000000},
	              {rank: '69', name: 'Titan***', points: 10000000000}];
	
	self.getRank = function(rankPoints) {
		var previous = null;
		for(var i = 0; i < self.ranks.length; i++) {
			var rank = self.ranks[i];
			if(rankPoints < rank.points) {
				break;
			} else {
				previous = rank;
			}
		}
		return previous;
	}
};


