function onEmployersShow() {
	loadEmployersTable();
}

function loadEmployersTable() {
	$('#employers-table').hide();
	$('#employers-loader').show();
	$('#employers-table > tbody > tr').remove();
	AdminService.listEmployers({
		callback : function(response) {

			response.forEach(function(item) {
				addCustomEmployerRow(item);
			});

			$('#employers-loader').hide();
			$('#employers-table').fadeIn();
		},
		onError : defaultError
	});
}

function addCustomEmployerRow(item) {

	var date = 'Não acessado';
	if (item.lastUpdatedAt) {
		date = item.lastUpdatedAt.toLocaleString();
	}

	var row = '<tr id="' + item.encodedKey
			+ '"><td><a href="http://www.erepublik.com/en/citizen/profile/'
			+ item.erId + '">' + item.name + '</a></td><td>' + date
			+ '</td><td>' + createEmployerKeyIcon(item) + '</td></tr>';

	$('#employers-table > tbody').append(row);
}

function createEmployerKeyIcon(item) {
	var icon = '<a id="employer'
			+ item.erId
			+ '" data-toggle="modal" href="#employerKeyModal" onclick="setModalEmployer(\''
			+ item.name + '\', \'' + item.encodedKey
			+ '\');"><span class="glyphicon glyphicon-barcode"></span> Chave de Autenticação</a>'
	return icon;
}

function setModalEmployer(name, key) {
	$('#employer-modal-name').val(name);
	$('#employer-modal-key').val(key);
}

function addEmployer() {
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
				defaultError();
			}
		},
		onError : defaultError
	});
}