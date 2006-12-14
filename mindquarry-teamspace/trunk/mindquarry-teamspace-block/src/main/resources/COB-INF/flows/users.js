
function userById(userId) {
	var userAdmin = cocoon.getComponent(UserAdmin.ROLE);	
	var result = userAdmin.userById(userId);	
	cocoon.releaseComponent(userAdmin);
	return result;
}

function existsUserWithId(userIdWidget) {
	var userModel = model_.userModel;
	var userExists = (null != userById(userModel.userId));
	if (userExists) {
		userIdWidget.setValidationError(
			new Packages.org.apache.cocoon.forms.validation.ValidationError("userId already exists!"));
	}
	return false;
}