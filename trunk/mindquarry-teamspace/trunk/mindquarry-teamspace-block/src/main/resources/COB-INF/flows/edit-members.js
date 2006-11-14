/*
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
cocoon.load("resource://org/apache/cocoon/forms/flow/javascript/Form.js");

var WidgetState = org.apache.cocoon.forms.formmodel.WidgetState;
var teamspaceQuery_;
var membership_;
var model_;
var form_;

function processEditMembersForm(form) {
	var teamspaceId = cocoon.parameters["teamId"];
	//print("editing members for team: " + teamspaceId);
	
	var lookupName = "com.mindquarry.teamspace.TeamspaceQuery";
	teamspaceQuery_ = cocoon.getComponent(lookupName);
	
	var editedTeamspace = teamspaceQuery_.teamspaceForId(teamspaceId);	
	membership_ = teamspaceQuery_.membership(editedTeamspace);
	
	model_ = form.getModel();
	form_ = form;
	model_.teamspaceName = editedTeamspace.name;
	
	loadModelWithMembership();
	
	activateEditMembersForm();
	setCreateUserEmbeddedMode();
	form.showForm("edit-members.instance");
	
	cocoon.redirectTo("/teamspace/");
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

function processCreateUserForm(form) {
	model_ = form.getModel();
	form_ = form;
	
	activateCreateUserForm();
	setCreateUserStandaloneMode();
	
	form.showForm("edit-members.instance");
	print(form.submitId);
	if (form.submitId == "cancelSubmit") {
		print("do not create users");
	} else {
		var userModel = model_.createUserModel;
		print("trying to upload an image");
		var uploadWidget = form_.lookupWidget("/createUserModel/photo");
		print("found widget: " + uploadWidget);
		var resolver = cocoon.getComponent(Packages.org.apache.cocoon.environment.SourceResolver.ROLE);
	    var source = resolver.resolveURI("jcr:///users/" + userModel.userId + ".png");
	    print("found source: " + source);
	    var value = uploadWidget.getValue();
	    print("value: " + value);
	    try {
	    	value.copyToSource(source);
	    	print("copied image to " + source.getURI());
	    } catch (e) {
	    	print("unable to save image " + e);
	    }
		
		var lookupName = "com.mindquarry.teamspace.TeamspaceAdmin";
		var teamspaceAdmin = cocoon.getComponent(lookupName);
		
		var user = teamspaceAdmin.createUser(
				userModel.userId, userModel.password, userModel.name, 
				userModel.surname, userModel.email, userModel.skills);
		
		print("successfully created user: " + user);
	}
	cocoon.redirectTo("/teamspace/");
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
	var uploadWidget = form_.lookupWidget("/createUserModel/photo");
	var resolver = cocoon.getComponent(Packages.org.apache.cocoon.environment.SourceResolver.ROLE);
    var source = resolver.resolveURI("jcr:///users/" + userModel.userId + ".png");
    var value = uploadWidget.getValue();
    
	try {
    	value.copyToSource(source);
    } catch (e) {
    	e.printStackTrace();
    }
	var lookupName = "com.mindquarry.teamspace.TeamspaceAdmin";
	var teamspaceAdmin = cocoon.getComponent(lookupName);
	
	return teamspaceAdmin.createUser(
			userModel.userId, userModel.password, userModel.name, 
			userModel.surname, userModel.email, userModel.skills);
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
