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

importClass(Packages.com.mindquarry.teamspace.TeamspaceAdmin);
importClass(Packages.com.mindquarry.user.UserAdmin);

var model_;

function processCreateTeamspaceForm(form) {
	model_ = form.getModel();
	var blockPath = cocoon.parameters["blockPath"];
	
	var userId = cocoon.parameters["username"];
	var user = userById(userId);
	
	form.showForm("create-teamspace.instance");
	if (form.submitId != "cancel") {
      // the user pressed ok and the form is valid
      var teamspaceAdmin = cocoon.getComponent(TeamspaceAdmin.ROLE);
      
      var model = form.getModel();
      teamspaceAdmin.createTeamspace(
      model.teamspaceId, model.name, model.description, user);
      
      cocoon.releaseComponent(teamspaceAdmin);
    }	
	cocoon.redirectTo(blockPath + "/");
}

function existsTeamspaceWithId(teamspaceIdWidget) {
	var teamspaceExists = (null != teamspaceById(model_.teamspaceId));
	if (teamspaceExists) {
		teamspaceIdWidget.setValidationError(
			new Packages.org.apache.cocoon.forms.validation.ValidationError("teamspaceId already exists!"));
	}
	return false;
}

function teamspaceById(teamspaceId) {
	var teamspaceAdmin = cocoon.getComponent(TeamspaceAdmin.ROLE);
	var result = teamspaceAdmin.teamspaceById(teamspaceId);
	cocoon.releaseComponent(teamspaceAdmin);
	return result;
}

function processEditTeamspaceForm(form) {

	var redirectURL = cocoon.parameters["redirectURL"];
	
	var teamspaceId = cocoon.parameters["teamspaceId"];
	var teamspace = teamspaceById(teamspaceId);
	
	var targetUri = cocoon.request.getParameter("targetUri");
	
	// change state of field teamspaceId to turn off the duplicateId validator
	var WidgetState = Packages.org.apache.cocoon.forms.formmodel.WidgetState;
	form.lookupWidget("teamspaceId").state = WidgetState.DISABLED;
	
	form.getModel().name = teamspace.name;
	form.getModel().description = teamspace.description;
	
	form.showForm("edit-teamspace.instance");
	
	//print(form.submitId);
	if (form.submitId != "cancel") {
		var teamspaceAdmin = cocoon.getComponent(TeamspaceAdmin.ROLE);
		
		teamspace.name = form.getModel().name;
		teamspace.description = form.getModel().description;
		teamspaceAdmin.updateTeamspace(teamspace);
      
    	cocoon.releaseComponent(teamspaceAdmin);
    }
	
	cocoon.redirectTo(redirectURL + "/");
}