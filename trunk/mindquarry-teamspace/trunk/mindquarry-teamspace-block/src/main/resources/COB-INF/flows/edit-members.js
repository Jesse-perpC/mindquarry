/*
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */

function editMembers() {

	var teamspaceId = cocoon.parameters.teamspaceId;
	
	var lookupName = "com.mindquarry.teamspace.TeamspaceQuery";
	var teamspaceQuery = cocoon.getComponent(lookupName);
	
	var editedTeamspace = teamspaceQuery.teamspaceForId(teamspaceId);	
	var membership = teamspaceQuery.membership(editedTeamspace);

	while (true) {
		print("show members");
		cocoon.sendPageAndWait("views/membersView", 
							{ "teamspace" : editedTeamspace
							 ,"members" : membership.getMembers()
							 ,"nonMembers" : membership.getNonMembers() } );
	}
}