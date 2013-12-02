
if (typeof RequestAttempt != "function") {
  function RequestAttempt() {
    this.rechargeTime = null;
    this.allowedType = null;
    this.allowedQuality = null;
    this.rocketAllowed = null;
    this.group = null;
    this.availableFF = null;
  }
}

if (typeof Player != "function") {
  function Player() {
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
    this.groups = null;
    this.erId = null;
  }
}

// Provide a default path to dwr.engine
if (dwr == null) var dwr = {};
if (dwr.engine == null) dwr.engine = {};
if (DWREngine == null) var DWREngine = dwr.engine;

if (RequestService == null) var RequestService = {};
RequestService._path = '/request/dwr';
RequestService.identify = function(p0, callback) {
  dwr.engine._execute(RequestService._path, 'RequestService', 'identify', p0, callback);
}
RequestService.getCurrentStatus = function(p0, p1, callback) {
  dwr.engine._execute(RequestService._path, 'RequestService', 'getCurrentStatus', p0, p1, callback);
}
RequestService.sendRequest = function(p0, p1, p2, p3, callback) {
  dwr.engine._execute(RequestService._path, 'RequestService', 'sendRequest', p0, p1, p2, p3, callback);
}
