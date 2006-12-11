/*
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
cocoon.load("resource://org/apache/cocoon/forms/flow/javascript/Form.js");
cocoon.load("flows/upload-photo.js");

var model_;
var form_;

function processEditUser(form) {
	model_ = form.getModel();
	form_ = form;
	
	var targetUri = cocoon.request.getParameter("targetUri");
	
	var currentUser = userById(cocoon.request.getAttribute("username"));
	
	form.showForm("edit-user.instance");
			
	cocoon.redirectTo(targetUri);
}

function userById(userId) {
	var userAdmin = cocoon.getComponent(UserAdmin.ROLE);	
	var result = userAdmin.userById(userId);	
	cocoon.releaseComponent(userAdmin);
	return result;
}

function changePassword() {
	
	var oldPwd = model_.changePassword.current_password;
	var newPwd = model_.changePassword.new_password;
		
	var userAdmin = cocoon.getComponent(UserAdmin.ROLE);
	
	var currentUserId = cocoon.request.getAttribute("username");
	var currentUser = userAdmin.userById(currentUserId);
	
	var changed = currentUser.changePassword(oldPwd, newPwd);	
	userAdmin.updateUser(currentUser);
	

	var WidgetState = Packages.org.apache.cocoon.forms.formmodel.WidgetState;
	
	// always set the not used message output to state invisible
	// otherwise both messages will appear if the user clicks changePassword twice
	var messageWidgetName;
	if (changed) {
		form_.lookupWidget("/changePassword/pwdChanged").setState(WidgetState.ACTIVE);
		form_.lookupWidget("/changePassword/pwdNotChanged").setState(WidgetState.INVISIBLE);
	}
	else {
		form_.lookupWidget("/changePassword/pwdChanged").setState(WidgetState.INVISIBLE);
		form_.lookupWidget("/changePassword/pwdNotChanged").setState(WidgetState.ACTIVE);
	}
}

function uploadPhoto() {

	var currentUserId = cocoon.request.getAttribute("username");
	var photoWidget = form_.lookupWidget("/uploadPhoto/photo");
	
	if (containsPhotoUpload(photoWidget))
	    persistUserPhoto(currentUserId, photoWidget);
	    
}