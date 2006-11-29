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
	
	var messageWidget = form_.lookupWidget("/changePassword/message");
	messageWidget.setState(Packages.org.apache.cocoon.forms.formmodel.WidgetState.ACTIVE);

	if (changed)
		model_.changePassword.message = "password changed."
	else
		model_.changePassword.message = "password could not be changed."
}

function uploadPhoto() {

	var currentUserId = cocoon.request.getAttribute("username");
	var photoWidget = form_.lookupWidget("/uploadPhoto/photo");
	
	if (containsPhotoUpload(photoWidget))
	    persistUserPhoto(currentUserId, photoWidget);
	    
}

/*
function updateUser() {

	var userModel = model_.userModel;
	
	if (containsPhotoUpload(form_))
	    persistUserPhoto(userModel.userId, form_);
	    	
	var userAdmin = cocoon.getComponent(UserAdmin.ROLE);
	
	var result = userAdmin.createUser(
			userModel.userId, userModel.new_password, userModel.name, 
			userModel.surname, userModel.email, userModel.skills);
	
	cocoon.releaseComponent(userAdmin);
	
	return result;
} */