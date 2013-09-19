if (!window.edist) {
	window.edist = {};
}

window.edist.header = {
	muName : '{muName}'
};

$(document).ready(function() {
	$('#muName').text('- ' + edist.header.muName);
});