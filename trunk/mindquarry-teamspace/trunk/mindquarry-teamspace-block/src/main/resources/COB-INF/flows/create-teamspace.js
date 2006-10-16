/*
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */

cocoon.load("resource://org/apache/cocoon/forms/flow/javascript/Form.js");

function processCreateTeamspaceForm(form) {

	var lookupName = "com.mindquarry.teamspace.TeamspaceAdmin";
	var teamspaceAdmin = cocoon.getComponent(lookupName);
	
	var userId = "bastian";
	var user = teamspaceAdmin.userForId(userId);
	
	if (null == user) {
		cocoon.redirectTo("../createUser/");
	}
	
	form.showForm("create-teamspace.instance");
	
	var model = form.getModel();	
	
	teamspaceAdmin.createTeamspace(
		model.teamspaceId, model.name, model.description, user);
	
	cocoon.redirectTo("../");
}