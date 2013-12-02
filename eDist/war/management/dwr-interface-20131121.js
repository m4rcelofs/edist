if (typeof FightVerification != "function") {
	function FightVerification() {
		this.fightVerifPct = 0;
		this.fightVerifDays = 0;
		this.fightVerifAction = null;
	}
}

if (typeof WorkVerification != "function") {
	function WorkVerification() {
		this.workVerifDays = 0;
		this.minWorkedDaysPct = 0;
		this.workVerifAction = null;
	}
}

if (typeof DistConfig != "function") {
	function DistConfig() {
		this.allowedType = null;
		this.allowedWeaponQuality = null;
		this.autoSend = null;
		this.rocketAllowed = null;
		this.distributor = null;
		this.currentMaxDailyFF = null;
	}
}

if (typeof Player != "function") {
	function Player() {
		this.groupIds = null;
		this.fightStatus = null;
		this.name = null;
		this.dbStatus = null;
		this.config = null;
		this.workStatus = null;
		this.fightVerif = null;
		this.rocketEnabled = null;
		this.avatar = null;
		this.workVerif = null;
		this.encodedKey = null;
		this.erId = null;
	}
}

// Provide a default path to dwr.engine
if (dwr == null)
	var dwr = {};
if (dwr.engine == null)
	dwr.engine = {};
if (DWREngine == null)
	var DWREngine = dwr.engine;

if (ManagementService == null)
	var ManagementService = {};
ManagementService._path = '/management/dwr';
ManagementService.editPlayer = function(p0, callback) {
	dwr.engine._execute(ManagementService._path, 'ManagementService',
			'editPlayer', p0, callback);
}
ManagementService.saveGroup = function(p0, callback) {
	dwr.engine._execute(ManagementService._path, 'ManagementService',
			'saveGroup', p0, callback);
}
ManagementService.listPlayers = function(p0, callback) {
	dwr.engine._execute(ManagementService._path, 'ManagementService',
			'listPlayers', p0, callback);
}
ManagementService.listGroups = function(callback) {
	dwr.engine._execute(ManagementService._path, 'ManagementService',
			'listGroups', callback);
}
ManagementService.listGroupNames = function(callback) {
	dwr.engine._execute(ManagementService._path, 'ManagementService',
			'listGroupNames', callback);
}
ManagementService.addPlayer = function(p0, callback) {
	dwr.engine._execute(ManagementService._path, 'ManagementService',
			'addPlayer', p0, callback);
}
ManagementService.listDistributors = function(callback) {
	dwr.engine._execute(ManagementService._path, 'ManagementService',
			'listDistributors', callback);
}