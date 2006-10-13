/*
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */

cocoon.load("resource://org/apache/cocoon/forms/flow/javascript/Form.js");

var membership_;
var model_;

function editMembers(form) {
	var teamspaceId = cocoon.parameters.teamspaceId;
	
	var lookupName = "com.mindquarry.teamspace.TeamspaceQuery";
	var teamspaceQuery = cocoon.getComponent(lookupName);
	
	var editedTeamspace = teamspaceQuery.teamspaceForId(teamspaceId);	
	membership_ = teamspaceQuery.membership(editedTeamspace);
	
	
	model_ = form.getModel();
	
	var members = membership_.getMembers();
		
	for (var i = 0; i < members.size(); i++) {
		model_.members[i].userId = members.get(i).id;
		model_.members[i].name = members.get(i).name;
	}
	
	var nonMembers = membership_.getNonMembers();
		
	for (var i = 0; i < nonMembers.size(); i++) {
		model_.nonMembers[i].userId = nonMembers.get(i).id;
		model_.nonMembers[i].name = nonMembers.get(i).name;
	}
	
	form.showForm("edit-members.plain-instance");
	
	print("finish");
	cocoon.redirectTo("../..");
}

function saveMembershipChanges(event) {
	print("saveMembershipChanges")
}

function removeMember(event) {
	var rowIndex = fetchRowIndex(event);
	var modelUser =	model_.members[rowIndex];
	addUserToEndOfList(modelUser, model_.nonMembers);
}

function addMember(event) {	
	var rowIndex = fetchRowIndex(event);
	var modelUser =	model_.nonMembers[rowIndex];
	addUserToEndOfList(modelUser, model_.members);
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