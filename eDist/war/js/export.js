var Export = new function() {
	var self = this;

	self.toCSV = function(table) {
		var csv = 'sep=;\n';
		$(table).find('tr').each(function(i, row){
		    $(row).find('th,td>:not(div)').each(function(j, cell){
		    	var text = $(cell).text().trim();
		    	if(text) {
		    		csv += text + ';';
		    	}
		    })
		    csv += '\n';
		});
		var blob = new Blob([csv], {type: "text/csv;charset=utf-8"});
		saveAs(blob, "eDist.csv");
		ga('send', 'event', 'export', 'csv');
	};
};