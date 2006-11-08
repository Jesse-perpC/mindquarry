/*
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
cocoon.load("resource://org/apache/cocoon/forms/flow/javascript/Form.js");

function processCreateTeamspaceForm(form) {

	var lookupName = "com.mindquarry.teamspace.TeamspaceAdmin";
	var teamspaceAdmin = cocoon.getComponent(lookupName);
	
	var userId = cocoon.parameters["username"];
	//print("userId: " + userId);
	var user = teamspaceAdmin.userForId(userId);
	
	form.showForm("create-teamspace.instance");
	if (form.submitId == "cancelSubmit") {
      // the user pressed cancel
    } else {
      // the user pressed ok and the form is valid
      var model = form.getModel();	
      teamspaceAdmin.createTeamspace(
      model.teamspaceId, model.name, model.description, user);
    }	
	cocoon.redirectTo("/teamspace/");
}