var DistributorsViewModel = new function() {

	var self = this;

	self.distributors = ko.observableArray([]);
	self.distributorInventory = ko.observable();
	self.currentDistributor = ko.observable();

	self.qualities = [ 'Q1', 'Q2', 'Q3', 'Q4', 'Q5', 'Q6', 'Q7' ];

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

	self.loadInventory = function(distributor) {
		AdminService.loadInventory({
			encodedKey : distributor.encodedKey
		}, {
			callback : function(response) {
				console.log(response);
				self.currentDistributor(distributor);
				self.distributorInventory(response);
				$('#distributorInventory').modal('show');
			}
		});
	}

	self.inventoryContents = function(product, quality) {
		if (self.distributorInventory() && self.distributorInventory()[product]
				&& self.distributorInventory()[product][quality])
			return self.distributorInventory()[product][quality];
		return null;
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