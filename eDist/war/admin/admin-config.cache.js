function onConfigShow() {
	AdminService.getServerConfig({
		callback : function(response) {
			
			edist.config = response;
			
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

function setServerDistConfig() {
	setDistConfig('server-config', edist.config.muConfig);
}

function saveConfig() {
	$('#save-config').button('loading');
	var config = {
		muName : $('#muName').val(),
		granularity : $('#granularity').val(),
		reportsAccess : $('#reportsAccess').val(),
		playerReportsAccess : $('#playerReportsAccess').val(),
		contactEmail : $('#contactEmail').val(),
		muConfig : getDistConfig('server-config')
	};

	console.log(JSON.stringify(config)); // TODO remove
	
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