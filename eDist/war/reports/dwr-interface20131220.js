
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

if (typeof DailyWork != "function") {
  function DailyWork() {
    this.stats = null;
    this.worked = null;
    this.employer = null;
    this.day = null;
    this.verificationStatus = null;
    this.groups = null;
  }
}

if (typeof DailyStats != "function") {
  function DailyStats() {
    this.maxHit = null;
    this.strength = null;
    this.div = null;
    this.banTip = null;
    this.isDead = false;
    this.xp = null;
    this.citizenship = null;
    this.avatar = null;
    this.isPermaBan = false;
    this.isTempBan = false;
    this.rank = null;
    this.militaryUnit = null;
    this.collectedAt = null;
    this.name = null;
    this.day = null;
    this.groups = null;
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

if (ReportService == null) var ReportService = {};
ReportService._path = '/reports/dwr';
ReportService.fightsByDate = function(p0, p1, callback) {
  dwr.engine._execute(ReportService._path, 'ReportService', 'fightsByDate', p0, p1, callback);
}
ReportService.fightsByDay = function(p0, p1, callback) {
  dwr.engine._execute(ReportService._path, 'ReportService', 'fightsByDay', p0, p1, callback);
}
ReportService.listRequestsByPlayer = function(p0, p1, callback) {
  dwr.engine._execute(ReportService._path, 'ReportService', 'listRequestsByPlayer', p0, p1, callback);
}
ReportService.getInitialInfo = function(callback) {
  dwr.engine._execute(ReportService._path, 'ReportService', 'getInitialInfo', callback);
}
ReportService.requestDetails = function(p0, callback) {
  dwr.engine._execute(ReportService._path, 'ReportService', 'requestDetails', p0, callback);
}
ReportService.listRequestsByDistributor = function(p0, p1, callback) {
  dwr.engine._execute(ReportService._path, 'ReportService', 'listRequestsByDistributor', p0, p1, callback);
}
ReportService.listRequestsByDate = function(p0, p1, callback) {
  dwr.engine._execute(ReportService._path, 'ReportService', 'listRequestsByDate', p0, p1, callback);
}
ReportService.listRequestsByDay = function(p0, p1, callback) {
  dwr.engine._execute(ReportService._path, 'ReportService', 'listRequestsByDay', p0, p1, callback);
}
ReportService.loadMonthlyDetails = function(p0, callback) {
  dwr.engine._execute(ReportService._path, 'ReportService', 'loadMonthlyDetails', p0, callback);
}
ReportService.loadDailyDetails = function(p0, callback) {
  dwr.engine._execute(ReportService._path, 'ReportService', 'loadDailyDetails', p0, callback);
}
ReportService.getRequestStats = function(p0, callback) {
  dwr.engine._execute(ReportService._path, 'ReportService', 'getRequestStats', p0, callback);
}
ReportService.getAvailableRequestStats = function(callback) {
  dwr.engine._execute(ReportService._path, 'ReportService', 'getAvailableRequestStats', callback);
}
ReportService.fightsByGroup = function(p0, p1, callback) {
  dwr.engine._execute(ReportService._path, 'ReportService', 'fightsByGroup', p0, p1, callback);
}
ReportService.fightsByPlayer = function(p0, p1, callback) {
  dwr.engine._execute(ReportService._path, 'ReportService', 'fightsByPlayer', p0, p1, callback);
}
ReportService.listWorkByEmployer = function(p0, p1, callback) {
  dwr.engine._execute(ReportService._path, 'ReportService', 'listWorkByEmployer', p0, p1, callback);
}
ReportService.listWorkByGroup = function(p0, p1, callback) {
  dwr.engine._execute(ReportService._path, 'ReportService', 'listWorkByGroup', p0, p1, callback);
}
ReportService.listWorkByPlayer = function(p0, p1, callback) {
  dwr.engine._execute(ReportService._path, 'ReportService', 'listWorkByPlayer', p0, p1, callback);
}
ReportService.listRequestsByType = function(p0, p1, callback) {
  dwr.engine._execute(ReportService._path, 'ReportService', 'listRequestsByType', p0, p1, callback);
}
ReportService.listRequestsByStatus = function(p0, p1, callback) {
  dwr.engine._execute(ReportService._path, 'ReportService', 'listRequestsByStatus', p0, p1, callback);
}
ReportService.listRequestsByGroup = function(p0, p1, callback) {
  dwr.engine._execute(ReportService._path, 'ReportService', 'listRequestsByGroup', p0, p1, callback);
}