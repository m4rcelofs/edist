// ==UserScript==
// @name        Controle de Funcionarios - Gestores MUs
// @namespace   http://edist-licence.appspot.com
// @description	Plugin para o controle de funcionarios das dists por script
// @include		http://www.erepublik.com/*
// @version     0.8.2
// @downloadURL	https://{host}/js/dynamic/employer.user.js
// @updateURL	https://{host}/js/dynamic/employer.user.js
// @grant		GM_getValue
// @grant		GM_setValue
// @grant		GM_listValues
// @grant		GM_deleteValue
// @grant		GM_log
// @grant		GM_info
// @grant		GM_xmlhttpRequest
// @require 	https://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js
// ==/UserScript==

clearKeyPressed = false;

function start() {

	addShiftHandler();
	
	var employerId = getEmployer();
	var employerName = getEmployerName();
	GM_log('You are citizen ' + employerId + ', ' + employerName);
	
	if (!GM_getValue('employerKey.' + employerId)
			|| !GM_getValue('employerKey.' + employerId).trim()) {

		GM_log('No employer key, asking the user...');
		
		var key = prompt('Please enter the auth key provided by your eDist admin for player '
				+ employerName
				+ '. \n\n Press "Cancel" to disable the script for '
				+ employerName + ':');

		GM_log('User entered key "' + key + '"');
		
		// pressed Cancel
		if (key == null) {
			key = 'disabled';
		}

		GM_setValue('employerKey.' + employerId, key);
	}

	if ($('#citizen_feed').length) {
		onMainPage = true;
		onEmployeesPage = false;
		GM_log('Running employees script from main page');
	} else if ($('.employee_info').length) {
		onMainPage = false;
		onEmployeesPage = true;
		GM_log('Running employees script from employees page');
	} else {
		onMainPage = false;
		onEmployeesPage = false;
	}

	refresh();
}

function addShiftHandler() {
	$(document).on('keydown', function(e){
		clearKeyPressed = String.fromCharCode(e.which) === 'C';
	});
    $(document).on('keyup', function(e){
    	clearKeyPressed = false;
	});
}

function refresh() {

	addStats();

	// forces update every 6h
	var hasToUpdate = GM_getValue('lastUpdatedAt.' + getEmployer(), 0) < (new Date()
			.getTime() - 21600000);
	if (hasToUpdate) {
		forceUpdate();
	}
}

function addStats() {
	if (onEmployeesPage) {
		addEmployeesStats();
	}

	if (onMainPage) {
		addMainStats();
	}
}

function addMainStats() {

	$('#updatedAtDiv').remove();

	var div = document.createElement('div');
	$(div).attr('id', 'updatedAtDiv');
	$(div).attr('class', 'user_finances');
	$(div)
			.append(
					'<img src="http://s3.www.erepublik.net/images/icons/industry/12/default.png" width=20 height=20 style="margin: 0px 7px 0px 2px;">');
	$(div).append(getUpdateLink());

	var loc = $('#large_sidebar > .sidebar > .sidebar_container');
	loc.append(div);
}

function addEmployeesStats() {

	$('#updatedAtLink').remove();
	$('#help_manage').remove();

	var newLink = getUpdateLink();
	$(newLink).attr('class', 'helper');

	var loc = $('.manager_dashboard > .list > .area > h4');
	loc.append(newLink);
}

function getUpdateLink() {
	newLink = document.createElement('a');
	newLink.addEventListener('click', manualUpdate, false);

	$(newLink).attr('id', 'updatedAtLink');
	$(newLink).attr('href', '#');
	$(newLink).attr('title', 'Force update');

	var lastUpdate = '---';
	if (GM_getValue('employerKey.' + getEmployer(), '') === 'disabled') {
		lastUpdate = 'eDist is disabled for this player';
	}

	if (GM_getValue('lastUpdatedAt.' + getEmployer(), 0) !== 0) {
		lastUpdate = new Date(GM_getValue('lastUpdatedAt.' + getEmployer()));
	}

	$(newLink).text('Last employees update: ' + lastUpdate.toLocaleString());
	return newLink;
}

function setUpdateRunning() {
	GM_log('Running update...');
	newLink.removeEventListener('click', manualUpdate, false);
	$(newLink).text('Update is running, please don\'t leave this page...');
	$(newLink).attr('title', '');
}

function manualUpdate() {
	
	if(clearKeyPressed) {
		clearKey();
		location.reload();
	}
	
	GM_log('Forced employees update');
	clearCache();
	forceUpdate();
}

function forceUpdate() {
	if (onEmployeesPage) {
		// avoids sending MouseClick event to the function
		update();
	}
	if (onMainPage) {
		updateFromMainPage();
	}
}

