
if (typeof FightVerification != "function") {
  function FightVerification() {
    this.fightVerifPct = 0;
    this.fightVerifDays = 0;
    this.fightVerifAction = null;
  }
}

if (typeof ServerConfig != "function") {
  function ServerConfig() {
    this.contactEmail = null;
    this.playerReportsAccess = null;
    this.granularity = null;
    this.config = null;
    this.fightVerif = null;
    this.avatarHistory = false;
    this.workVerif = null;
    this.reportsAccess = null;
    this.muName = null;
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

// Provide a default path to dwr.engine
if (dwr == null) var dwr = {};
if (dwr.engine == null) dwr.engine = {};
if (DWREngine == null) var DWREngine = dwr.engine;

if (AdminService == null) var AdminService = {};
AdminService._path = '/admin/dwr';
AdminService.getServerConfig = function(callback) {
  dwr.engine._execute(AdminService._path, 'AdminService', 'getServerConfig', callback);
}
AdminService.saveEmployer = function(p0, callback) {
  dwr.engine._execute(AdminService._path, 'AdminService', 'saveEmployer', p0, callback);
}
AdminService.saveServerConfig = function(p0, callback) {
  dwr.engine._execute(AdminService._path, 'AdminService', 'saveServerConfig', p0, callback);
}
AdminService.saveDistributor = function(p0, callback) {
  dwr.engine._execute(AdminService._path, 'AdminService', 'saveDistributor', p0, callback);
}
AdminService.removeRegisteredUser = function(p0, callback) {
  dwr.engine._execute(AdminService._path, 'AdminService', 'removeRegisteredUser', p0, callback);
}
AdminService.saveRegisteredUser = function(p0, callback) {
  dwr.engine._execute(AdminService._path, 'AdminService', 'saveRegisteredUser', p0, callback);
}
AdminService.listRegisteredUsers = function(p0, callback) {
  dwr.engine._execute(AdminService._path, 'AdminService', 'listRegisteredUsers', p0, callback);
}
AdminService.listDistributors = function(callback) {
  dwr.engine._execute(AdminService._path, 'AdminService', 'listDistributors', callback);
}
AdminService.listEmployers = function(callback) {
  dwr.engine._execute(AdminService._path, 'AdminService', 'listEmployers', callback);
}