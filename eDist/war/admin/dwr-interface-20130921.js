// Provide a default path to dwr.engine
if (dwr == null) var dwr = {};
if (dwr.engine == null) dwr.engine = {};
if (DWREngine == null) var DWREngine = dwr.engine;

if (AdminService == null) var AdminService = {};
AdminService._path = '/admin/dwr';
AdminService.listEmployers = function(callback) {
  dwr.engine._execute(AdminService._path, 'AdminService', 'listEmployers', callback);
}
AdminService.saveServerConfig = function(p0, callback) {
  dwr.engine._execute(AdminService._path, 'AdminService', 'saveServerConfig', p0, callback);
}
AdminService.listDistributors = function(callback) {
  dwr.engine._execute(AdminService._path, 'AdminService', 'listDistributors', callback);
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
AdminService.getServerConfig = function(callback) {
  dwr.engine._execute(AdminService._path, 'AdminService', 'getServerConfig', callback);
}
AdminService.saveEmployer = function(p0, callback) {
  dwr.engine._execute(AdminService._path, 'AdminService', 'saveEmployer', p0, callback);
}