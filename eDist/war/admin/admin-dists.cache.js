function onDistributorsShow() {
	loadDistributorsTable();
}

function loadDistributorsTable() {
	$('#dists-table').hide();
	$('#dists-loader').show();
	$('#dists-table > tbody > tr').remove();
	AdminService.listDistributors({
		callback : function(response) {
			
			console.log(JSON.stringify(response));
			
			response.forEach(function(item) {
				addCustomDistributorRow(item);
			});

			$('#dists-loader').hide();
			$('#dists-table').fadeIn();
		},
		onError : defaultError
	});
}

function addCustomDistributorRow(item) {

	var name = '<input type="text" id="name' + item.encodedKey
			+ '" placeholder="nome/nick">';

	var email = '<input type="email" id="email' + item.encodedKey
			+ '" placeholder="email">';

	var password = '<input type="password" id="password' + item.encodedKey
			+ '" placeholder="senha">';
	
	var date = 'Não acessado';
	if (item.lastAccessTimestamp) {
		date = new Date(item.lastAccessTimestamp).toLocaleString();
	}

	var saveIcon = createDistSaveIcon(item);

	var row = '<tr id="' + item.encodedKey + '"><td>' + name + '</td><td>'
			+ email + '</td><td>' + password + '</td><td>' + date
			+ '</td><td>' + saveIcon + '</td></tr>';

	$('#dists-table > tbody').append(row);

	$('#name' + item.encodedKey).val(item.name);
	$('#email' + item.encodedKey).val(item.email);
}

function createDistSaveIcon(item) {
	var save = '<a href="javascript:void(0);" role="button" '
		+ 'onclick="saveDistributor(\''
		+ item.encodedKey
		+ '\');"><span class="glyphicon glyphicon-floppy-disk"></span></a>';
	return save;
}

function saveDistributor(encodedKey) {
	var distributor = {
			encodedKey : encodedKey,
			name : $('#name' + encodedKey).val(),
			email : $('#email' + encodedKey).val(),
			password : $('#password' + encodedKey).val()
	}
	AdminService.saveDistributor(distributor, {
		callback : function(response) {
			if(response) {
				$('#' + encodedKey).addClass('success');
				setTimeout(function(){
					$('#' + encodedKey).removeClass('success');	
				}, 3000);
			} else {
				defaultError();
			}
		}, 
		onError: defaultError
	});
}

function addDistributor() {
	var distributor = {
			name : $('#new-dist-name').val(),
			email : $('#new-dist-email').val(),
			password : $('#new-dist-password').val()
	}
	$('#add-dist-form').attr("disabled", "disabled");
	AdminService.saveDistributor(distributor, {
		callback : function(response) {
			if(response) {
				setTimeout(function(){
					$('#add-dist-form').trigger('reset');
					$('#add-dist-form').removeAttr("disabled");
					loadDistributorsTable();
				}, 1000); //this is bad
			} else {
				defaultError();
			}
		}, 
		onError: defaultError
	});
}