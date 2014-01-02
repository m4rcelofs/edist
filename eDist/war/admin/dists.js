var DistributorsViewModel = new function() {

	var self = this;

	self.distributors = ko.observableArray([]);

	self.onDistributorsShow = function() {
		loadDistributorsTable();
	}

	self.saveDistributor = function(distributor) {
		AdminService.saveDistributor(distributor, {
			callback : function(response) {
				if (response) {
					$('#' + distributor.encodedKey).addClass('success');
					setTimeout(function() {
						$('#' + distributor.encodedKey).removeClass('success');
					}, 3000);
				} else {
					Admin.defaultError();
				}
			},
			onError : Admin.defaultError
		});
	}

	self.addDistributor = function() {
		var distributor = {
			name : $('#new-dist-name').val(),
			email : $('#new-dist-email').val(),
			password : $('#new-dist-password').val()
		}
		$('#add-dist-form').attr("disabled", "disabled");
		AdminService.saveDistributor(distributor, {
			callback : function(response) {
				if (response) {
					$('#add-dist-form').trigger('reset');
					$('#add-dist-form').removeAttr("disabled");
					loadDistributorsTable();
				} else {
					Admin.defaultError();
				}
			},
			onError : Admin.defaultError
		});
	}

	function loadDistributorsTable() {
		self.distributors([]);
		AdminService.listDistributors({
			callback : function(response) {

				response.forEach(function(item) {
					self.distributors.push(item);
				});

			},
			onError : Admin.defaultError
		});
	}

};