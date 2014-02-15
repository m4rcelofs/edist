
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

if (typeof Request != "function") {
  function Request() {
    this.status = null;
    this.stats = null;
    this.type = null;
    this.completedAt = null;
    this.eRepDay = null;
    this.ip = null;
    this.rocketQuality = null;
    this.XAppEngineRegion = null;
    this.requestedAt = null;
    this.requestedFF = null;
    this.distributor = null;
    this.XAppEngineCity = null;
    this.XAppEngineCountry = null;
    this.group = null;
    this.encodedKey = null;
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
    this.fightStatus = null;
    this.avatar = null;
    this.rocketEnabled = null;
    this.workVerif = null;
    this.erId = null;
    this.groupIds = null;
    this.currentEmployerName = null;
    this.name = null;
    this.dbStatus = null;
    this.config = null;
    this.workStatus = null;
    this.fightVerif = null;
    this.encodedKey = null;
    this.groups = null;
  }
}

// Provide a default path to dwr.engine
if (dwr == null) var dwr = {};
if (dwr.engine == null) dwr.engine = {};
if (DWREngine == null) var DWREngine = dwr.engine;

if (ManagementService == null) var ManagementService = {};
ManagementService._path = '/management/dwr';
ManagementService.listGroupNames = function(callback) {
  dwr.engine._execute(ManagementService._path, 'ManagementService', 'listGroupNames', callback);
}
ManagementService.listPlayersByString = function(p0, p1, callback) {
  dwr.engine._execute(ManagementService._path, 'ManagementService', 'listPlayersByString', p0, p1, callback);
}
ManagementService.listDistributors = function(callback) {
  dwr.engine._execute(ManagementService._path, 'ManagementService', 'listDistributors', callback);
}
ManagementService.listPlayersByGroup = function(p0, p1, callback) {
  dwr.engine._execute(ManagementService._path, 'ManagementService', 'listPlayersByGroup', p0, p1, callback);
}
ManagementService.listPlayersByFight = function(p0, p1, callback) {
  dwr.engine._execute(ManagementService._path, 'ManagementService', 'listPlayersByFight', p0, p1, callback);
}
ManagementService.listPlayersByStatus = function(p0, p1, callback) {
  dwr.engine._execute(ManagementService._path, 'ManagementService', 'listPlayersByStatus', p0, p1, callback);
}
ManagementService.listPlayersByWork = function(p0, p1, callback) {
  dwr.engine._execute(ManagementService._path, 'ManagementService', 'listPlayersByWork', p0, p1, callback);
}
ManagementService.listGroups = function(callback) {
  dwr.engine._execute(ManagementService._path, 'ManagementService', 'listGroups', callback);
}
ManagementService.listPlayers = function(p0, p1, callback) {
  dwr.engine._execute(ManagementService._path, 'ManagementService', 'listPlayers', p0, p1, callback);
}
ManagementService.addPlayer = function(p0, callback) {
  dwr.engine._execute(ManagementService._path, 'ManagementService', 'addPlayer', p0, callback);
}
ManagementService.editPlayer = function(p0, callback) {
  dwr.engine._execute(ManagementService._path, 'ManagementService', 'editPlayer', p0, callback);
}
ManagementService.removePlayer = function(p0, callback) {
  dwr.engine._execute(ManagementService._path, 'ManagementService', 'removePlayer', p0, callback);
}
ManagementService.saveGroup = function(p0, callback) {
  dwr.engine._execute(ManagementService._path, 'ManagementService', 'saveGroup', p0, callback);
}