/*
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
 
function teamspaceView() {

    var teamspaceManager = cocoon.getComponent("teamspaceManager");
    
    var teamspaceList = { "teamspaceList" : teamspaceManager.list() }
    cocoon.sendPage("views/teamspaceView", teamspaceList);
}