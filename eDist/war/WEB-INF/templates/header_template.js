if (!window.edist) {
	window.edist = {};
}

window.edist.header = {
	muName : '{muName}'
};

$(document).ready(function() {
	if(edist.header.muName) {
		$('#muNameHeader').text(edist.header.muName);
	}
});