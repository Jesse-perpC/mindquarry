/*
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
function listTeamspacesForUser() {
	var teamspaceQueryName = "com.mindquarry.teamspace.TeamspaceQuery";
    var teamspaceQuery = cocoon.getComponent(teamspaceQueryName);
    
    var userId = cocoon.parameters["username"];
    var teamspaces = teamspaceQuery.teamspacesForUser(userId);
    
    var parameterMap = { "teamspaces" : teamspaces }
    var target = cocoon.parameters["target"];
    cocoon.sendPage(target, parameterMap);
}

function teamspacesByID() {
	var teamspaceQueryName = "com.mindquarry.teamspace.TeamspaceQuery";
    var teamspaceQuery = cocoon.getComponent(teamspaceQueryName);
    
    var id = cocoon.parameters["teamspaceID"];
    var teamspace = teamspaceQuery.teamspaceForId(id);
    
    var parameterMap = { "teamspace" : teamspace }
    var target = cocoon.parameters["target"];
    cocoon.sendPage(target, parameterMap);
}