function clearKey() {
	GM_log('Clearing player key...');
	GM_deleteValue('employerKey.' + getEmployer());
	GM_log('Cleared player key.');
}


function clearCache() {
	GM_log('Clearing employees cached data...');
	var keys = GM_listValues();
	for ( var i = 0; i < keys.length; i++) {
		var key = keys[i];
		if (key.indexOf('lastUpdatedAt') != -1
				&& key.indexOf('employerKey') != -1) {
			GM_deleteValue(keys[i]);
		}
	}
	GM_log('Cleared employees cache.');
}

function updateFromMainPage() {
	GM_xmlhttpRequest({
		method : 'GET',
		url : 'http://www.erepublik.com/en/economy/manage-employees/1',
		onerror : onErrorFct,
		onload : function(response) {
			var respDoc = $(response.responseText);
			update(respDoc);
		}
	});
}

function update(doc) {

	if (!GM_getValue('employerKey.' + getEmployer(), false)) {
		GM_log('No auth key set for this player');
		return;
	}

	if (GM_getValue('employerKey.' + getEmployer(), '') === 'disabled') {
		GM_log('Update is disabled for this player');
		return;
	}

	setUpdateRunning();

    var employees = $('#employees_used', doc).text().split('/');
    var employeeOpen = $('span.no_job_offers', doc).text();
    var salary = $('strong.job_salary', doc).text();
        
	var data = {
		id : getEmployer(),
        key : GM_getValue('employerKey.' + getEmployer(), 'null'),
        employeeStats : {
            count : employees[0],
            open : employeeOpen,
            salary : salary,
            total : employees[1]
        },
		employees : getEmployees(doc)
	};
		
	var jsonData = JSON.stringify(data);

	GM_log('The following data will be uploaded: ' + jsonData);

	GM_xmlhttpRequest({
		method : 'PUT',
		url : getServer() + '/employer',
		data : 'json=' + jsonData,
		headers : {
			"Content-Type" : "application/x-www-form-urlencoded"
		},
		onload : function(response) {
			if (response.status != 200) {
				onErrorFct(response);
			} else {
				GM_log('Update successfull.')
				GM_setValue('lastUpdatedAt.' + getEmployer(), new Date()
						.getTime());
				cache(data);
				refresh();
			}
		}
	});
}

function cache(data) {
	data.employees.forEach(function(employee) {
		employee.work.forEach(function(work) {
			if (work.worked != null || work.day != getDay()) {
				var key = employee.id + '.' + work.day;
				GM_setValue(key, true);
			}
		});
	});
	GM_log('Sent data has been cached!')
}

function onErrorFct(response) {
	if (newLink) {
		$(newLink).text('Update failed ):');
		$(newLink).parent().css('border', '1px solid red');
		newLink.addEventListener('click', manualUpdate, false);
	}
	GM_log('Something went wrong, data could not be uploaded to dist server!');
	GM_log(JSON.stringify(response));
}

function getEmployer() {
	var id = $('div.user_identity > a.user_name').attr('href').replace(
			/\/[a-z]*\/citizen\/profile\//, '');
	return id;
}

function getEmployerName() {
	var name = $('div.user_identity > a.user_name').text();
	return name.trim();
}

function getEmployees(doc) {
	var today = getDay();
	var employees = [];

	$('.list_group > .listing', doc)
			.each(
					function(index) {
						var id = $(this)
								.find('.employee_entry')
								.attr('href')
								.replace(
										/http:\/\/www\.erepublik\.com\/[a-z]*\/citizen\/profile\//,
										'');

						var employee = {
							id : id,
							work : []
						};

						for ( var i = 1; i <= 7; i++) {
							var relTo = today - (7 - i);
							var key = employee.id + '.' + relTo;
							if (GM_getValue(key, false) == false) {
								employee.work.push({
									day : relTo,
									worked : findWorked(this, i)
								});
							}
						}
						employees.push(employee);
					});
	return employees;
}

function getServer() {
	// this works on chrome, but stopped working on fx
	// var server = GM_info.script.downloadURL.match(/https:\/\/[^\/]*/);
	var server = 'https://{host}';
	GM_log('You are uploading to server ' + server);
	return server;
}

function getDay() {
	var today = $('div.erpk_time > span.eday > strong').text().replace(',', '')
			.replace('.', '');
	GM_log('Today is day ' + today);
	return today;
}

function findWorked(listing, day) {
	var worked = $(listing).find('.day' + day).attr('class');

	if (worked.indexOf('nan') >= 0) {
		return null;
	}
	if (worked.indexOf('worked') >= 0) {
		return true;
	}
	return false;
}

start();