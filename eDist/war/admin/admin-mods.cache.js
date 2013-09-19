function onModeratorsShow() {
	$('#mods-table > tbody > tr').remove();
	AdminService.listRegisteredUsers(null, {
		callback : function(response) {

			edist.registeredUsersList = {
				cursor : response.encodedCursor
			};

			response.results.forEach(function(item) {
				addCustomUserRow(item);
			});

			$('#mods-loader').hide();
			$('#mods-table').show();
		},
		onError : defaultError
	});

}

function addCustomUserRow(item) {
	var date = '';
	if (item.registeredAt) {
		date = item.registeredAt.toLocaleString();
	}

	var nickId = 'name' + item.gId;
	var name = '<input type="text" id="' + nickId
			+ '" placeholder="nome/nick">';

	var selectId = 'role' + item.gId;
	var select = createRoleSelect(selectId);
	var saveIcon = '<span class="glyphicon glyphicon-floppy-disk"></span>  ';
	var removeIcon = createRemoveIcon(item);

	var row = '<tr><td style="display:none;">' 
			+ item.gId + '</td><td>' 
			+ name + '</td><td>' 
			+ item.gEmail + '</td><td>' 
			+ select + '</td><td>'
			+ date + '</td><td>' 
			+ saveIcon + removeIcon + '</td></tr>';

	$('#mods-table > tbody').append(row);
	$(nickId).val(item.name);
	$(selectId).val(item.role);
}

function createRoleSelect(selectId) {
	var select = '<select class="form-control" data-style="btn-inverse"	name="role" id="'
			+ selectId
			+ '"><option value="NONE">Nenhum</option><option value="VIEWER">Visitante</option>'
			+ '<option value="MODERATOR">Moderador</option></select>';
	return select;
}

function createRemoveIcon(item) {
	var remove = '<a href="#removeUserConfirmation" role="button" data-toggle="modal"' +
		'onclick="selectedUserId=' + item.gId
		+';"><span class="glyphicon glyphicon-remove"></span></a>';
	return remove;
}

function confirmUserRemoval() {
	console.log(JSON.stringify(selectedUserId));
	
	AdminService.removeRegisteredUser({gId : selectedUserId}, {
		callback : function(response) {
			console.log('1');
			if(response) {
				console.log('2');
				onModeratorsShow();
			}
		}, 
		onError: defaultError()
	});
}