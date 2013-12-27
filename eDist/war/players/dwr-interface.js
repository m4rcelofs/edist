
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

if (PlayerReportService == null) var PlayerReportService = {};
PlayerReportService._path = '/players/dwr';
PlayerReportService.statsByDay = function(p0, p1, callback) {
  dwr.engine._execute(PlayerReportService._path, 'PlayerReportService', 'statsByDay', p0, p1, callback);
}
PlayerReportService.statsByGroup = function(p0, p1, callback) {
  dwr.engine._execute(PlayerReportService._path, 'PlayerReportService', 'statsByGroup', p0, p1, callback);
}
PlayerReportService.fightsByDate = function(p0, p1, callback) {
  dwr.engine._execute(PlayerReportService._path, 'PlayerReportService', 'fightsByDate', p0, p1, callback);
}
PlayerReportService.statsByDate = function(p0, p1, callback) {
  dwr.engine._execute(PlayerReportService._path, 'PlayerReportService', 'statsByDate', p0, p1, callback);
}
PlayerReportService.fightsByDay = function(p0, p1, callback) {
  dwr.engine._execute(PlayerReportService._path, 'PlayerReportService', 'fightsByDay', p0, p1, callback);
}
PlayerReportService.getInitialInfo = function(callback) {
  dwr.engine._execute(PlayerReportService._path, 'PlayerReportService', 'getInitialInfo', callback);
}
PlayerReportService.statsByPlayer = function(p0, p1, callback) {
  dwr.engine._execute(PlayerReportService._path, 'PlayerReportService', 'statsByPlayer', p0, p1, callback);
}
PlayerReportService.fightsByPlayer = function(p0, p1, callback) {
  dwr.engine._execute(PlayerReportService._path, 'PlayerReportService', 'fightsByPlayer', p0, p1, callback);
}
PlayerReportService.fightsByGroup = function(p0, p1, callback) {
  dwr.engine._execute(PlayerReportService._path, 'PlayerReportService', 'fightsByGroup', p0, p1, callback);
}