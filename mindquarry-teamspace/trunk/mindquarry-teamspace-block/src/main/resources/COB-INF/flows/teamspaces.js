/*
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
cocoon.load("resource://org/apache/cocoon/forms/flow/javascript/Form.js");

importClass(Packages.com.mindquarry.teamspace.TeamspaceAdmin);
importClass(Packages.com.mindquarry.user.UserAdmin);

function processCreateTeamspaceForm(form) {

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

function teamspaceById(teamspaceId) {
	var teamspaceAdmin = cocoon.getComponent(TeamspaceAdmin.ROLE);
	var result = teamspaceAdmin.teamspaceById(teamspaceId);
	cocoon.releaseComponent(teamspaceAdmin);
	return result;
}

function processEditTeamspaceForm(form) {
	
	var blockPath = cocoon.parameters["blockPath"];
	
	var teamspaceId = cocoon.parameters["teamspaceId"];
	var teamspace = teamspaceById(teamspaceId);
	
	var targetUri = cocoon.request.getParameter("targetUri");
	
	form.getModel().teamspaceId = teamspaceId;
	form.getModel().name = teamspace.name;
	form.getModel().description = teamspace.description;
	
	form.showForm("edit-teamspace.instance");
	
	if (form.submitId != "cancel") {
		var teamspaceAdmin = cocoon.getComponent(TeamspaceAdmin.ROLE);
		
		teamspace.name = form.getModel().name;
		teamspace.description = form.getModel().description;
		teamspaceAdmin.updateTeamspace(teamspace);
      
    	cocoon.releaseComponent(teamspaceAdmin);
    }
	
	cocoon.redirectTo(blockPath + "/");
}