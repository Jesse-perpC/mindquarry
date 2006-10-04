/*
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
 
function teamspaceView() {

	var teamspaceQueryName = "com.mindquarry.teamspace.TeamspaceQuery";
    var teamspaceManager = cocoon.getComponent(teamspaceQueryName);
    
    var teamspaceList = { "teamspaceList" : teamspaceManager.list() }
    cocoon.sendPage("views/teamspaceView", teamspaceList);
}