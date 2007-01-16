/*
 * Copyright (C) 2006-2007 Mindquarry GmbH, All Rights Reserved
 *
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 */ 
cocoon.load("resource://org/apache/cocoon/forms/flow/javascript/Form.js");
cocoon.load("flows/upload-photo.js");

var model_;
var form_;

function processEditUser(form) {
	model_ = form.getModel();
	form_ = form;
	
	var targetUri = cocoon.request.getParameter("targetUri");
	
	var user = userById(cocoon.request.getAttribute("username"));	
	model_.fullName = user.name + " " + user.surname;
	model_.email = user.email;
	
	form.showForm("edit-user.instance");
			
	cocoon.redirectTo(targetUri);
}

function changePassword() {
		
	var oldPwd = model_.changePassword.currentPassword;
	var newPwd = model_.changePassword.newPassword;
		
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