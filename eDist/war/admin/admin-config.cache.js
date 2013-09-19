function onConfigShow() {
	AdminService.getServerConfig({
		callback : function(response) {
			$('#muName').val(response.muName);
			$('#granularity').val(response.granularity);
			$('#reportsAccess').val(response.reportsAccess);
			$('#playerReportsAccess').val(response.playerReportsAccess);
			$('#contactEmail').val(response.contactEmail);

			$('#config-loader').hide();
			$('#config-form').show();
		},
		onError : defaultError
	});
}

function saveConfig() {
	$('#save-config').button('loading');
	// TODO default dist config
	var config = {
		muName : $('#muName').val(),
		granularity : $('#granularity').val(),
		reportsAccess : $('#reportsAccess').val(),
		playerReportsAccess : $('#playerReportsAccess').val(),
		contactEmail : $('#contactEmail').val()
	};

	$('#config-form').attr("disabled", "disabled");

	AdminService.saveServerConfig(config, {
		callback : function(response) {
			$('#config-form').removeAttr("disabled");
			$('#save-config').button('reset');
			if (response) {
				defaultSuccess();
			} else {
				defaultError();
			}
		},
		onError : defaultError
	});
}