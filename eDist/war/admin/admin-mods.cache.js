function onModeratorsShow() {
	if (!edist.registeredUsersList) {
		edist.registeredUsersList = {
			currentPage : 1,
			cursors : {
				1 : ''
			}
		}
	}
	loadModeratorsTable();
}

function loadModeratorsTable(encodedCursor) {
	$('#mods-table').hide();
	$('#mods-loader').show();
	$('#mods-table > tbody > tr').remove();
	AdminService
			.listRegisteredUsers(
					encodedCursor,
					{
						callback : function(response) {

							edist.registeredUsersList.cursors[edist.registeredUsersList.currentPage + 1] = response.encodedCursor;

							toggleNavigation(response.results);

							response.results.forEach(function(item) {
								addCustomUserRow(item);
							});

							$('#mods-loader').hide();
							$('#mods-table').fadeIn();
						},
						onError : defaultError
					});
}

function toggleNavigation(results) {
	if (results.length < 20) {
		$('#nextMods').addClass('disabled');
	} else {
		$('#nextMods').removeClass('disabled');
	}

	if (edist.registeredUsersList.currentPage === 1) {
		$('#previousMods').addClass('disabled');
	} else {
		$('#previousMods').removeClass('disabled');
	}
}

function nextPage() {
	if (!$('#nextMods').hasClass('disabled')) {
		edist.registeredUsersList.currentPage++;
		loadModeratorsTable(edist.registeredUsersList.cursors[edist.registeredUsersList.currentPage]);
	}
}

function previousPage() {
	if (!$('#previousMods').hasClass('disabled')) {
		edist.registeredUsersList.currentPage--;
		loadModeratorsTable(edist.registeredUsersList.cursors[edist.registeredUsersList.currentPage]);
	}
}

function addCustomUserRow(item) {
	var date = '';
	if (item.registeredAt) {
		date = item.registeredAt.toLocaleString();
	}

	var name = '<input type="text" id="name' + item.gId
			+ '" placeholder="nome/nick">';

	var select = createRoleSelect(item);
	var saveIcon = createSaveIcon(item);
	var removeIcon = createRemoveIcon(item);

	var row = '<tr id="' + item.gId + '"><td>' + name
			+ '</td><td>' + item.gEmail + '</td><td>' + select + '</td><td>'
			+ date + '</td><td>' + saveIcon + '  ' + removeIcon + '</td></tr>';

	$('#mods-table > tbody').append(row);

	$('#name' + item.gId).val(item.name);
	$('#role' + item.gId).val(item.role);
}

function createRoleSelect(item) {
	var select = '<select class="form-control" data-style="btn-inverse"	name="role" id="role'
			+ item.gId
			+ '"><option value="NONE">Nenhum</option><option value="VIEWER">Visitante</option>'
			+ '<option value="MODERATOR">Moderador</option></select>';
	return select;
}

function createSaveIcon(item) {
	var save = '<a href="javascript:void(0);" role="button" '
		+ 'onclick="saveUser(\''
		+ item.gId
		+ '\');"><span class="glyphicon glyphicon-floppy-disk"></span></a>';
	return save;
}

function saveUser(userId) {
	var user = {
			gId : userId,
			name : $('#name' + userId).val(),
			role : $('#role' + userId).val()
	}
	
	AdminService.saveRegisteredUser(user, {
		callback : function(response) {
			if(response) {
				$('#' + userId).addClass('success');
				setTimeout(function(){
					$('#' + userId).removeClass('success');	
				}, 3000);
			} else {
				defaultError();
			}
		}, 
		onError: defaultError
	});
	
}

function createRemoveIcon(item) {
	var remove = '<a href="#removeUserConfirmation" role="button" data-toggle="modal"'
			+ 'onclick="edist.registeredUsersList.selectedUserId=\''
			+ item.gId
			+ '\';"><span class="glyphicon glyphicon-remove"></span></a>';
	return remove;
}

function confirmUserRemoval() {
	AdminService.removeRegisteredUser({
		gId : edist.registeredUsersList.selectedUserId
	}, {
		callback : function(response) {
			if (response) {
				loadModeratorsTable();
			} else {
				defaultError();
			}
		},
		onError : defaultError
	});
}