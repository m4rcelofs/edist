var ConfigViewModel = new function() {
	var self = this;

	self.config = ko.mapping.fromJS(new ServerConfig());

	self.onConfigShow = function() {
		loadConfig();
	}

	self.saveConfig = function() {
		saveConfigs();
	}

	self.editConfig = function() {
		DistConfigViewModel.setDistConfig('server-config', self.config);
		$('#distConfigDialog').modal('show');
	}

	self.editFightVerif = function() {
		FightVerificationViewModel.setFightVerif('server-config', self.config);
		$('#fightVerifDialog').modal('show');
	}

	self.editWorkVerif = function() {
		WorkVerificationViewModel.setWorkVerif('server-config', self.config);
		$('#workVerifDialog').modal('show');
	}

	function loadConfig() {
		AdminService.getServerConfig({
			callback : function(response) {
				ko.mapping.fromJS(response, self.config);
			},
			onError : Admin.defaultError
		});
	}

	function saveConfigs() {
		$('#save-config').button('loading');
		$('#config-form').attr("disabled", "disabled");

		var config = ko.mapping.toJS(self.config);

		AdminService.saveServerConfig(config, {
			callback : function(response) {
				$('#config-form').removeAttr("disabled");
				$('#save-config').button('reset');
				if (response) {
					Admin.defaultSuccess();
				} else {
					Admin.defaultError();
				}
			},
			onError : Admin.defaultError
		});
	}
};
