
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

// Provide a default path to dwr.engine
if (dwr == null) var dwr = {};
if (dwr.engine == null) dwr.engine = {};
if (DWREngine == null) var DWREngine = dwr.engine;

if (PlayerReportService == null) var PlayerReportService = {};
PlayerReportService._path = '/players/dwr';
PlayerReportService.statsByDay = function(p0, p1, callback) {
  dwr.engine._execute(PlayerReportService._path, 'PlayerReportService', 'statsByDay', p0, p1, callback);
}
PlayerReportService.statsByGroup = function(p0, p1, p2, callback) {
  dwr.engine._execute(PlayerReportService._path, 'PlayerReportService', 'statsByGroup', p0, p1, p2, callback);
}
PlayerReportService.statsByDate = function(p0, p1, callback) {
  dwr.engine._execute(PlayerReportService._path, 'PlayerReportService', 'statsByDate', p0, p1, callback);
}
PlayerReportService.getInitialInfo = function(callback) {
  dwr.engine._execute(PlayerReportService._path, 'PlayerReportService', 'getInitialInfo', callback);
}
PlayerReportService.statsByPlayer = function(p0, p1, callback) {
  dwr.engine._execute(PlayerReportService._path, 'PlayerReportService', 'statsByPlayer', p0, p1, callback);
}
PlayerReportService.statsByDivision = function(p0, p1, p2, callback) {
  dwr.engine._execute(PlayerReportService._path, 'PlayerReportService', 'statsByDivision', p0, p1, p2, callback);
}
PlayerReportService.getAvailableRequestStats = function(callback) {
  dwr.engine._execute(PlayerReportService._path, 'PlayerReportService', 'getAvailableRequestStats', callback);
}
PlayerReportService.getPlayerStats = function(p0, callback) {
  dwr.engine._execute(PlayerReportService._path, 'PlayerReportService', 'getPlayerStats', p0, callback);
}