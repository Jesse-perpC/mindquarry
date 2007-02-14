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

var teamspaceQuery_;
var membership_;
var model_;
var form_;

function processEditMembersForm(form) {
	var teamspaceId = cocoon.parameters["teamId"];
	var redirectURL = cocoon.parameters["redirectURL"];

	teamspaceQuery_ = cocoon.getComponent(Packages.com.mindquarry.teamspace.TeamspaceQuery.ROLE);
	
	var editedTeamspace = teamspaceQuery_.teamspaceById(teamspaceId);	
	membership_ = teamspaceQuery_.membership(editedTeamspace);
	
	model_ = form.getModel();
	form_ = form;
	model_.teamspaceName = editedTeamspace.name;
	
	loadModelWithMembership();
	
	activateEditMembersForm();
	setCreateUserEmbeddedMode();
	form.showForm("edit-members.instance");
	
	cocoon.redirectTo(redirectURL);
}

function loadModelWithMembership() {

	var editModel = model_.editMembersModel;
	
	var members = membership_.getMembers();
		
	for (var i = 0; i < members.size(); i++) {
		editModel.members[i].userId = members.get(i).id;
		editModel.members[i].name = members.get(i).name;
		editModel.members[i].surname = members.get(i).surname;
		editModel.members[i].skills = members.get(i).skills;
	}
	
	var nonMembers = membership_.getNonMembers();
		
	for (var i = 0; i < nonMembers.size(); i++) {
		editModel.nonMembers[i].userId = nonMembers.get(i).id;
		editModel.nonMembers[i].name = nonMembers.get(i).name;
		editModel.nonMembers[i].surname = nonMembers.get(i).surname;
		editModel.nonMembers[i].skills = nonMembers.get(i).skills;
	}
}

function processCreateUser(form) {
    // do this before showing the form, because new cocon requests 
    // delete existing params
	var redirectURL = cocoon.parameters["redirectURL"];
	
	model_ = form.getModel();
	form_ = form;
			
	activateUserForm();
	setCreateUserStandaloneMode();
		
	form.showForm("edit-members.instance");
	
	if (form.submitId != "cancelSubmit")
		createUser();
		
	cocoon.redirectTo(redirectURL);
}

function createUser() {
	var userModel = model_.userModel;
	
	var photoWidget = form_.lookupWidget("/userModel/photo");
	if (containsPhotoUpload(photoWidget))
	    persistUserPhoto(userModel.userId, photoWidget);
	    	
	var userAdmin = cocoon.getComponent(UserAdmin.ROLE);
	
	var result = userAdmin.createUser(
			userModel.userId, userModel.newPassword, userModel.name, 
			userModel.surname, userModel.email, userModel.skills);
	
	cocoon.releaseComponent(userAdmin);
	
	return result;
}

function containsPhotoUpload(userForm) {
	var uploadWidget = form_.lookupWidget("/userModel/photo");
	return (uploadWidget.getValue() != null);
}

function persistUserPhoto(userId, userForm) {
	
	var photoJcrUri = "jcr:///users/" + userId + ".png"		
	var photoSource = resolveSource(photoJcrUri);

	var uploadWidget = userForm.lookupWidget("/userModel/photo");
	var uploadValue = uploadWidget.getValue();
	
	uploadValue.copyToSource(photoSource);
}

function resolveSource(uri) {
	var resolver = cocoon.getComponent(Packages.org.apache.cocoon.environment.SourceResolver.ROLE);
    var result = resolver.resolveURI(uri);
    cocoon.releaseComponent(resolver);
    return result;
}

function activateUserForm() {
	model_.editMembersModel.state = Packages.org.apache.cocoon.forms.formmodel.WidgetState.INVISIBLE;
	model_.userModel.state = Packages.org.apache.cocoon.forms.formmodel.WidgetState.ACTIVE;
}

function activateEditMembersForm() {
	model_.editMembersModel.state = Packages.org.apache.cocoon.forms.formmodel.WidgetState.ACTIVE;
	model_.userModel.state = Packages.org.apache.cocoon.forms.formmodel.WidgetState.INVISIBLE;
}

function setCreateUserStandaloneMode() {	
	model_.userModel.createUserSubmit.state = Packages.org.apache.cocoon.forms.formmodel.WidgetState.ACTIVE;
	model_.userModel.cancelSubmit.state = Packages.org.apache.cocoon.forms.formmodel.WidgetState.ACTIVE;
	
	model_.userModel.createUserAction.state = Packages.org.apache.cocoon.forms.formmodel.WidgetState.INVISIBLE;
	model_.userModel.cancelAction.state = Packages.org.apache.cocoon.forms.formmodel.WidgetState.INVISIBLE;
}

function setCreateUserEmbeddedMode() {	
	model_.userModel.createUserSubmit.state = Packages.org.apache.cocoon.forms.formmodel.WidgetState.INVISIBLE;
	model_.userModel.cancelSubmit.state = Packages.org.apache.cocoon.forms.formmodel.WidgetState.INVISIBLE;
		
	model_.userModel.createUserAction.state = Packages.org.apache.cocoon.forms.formmodel.WidgetState.ACTIVE;
	model_.userModel.cancelAction.state = Packages.org.apache.cocoon.forms.formmodel.WidgetState.ACTIVE;
}

function saveMembershipChanges(event) {
	teamspaceQuery_.updateMembership(membership_);
}

function removeMember(event) {
	var editModel = model_.editMembersModel;
	
	var rowIndex = fetchRowIndex(event);
	var modelUser =	editModel.members[rowIndex];
	
	removeUserFromMembers(modelUser);
}

function addMember(event) {	
	var editModel = model_.editMembersModel;
	
	var rowIndex = fetchRowIndex(event);
	var modelUser =	editModel.nonMembers[rowIndex];
	
	addUserToMembers(modelUser);
}

function removeUserFromMembers(user) {
	var editModel = model_.editMembersModel;
	
	membership_.removeMember(user.userId);
	addUserToEndOfList(user, editModel.nonMembers);
}

function addUserToMembers(user) {
	membership_.addMember(user.userId);
	addUserToEndOfList(user, model_.editMembersModel.members);
}

function addUserToEndOfList(user, list) {
	var i = list.length;
	list[i].userId = user.userId;
	list[i].name = user.name;
	list[i].surname = user.surname;
	list[i].skills = user.skills;
}

function fetchRowIndex(event) {
	var repeaterClazz = org.apache.cocoon.forms.formmodel.Repeater;
	var row = repeaterClazz.getParentRow(event.getSourceWidget());
	var repeater = row.parent;
	return repeater.indexOf(row);	
}
