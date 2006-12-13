
function userById(userId) {
	var userAdmin = cocoon.getComponent(UserAdmin.ROLE);	
	var result = userAdmin.userById(userId);	
	cocoon.releaseComponent(userAdmin);
	return result;
}