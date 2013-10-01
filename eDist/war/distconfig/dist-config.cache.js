function setDistConfig(key, distConfig) {
	$('#dist-config-form').attr('entity-key', key);
	fillDistConfigDistributors(distConfig);

	if (key === 'server-config') {
		$('#fight-verification-enabled').attr('disabled', 'disabled');
		$('#work-verification-enabled').attr('disabled', 'disabled');
		$('#dist-config-enabled').attr('disabled', 'disabled');
		$('#dist-config-enabled').prop('checked', true);
	}

	console.log(key);
	console.log(JSON.stringify(distConfig));
	// TODO remove
}

function fillDistConfigDistributors(distConfig) {
	var loadingFct;
	if (typeof AdminService !== 'undefined') {
		loadingFct = AdminService.listDistributors;
	} else {
		loadingFct = ManagementService.listDistributors;
	}

	loadingFct({
		callback : function(response) {
			$('#dist-config-distributor').empty();
			response.forEach(function(item) {
				var text = item.name ? item.name : item.email ? item.email
						: item.encodedKey;
				var row = '<option value="' + item.encodedKey + '">' + text
						+ '</option>';
				$('#dist-config-distributor').append(row);
			});
			$('#dist-config-distributor').removeAttr('disabled');
			setDistConfigInfo(distConfig);
		}
	});

}

function setDistConfigInfo(distConfig) {
	
	if(!distConfig){
		return;
	}
	
	$('#maxFF').val(distConfig.currentMaxDailyFF);
	$('#rocketAllowed').prop('checked', distConfig.rocketAllowed);
	$('#autoDist').prop('checked', distConfig.autoSend);
	$('#distType').val(distConfig.allowedType);
	$('#distWeaponQuality').val(distConfig.allowedWeaponQuality);
	$('#dist-config-distributor').val(distConfig.distributor.encodedKey);

	if (distConfig.fightVerification
			&& distConfig.fightVerification.fightVerifAction) {
		$('#fight-verification-enabled').prop('checked', true);
		enableFightVerification();

		$('#fight-verification-action').val(
				distConfig.fightVerification.fightVerifAction);
		$('#fight-verification-pct').val(
				distConfig.fightVerification.fightVerifPct);
		$('#fight-verification-days').val(
				distConfig.fightVerification.fightVerifDays);
	}

	if (distConfig.workVerification
			&& distConfig.workVerification.workVerifAction) {
		$('#work-verification-enabled').prop('checked', true);
		enableWorkVerification();

		$('#work-verification-action').val(
				distConfig.workVerification.workVerifAction);
		$('#work-verification-pct').val(
				distConfig.workVerification.minWorkedDaysPct);
		$('#work-verification-days').val(
				distConfig.workVerification.workVerifDays);
	}
}

// saving 'null' makes no changes
// deep save needs empty object
function getDistConfig(key) {
	if ($('#dist-config-form').attr('entity-key') !== key) {
		return null;
	} else {
		var enabled = $('#dist-config-enabled').prop('checked');
		var distConfig = {
			currentMaxDailyFF : enabled ? $('#maxFF').val() : null,
			rocketAllowed : enabled ? $('#rocketAllowed').prop('checked')
					: null,
			autoSend : enabled ? $('#autoDist').prop('checked') : null,
			allowedType : enabled ? $('#distType').val() : null,
			allowedWeaponQuality : enabled ? $('#distWeaponQuality').val()
					: null,
			distributor : enabled ? {
				encodedKey : $('#dist-config-distributor').val()
			} : null,
			fightVerification : enabled ? getCurrentFightVerification() : null,
			workVerification : enabled ? getCurrentWorkVerification() : null
		};
		return distConfig;
	}
}

function getCurrentFightVerification() {
	var fightVerifEnabled = $('#fight-verification-enabled').prop('checked');
	var fightVerif = {
		fightVerifDays : $('#fight-verification-days').val(),
		fightVerifPct : $('#fight-verification-pct').val(),
		fightVerifAction : fightVerifEnabled ? $('#fight-verification-action')
				.val() : null
	};
	return fightVerif;
}

function getCurrentWorkVerification() {
	var workVerifEnabled = $('#work-verification-enabled').prop('checked');
	var workVerif = {
		workVerifDays : $('#work-verification-days').val(),
		minWorkedDaysPct : $('#work-verification-pct').val(),
		workVerifAction : workVerifEnabled ? $('#work-verification-action')
				.val() : null
	};
	return workVerif;
}

function enableFightVerification() {
	if ($('#fight-verification-enabled').prop('checked')) {
		$('.fight-verification-group').removeAttr('disabled');
	} else {
		$('.fight-verification-group').attr('disabled', 'disabled');
	}
}

function enableWorkVerification() {
	if ($('#work-verification-enabled').prop('checked')) {
		$('.work-verification-group').removeAttr('disabled');
	} else {
		$('.work-verification-group').attr('disabled', 'disabled');
	}
}