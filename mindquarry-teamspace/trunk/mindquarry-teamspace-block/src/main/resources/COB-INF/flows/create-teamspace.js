/*
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
cocoon.load("resource://org/apache/cocoon/forms/flow/javascript/Form.js");

importClass(Packages.com.mindquarry.teamspace.TeamspaceAdmin);
importClass(Packages.com.mindquarry.user.UserAdmin);

function processCreateTeamspaceForm(form) {

	var userAdmin = cocoon.getComponent(UserAdmin.ROLE);
	var teamspaceAdmin = cocoon.getComponent(TeamspaceAdmin.ROLE);
	
	var userId = cocoon.parameters["username"];
	//print("userId: " + userId);
	var user = userAdmin.userById(userId);
	
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