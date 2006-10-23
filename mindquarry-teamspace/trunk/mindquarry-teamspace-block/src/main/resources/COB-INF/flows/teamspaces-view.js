/*
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
function listTeamspacesForUser() {
	var teamspaceQueryName = "com.mindquarry.teamspace.TeamspaceQuery";
    var teamspaceQuery = cocoon.getComponent(teamspaceQueryName);
    
    var userId = cocoon.parameters["username"];
    var teamspaces = teamspaceQuery.teamspacesForUser(userId);
    
    var parameterMap = { "teamspaces" : teamspaces }
    var view = cocoon.parameters["target"];
    cocoon.sendPage(view, parameterMap);
}
