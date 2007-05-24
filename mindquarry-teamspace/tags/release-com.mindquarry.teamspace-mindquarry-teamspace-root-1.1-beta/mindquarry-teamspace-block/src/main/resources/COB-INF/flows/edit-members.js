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

importClass(Packages.java.util.ArrayList);

importClass(Packages.com.mindquarry.teamspace.TeamspaceAdmin);
importClass(Packages.com.mindquarry.teamspace.TeamspaceQuery);
importClass(Packages.com.mindquarry.user.UserQuery);

var teamsQuery_;
var userQuery_;

var editedTeamId_;
var removedUsers_;
var addedUsers_;

var model_;
var form_;

function processEditMembersForm(form) {
	editedTeamId_ = cocoon.parameters["teamId"];
	var redirectURL = cocoon.parameters["redirectURL"];

	teamsQuery_ = cocoon.getComponent(TeamspaceQuery.ROLE);
	userQuery_ = cocoon.getComponent(UserQuery.ROLE);
	
	var editedTeam = teamsQuery_.teamspaceById(editedTeamId_);
	var members = editedTeam.getUsers();
	var nonMembers = findNonMembers(members, userQuery_.allUsers());
	
	removedUsers_ = new ArrayList();
	addedUsers_ = new ArrayList();
	
	model_ = form.getModel();
	form_ = form;
	model_.teamspaceName = editedTeam.name;
	
	loadModelWithMembership(members, nonMembers);
	
	activateEditMembersForm();
	setCreateUserEmbeddedMode();
	form.showForm("edit-members.instance", { teamId : editedTeamId_ } );
	
	cocoon.redirectTo("cocoon:/redirectTo/" + redirectURL);
}

function findNonMembers(members, allUsers) {
	var nonMembers = new ArrayList();
	for (var it = allUsers.iterator(); it.hasNext(); ) {	
		var user = it.next();
		if (! members.contains(user)) {
			nonMembers.add(user);
		}
	}
	return nonMembers;
}

function loadModelWithMembership(members, nonMembers) {

	var editModel = model_.editMembersModel;

	var i = 0;
	var it = members.iterator()
	while (it.hasNext()) {	
		var member = it.next();
		editModel.members[i].userId = member.id;
		editModel.members[i].name = member.name;
		editModel.members[i].surname = member.surname;
		editModel.members[i].skills = member.skills;
		i++;
	}
	
	i = 0;
	var it = nonMembers.iterator();	
	while (it.hasNext()) {	
		var nonMember = it.next();
		editModel.nonMembers[i].userId = nonMember.id;
		editModel.nonMembers[i].name = nonMember.name;
		editModel.nonMembers[i].surname = nonMember.surname;
		editModel.nonMembers[i].skills = nonMember.skills;
		i++;
	}
}

function processCreateUser(form) {
    // do this before showing the form, because new cocon requests 
    // delete existing params
	//var redirectURL = cocoon.parameters["redirectURL"];
	var refererURL = cocoon.parameters["refererURL"];
	
	model_ = form.getModel();
	form_ = form;
	
	activateUserForm();
	setCreateUserStandaloneMode();
	
	form.showForm("edit-members.instance");
	if (form.submitId != "cancelSubmit") {
		createUser();
	}
	cocoon.redirectTo("cocoon:/redirectTo/" + refererURL);
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
	
	print("save");
	var userQuery = cocoon.getComponent(UserQuery.ROLE);
	var teamAdmin = cocoon.getComponent(TeamspaceAdmin.ROLE);
	
	var editedTeam = teamAdmin.teamspaceById(editedTeamId_);	
	var members = editedTeam.getUsers();
	
	print("amount: " + addedUsers_.size());
	
	for (var i = 0; i < addedUsers_.size(); i++) {
		var userId = addedUsers_.get(i);
		var user = userQuery.userById(userId);
		print("added " + userId);
		if (! members.contains(user)) {
			print("new member: " + userId);
			teamAdmin.addMember(user, editedTeam);
			print("2 new member: " + userId);
		}
	}
	
	for (var i = 0; i < removedUsers_.size(); i++) {
		var userId = removedUsers_.get(i);
		var user = userQuery.userById(userId);
		print("removed userId");
		if (members.contains(user)) {			
			teamAdmin.removeMember(user, editedTeam);
			print("old member userId");
		}
	}
}

function memberIds(members) {
	var memberIds = new ArrayList();
	for (var i = 0; i < members.size(); i++) {
		memberIds.add(members.get(i).getId());
	}
	return memberIds;
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

function removeUserFromMembers(modelUser) {
	var editModel = model_.editMembersModel;
	
	var userId = modelUser.userId;
		
	if (addedUsers_.contains(userId))
		addedUsers_.remove(userId);
	else
		removedUsers_.add(modelUser.userId);
		
	addUserToEndOfList(modelUser, editModel.nonMembers);
}

function addUserToMembers(modelUser) {
	var userId = modelUser.userId;

	if (removedUsers_.contains(userId))
		removedUsers_.remove(userId);
	else
		addedUsers_.add(modelUser.userId);
	
	addUserToEndOfList(modelUser, model_.editMembersModel.members);
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
