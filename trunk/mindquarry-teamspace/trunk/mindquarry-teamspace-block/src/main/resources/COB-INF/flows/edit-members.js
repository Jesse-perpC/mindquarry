/*
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */

cocoon.load("resource://org/apache/cocoon/forms/flow/javascript/Form.js");

var WidgetState = org.apache.cocoon.forms.formmodel.WidgetState;
var teamspaceQuery_;
var membership_;
var model_;

function processEditMembersForm(form) {
	
	var teamspaceId = cocoon.parameters["teamId"];
	print("editing members for team: " + teamspaceId);
	
	var lookupName = "com.mindquarry.teamspace.TeamspaceQuery";
	teamspaceQuery_ = cocoon.getComponent(lookupName);
	
	var editedTeamspace = teamspaceQuery_.teamspaceForId(teamspaceId);	
	membership_ = teamspaceQuery_.membership(editedTeamspace);
	
	model_ = form.getModel();
	model_.teamspaceName = editedTeamspace.name;
	
	loadModelWithMembership();
	
	activateEditMembersForm();
	setCreateUserEmbeddedMode();
	form.showForm("edit-members.instance");
	
	cocoon.redirectTo("/");
}

function loadModelWithMembership() {

	var editModel = model_.editMembersModel;
	
	var members = membership_.getMembers();
		
	for (var i = 0; i < members.size(); i++) {
		editModel.members[i].userId = members.get(i).id;
		editModel.members[i].name = members.get(i).name;
	}
	
	var nonMembers = membership_.getNonMembers();
		
	for (var i = 0; i < nonMembers.size(); i++) {
		editModel.nonMembers[i].userId = nonMembers.get(i).id;
		editModel.nonMembers[i].name = nonMembers.get(i).name;
	}
}

function processCreateUserForm(form) {
	
	model_ = form.getModel();
	
	activateCreateUserForm();
	setCreateUserStandaloneMode();
	
	form.showForm("edit-members.instance");
	
	print("creating user...");

	cocoon.redirectTo("/");
}

function activateCreateUserForm() {
	model_.editMembersModel.state = WidgetState.INVISIBLE;
	model_.createUserModel.state = WidgetState.ACTIVE;
}

function activateEditMembersForm() {
	model_.editMembersModel.state = WidgetState.ACTIVE;
	model_.createUserModel.state = WidgetState.INVISIBLE;
}

function setCreateUserStandaloneMode() {	
	model_.createUserModel.createUserSubmit.state = WidgetState.ACTIVE;
	model_.createUserModel.cancelSubmit.state = WidgetState.ACTIVE;
	
	model_.createUserModel.createUserAction.state = WidgetState.INVISIBLE;
	model_.createUserModel.cancelAction.state = WidgetState.INVISIBLE;
}

function setCreateUserEmbeddedMode() {	
	model_.createUserModel.createUserSubmit.state = WidgetState.INVISIBLE;
	model_.createUserModel.cancelSubmit.state = WidgetState.INVISIBLE;
	
	model_.createUserModel.createUserAction.state = WidgetState.ACTIVE;
	model_.createUserModel.cancelAction.state = WidgetState.ACTIVE;
}

function createUser() {
	
	var userModel = model_.createUserModel;
	
	var lookupName = "com.mindquarry.teamspace.TeamspaceAdmin";
	var teamspaceAdmin = cocoon.getComponent(lookupName);
	
	return teamspaceAdmin.createUser(
			userModel.userId, userModel.name, userModel.email);
}

function saveMembershipChanges(event) {
	teamspaceQuery_.updateMembership(membership_);
}

function removeMember(event) {
	var editModel = model_.editMembersModel;
	
	var rowIndex = fetchRowIndex(event);
	var modelUser =	editModel.members[rowIndex];
	
	membership_.removeMember(modelUser.userId);
	addUserToEndOfList(modelUser, editModel.nonMembers);
}

function addMember(event) {	
	var editModel = model_.editMembersModel;
	
	var rowIndex = fetchRowIndex(event);
	var modelUser =	editModel.nonMembers[rowIndex];
	
	addUserToMembers(modelUser);
}

function addUserToMembers(user) {
	membership_.addMember(user.userId);
	addUserToEndOfList(user, model_.editMembersModel.members);
}

function addUserToEndOfList(user, list) {
	var i = list.length;
	list[i].userId = user.userId;
	list[i].name = user.name;
}

function fetchRowIndex(event) {
	var repeaterClazz = org.apache.cocoon.forms.formmodel.Repeater;
	var row = repeaterClazz.getParentRow(event.getSourceWidget());
	var repeater = row.parent;
	return repeater.indexOf(row);	
}