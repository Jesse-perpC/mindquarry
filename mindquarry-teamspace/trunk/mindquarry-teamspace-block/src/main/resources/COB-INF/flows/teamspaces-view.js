/*
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
 
importClass(Packages.com.mindquarry.teamspace.TeamspaceQuery);
importPackage(Packages.com.mindquarry.user);

function listTeamspacesForUser() {

    var teamspaceQuery = cocoon.getComponent(TeamspaceQuery.ROLE);
    
    var userId = cocoon.parameters["username"];
    var teamspaces = teamspaceQuery.teamspacesForUser(userId);
    
    var parameterMap = { "teamspaces" : teamspaces }
    var target = cocoon.parameters["target"];
    cocoon.sendPage(target, parameterMap);
}

function teamspacesByID() {

	var teamspaceQuery = cocoon.getComponent(TeamspaceQuery.ROLE);
    
    var id = cocoon.parameters["teamspaceID"];
    var teamspace = teamspaceQuery.teamspaceForId(id);
    
    var parameterMap = { "teamspace" : teamspace }
    var target = cocoon.parameters["target"];
    cocoon.sendPage(target, parameterMap);
}
