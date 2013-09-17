$(document).ready(function() {
	$('.help').popover();

	$('#moderatorsTab').on('show.bs.tab', onModeratorsShow);
	$('#distributorsTab').on('show.bs.tab', onDistributorsShow);
	$('#configTab').on('show.bs.tab', onConfigShow);

	onConfigShow();
});

function onModeratorsShow() {

}

function onDistributorsShow() {

}

function onConfigShow() {
	AdminService.getServerConfig({
		callback : function(response) {
			console.log(JSON.stringify(response));

			$('#muName').val(response.muName);
			$('#granularity').val(response.granularity);
			$('#reportsAccess').val(response.reportsAccess);
			$('#playerReportsAccess').val(response.playerReportsAccess);
			$('#contactEmail').val(response.contactEmail);

			$('#config-loader').hide();
			$('#config-form').show();
		}
	});
}

function saveConfig() {
	// TODO default dist config
	var config = {
		muName : $('#muName').val(),
		granularity : $('#granularity').val(),
		reportsAccess : $('#reportsAccess').val(),
		playerReportsAccess : $('#playerReportsAccess').val(),
		contactEmail : $('#contactEmail').val()
	};

	$('#config-form').attr("disabled", "disabled");

	console.log(JSON.stringify(config));
	
	AdminService.saveServerConfig(config, {
		callback : function(response) {
			$('#config-form').removeAttr("disabled");
		}
	});
}