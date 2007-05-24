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
	
	var refererURL = cocoon.parameters["refererURL"];
	
	var user = userById(cocoon.request.getAttribute("username"));	
	model_.fullName = user.name + " " + user.surname;
	
	model_.profile.name = user.getName();
	model_.profile.surname = user.getSurname();
	model_.profile.email = user.getEmail();
	model_.profile.skills = user.getSkills();
	
	form_.lookupWidget("/changePassword/successfullyChanged").value = "Password Changed";
	form_.lookupWidget("/changePassword/passwordNotChanged").value = "Password could not be changed";

	form.showForm("edit-user.instance");
			
	cocoon.redirectTo("cocoon:/redirectTo/" + refererURL);
}

function updateProfile() {
	var firstname = model_.profile.name;
	var surname = model_.profile.surname;
	var email = model_.profile.email;
	var skills = model_.profile.skills;
		
	var userAdmin = cocoon.getComponent(UserAdmin.ROLE);
	var currentUserId = cocoon.request.getAttribute("username");
	var currentUser = userAdmin.userById(currentUserId);
	
		
	currentUser.setName(firstname);
	currentUser.setSurname(surname);	
	currentUser.setEmail(email);
	currentUser.setSkills(skills);
	
	userAdmin.updateUser(currentUser);	
}

function changePassword() {
	var oldPwd = model_.changePassword.currentPassword;
	var newPwd = model_.changePassword.newPassword;
		
	var userAdmin = cocoon.getComponent(UserAdmin.ROLE);
	
	var currentUserId = cocoon.request.getAttribute("username");
	var currentUser = userAdmin.userById(currentUserId);
		
	var changed = currentUser.changePassword(oldPwd, newPwd);
	
	if (changed) {
	    userAdmin.updateUser(currentUser);
	    
		form_.lookupWidget("/changePassword/successfullyChanged").state = Packages.org.apache.cocoon.forms.formmodel.WidgetState.ACTIVE;
		form_.lookupWidget("/changePassword/passwordNotChanged").state = Packages.org.apache.cocoon.forms.formmodel.WidgetState.INVISIBLE;
	}
	else {
		form_.lookupWidget("/changePassword/passwordNotChanged").state = Packages.org.apache.cocoon.forms.formmodel.WidgetState.ACTIVE;
		form_.lookupWidget("/changePassword/successfullyChanged").state = Packages.org.apache.cocoon.forms.formmodel.WidgetState.INVISIBLE;
	}
}

function uploadPhoto() {
	var currentUserId = cocoon.request.getAttribute("username");
	var photoWidget = form_.lookupWidget("/uploadPhoto/photo");
	
	if (containsPhotoUpload(photoWidget))
	    persistUserPhoto(currentUserId, photoWidget);
}
