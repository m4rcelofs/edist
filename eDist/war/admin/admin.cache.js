$(document).ready(function() {
	$('.help').popover();

	$('#moderatorsTab').on('show.bs.tab', onModeratorsShow);
	$('#distributorsTab').on('show.bs.tab', onDistributorsShow);
	$('#configTab').on('show.bs.tab', onConfigShow);

	onModeratorsShow();
});

function onModeratorsShow() {
	$('#mods-table > tbody > tr').remove();
	AdminService.listRegisteredUsers(null, {
		callback : function(response) {

			edist.registeredUsersList = {
					cursor : response.encodedCursor
			};
			
			response.results.forEach(function(item){
				addCustomUserRow(item);
			});

			$('#mods-loader').hide();
			$('#mods-table').show();
		}
	});

}

function addCustomUserRow(item) {
	var date = '';
	if(item.registeredAt) {
		date = item.registeredAt.toLocaleString();
	}
	
	var nickId = 'name' + item.gId;
	var name = '<input type="text" id="' + nickId +'" placeholder="nome/nick">';
	
	var selectId = 'role' + item.gId;
	var select = '<select class="form-control" data-style="btn-inverse"	name="role" id="' + selectId
		+ '"><option value="NONE">Nenhum</option><option value="VIEWER">Visitante</option>'
		+ '<option value="MODERATOR">Moderador</option></select>';
	
	var saveIcon = '<span class="glyphicon glyphicon-floppy-disk"></span>  ';
	var removeIcon = '<a href="#remove" role="button" data-toggle="modal"><span class="glyphicon glyphicon-remove"></span></a>';
	
	var row = '<tr><td style="display:none;">' + item.gId +'</td><td>'
			+ name + '</td><td>'
			+ item.gEmail + '</td><td>'
			+ select + '</td><td>' 
			+ date + '</td><td>'
			+ saveIcon + removeIcon + '</td></tr>';
	
	$('#mods-table > tbody').append(row);
	$(nickId).val(item.name);
	$(selectId).val(item.role);
}

function onDistributorsShow() {

}

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
		}
	});
}

function saveConfig() {
	// TODO default dist config
	var config = {
		muName : $('#muName').val(),
		granularity : $('#granularity').val(),
		reportsAccess : $('#reportsAccess').val(),
		playerReportsAccess : $('#playerReportsAccess').val(),
		contactEmail : $('#contactEmail').val()
	};

	$('#config-form').attr("disabled", "disabled");

	console.log(JSON.stringify(config));
	
	AdminService.saveServerConfig(config, {
		callback : function(response) {
			$('#config-form').removeAttr("disabled");
		}
	});
}