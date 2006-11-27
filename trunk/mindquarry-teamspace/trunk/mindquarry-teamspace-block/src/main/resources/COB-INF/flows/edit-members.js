/*
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
cocoon.load("resource://org/apache/cocoon/forms/flow/javascript/Form.js");

importPackage(Packages.com.mindquarry.teamspace);
importPackage(Packages.com.mindquarry.user);

importClass(Packages.com.mindquarry.teamspace.TeamspaceQuery);
importClass(Packages.org.apache.cocoon.environment.SourceResolver);
importClass(Packages.org.apache.cocoon.forms.formmodel.WidgetState);

var teamspaceQuery_;
var membership_;
var model_;
var form_;

function processEditMembersForm(form) {

	var teamspaceId = cocoon.parameters["teamId"];

	teamspaceQuery_ = cocoon.getComponent(TeamspaceQuery.ROLE);
	
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

function processCreateUser(form) {
	model_ = form.getModel();
	form_ = form;
	
	activateUserForm();
	setCreateUserStandaloneMode();
		
	form.showForm("edit-members.instance");
	
	if (form.submitId != "cancelSubmit")
		createUser();
		
	cocoon.redirectTo("/teamspace/");
}

function processEditUser(form) {
	model_ = form.getModel();
	form_ = form;
	
	activateUserForm();
	
	var targetUri = cocoon.request.getParameter("targetUri");
	
	var currentUser = userById(cocoon.request.getAttribute("username"));
	model_.userModel.userId = currentUser.id;
	model_.userModel.name = currentUser.name;
	model_.userModel.surname = currentUser.surname;
	model_.userModel.email = currentUser.email;
	model_.userModel.skills = currentUser.skills;
	
	form.showForm("edit-user.instance");
	
	if (form.submitId != "cancelSubmit") {
		//createUser();
	}
		
	cocoon.redirectTo(targetUri);
}

function userById(userId) {
	var userAdmin = cocoon.getComponent(UserAdmin.ROLE);	
	var result = userAdmin.userById(userId);	
	cocoon.releaseComponent(userAdmin);
	return result;
}

function updateUser() {

	var userModel = model_.userModel;
	
	if (containsPhotoUpload(form_))
	    persistUserPhoto(userModel.userId, form_);
	    	
	var userAdmin = cocoon.getComponent(UserAdmin.ROLE);
	
	var result = userAdmin.createUser(
			userModel.userId, userModel.password, userModel.name, 
			userModel.surname, userModel.email, userModel.skills);
	
	cocoon.releaseComponent(userAdmin);
	
	return result;
}

function createUser() {

	var userModel = model_.userModel;
	
	if (containsPhotoUpload(form_))
	    persistUserPhoto(userModel.userId, form_);
	    	
	var userAdmin = cocoon.getComponent(UserAdmin.ROLE);
	
	var result = userAdmin.createUser(
			userModel.userId, userModel.password, userModel.name, 
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
	var resolver = cocoon.getComponent(SourceResolver.ROLE);
    var result = resolver.resolveURI(uri);
    cocoon.releaseComponent(resolver);
    return result;
}

function activateUserForm() {
	model_.editMembersModel.state = WidgetState.INVISIBLE;
	model_.userModel.state = WidgetState.ACTIVE;
}

function activateEditMembersForm() {
	model_.editMembersModel.state = WidgetState.ACTIVE;
	model_.userModel.state = WidgetState.INVISIBLE;
}

function setCreateUserStandaloneMode() {	
	model_.userModel.createUserSubmit.state = WidgetState.ACTIVE;
	model_.userModel.cancelSubmit.state = WidgetState.ACTIVE;
	
	model_.userModel.createUserAction.state = WidgetState.INVISIBLE;
	model_.userModel.cancelAction.state = WidgetState.INVISIBLE;
}

function setCreateUserEmbeddedMode() {	
	model_.userModel.createUserSubmit.state = WidgetState.INVISIBLE;
	model_.userModel.cancelSubmit.state = WidgetState.INVISIBLE;
		
	model_.userModel.createUserAction.state = WidgetState.ACTIVE;
	model_.userModel.cancelAction.state = WidgetState.ACTIVE;
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
