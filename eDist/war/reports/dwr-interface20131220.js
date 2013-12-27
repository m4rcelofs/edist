
if (typeof Request != "function") {
  function Request() {
    this.resources = null;
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
    this.rank = null;
    this.maxHit = null;
    this.collectedAt = null;
    this.strength = null;
    this.name = null;
    this.xp = null;
    this.day = null;
    this.avatar = null;
    this.groups = null;
  }
}

// Provide a default path to dwr.engine
if (dwr == null) var dwr = {};
if (dwr.engine == null) dwr.engine = {};
if (DWREngine == null) var DWREngine = dwr.engine;

if (ReportService == null) var ReportService = {};
ReportService._path = '/reports/dwr';
ReportService.listRequestsByPlayer = function(p0, p1, callback) {
  dwr.engine._execute(ReportService._path, 'ReportService', 'listRequestsByPlayer', p0, p1, callback);
}
ReportService.listRequestsByGroup = function(p0, p1, callback) {
  dwr.engine._execute(ReportService._path, 'ReportService', 'listRequestsByGroup', p0, p1, callback);
}
ReportService.listRequestsByStatus = function(p0, p1, callback) {
  dwr.engine._execute(ReportService._path, 'ReportService', 'listRequestsByStatus', p0, p1, callback);
}
ReportService.listRequestsByType = function(p0, p1, callback) {
  dwr.engine._execute(ReportService._path, 'ReportService', 'listRequestsByType', p0, p1, callback);
}
ReportService.listWorkByPlayer = function(p0, p1, callback) {
  dwr.engine._execute(ReportService._path, 'ReportService', 'listWorkByPlayer', p0, p1, callback);
}
ReportService.listWorkByGroup = function(p0, p1, callback) {
  dwr.engine._execute(ReportService._path, 'ReportService', 'listWorkByGroup', p0, p1, callback);
}
ReportService.listWorkByEmployer = function(p0, p1, callback) {
  dwr.engine._execute(ReportService._path, 'ReportService', 'listWorkByEmployer', p0, p1, callback);
}
ReportService.listWorkByDate = function(p0, p1, callback) {
  dwr.engine._execute(ReportService._path, 'ReportService', 'listWorkByDate', p0, p1, callback);
}
ReportService.listWorkByDay = function(p0, p1, callback) {
  dwr.engine._execute(ReportService._path, 'ReportService', 'listWorkByDay', p0, p1, callback);
}
ReportService.getInitialInfo = function(callback) {
  dwr.engine._execute(ReportService._path, 'ReportService', 'getInitialInfo', callback);
}
ReportService.requestDetails = function(p0, callback) {
  dwr.engine._execute(ReportService._path, 'ReportService', 'requestDetails', p0, callback);
}
ReportService.listRequestsByDate = function(p0, p1, callback) {
  dwr.engine._execute(ReportService._path, 'ReportService', 'listRequestsByDate', p0, p1, callback);
}
ReportService.listRequestsByDay = function(p0, p1, callback) {
  dwr.engine._execute(ReportService._path, 'ReportService', 'listRequestsByDay', p0, p1, callback);
}
ReportService.listRequestsByDistributor = function(p0, p1, callback) {
  dwr.engine._execute(ReportService._path, 'ReportService', 'listRequestsByDistributor', p0, p1, callback);
}