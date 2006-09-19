/*
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
 
function teamspaceView() {

    var teamspaceQuery = cocoon.getComponent("teamspaceQuery");
    
    var teamspaceList = { "teamspaceList" : teamspaceQuery.list() }
    cocoon.sendPage("screens/teamspaceView", teamspaceList);
}