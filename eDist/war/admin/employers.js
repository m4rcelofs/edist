var EmployersViewModel = new function() {
	var self = this;

	self.employers = ko.observableArray([]);
	self.selectedEmployer = ko.observable({});

	self.selectedEmployerName = ko
			.computed(function() {
				return self.selectedEmployer().name ? self.selectedEmployer().name
						: '';
			});

	self.selectedEmployerKey = ko
			.computed(function() {
				return self.selectedEmployer().encodedKey ? self
						.selectedEmployer().encodedKey : '';
			});

	self.onEmployersShow = function() {
		loadEmployersTable();
	}

	self.addEmployer = function() {
		var employer = {
			name : $('#new-employer-name').val(),
			erId : $('#new-employer-id').val()
		};

		$('#add-employer-form').attr("disabled", "disabled");
		AdminService.saveEmployer(employer, {
			callback : function(response) {
				if (response) {
					$('#add-employer-form').trigger('reset');
					$('#add-employer-form').removeAttr("disabled");
					loadEmployersTable();
				} else {
					Admin.defaultError();
				}
			},
			onError : Admin.defaultError
		});
	}

	function loadEmployersTable() {
		self.employers([]);
		AdminService.listEmployers({
			callback : function(response) {
				response.forEach(function(item) {
					self.employers.push(item);
				});
			},
			onError : Admin.defaultError
		});
	}
};
