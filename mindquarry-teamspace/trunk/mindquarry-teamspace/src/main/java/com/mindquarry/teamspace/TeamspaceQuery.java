/**
 * Copyright (C) 2005 MindQuarry GmbH, All Rights Reserved
 */
package com.mindquarry.teamspace;

import java.util.List;

/**
 * 
 *
 * @author <a href="bastian(dot)steinert(at)mindquarry(dot)com">Bastian Steinert</a>
 */
public interface TeamspaceQuery {

	/**
	 * @param name, the id of the project
	 * @return return the uri to the corresponding
     * workspace, e.g. file:///tmp/mindquarry/webapp/
	 */
	String workspaceUri(String id);
    
    /**
     * return the list of all teamspaces the user participates in;
     * each teamspace in the result list contains also
     * a list of all participating users. 
     */
    List<TeamspaceRO> teamspacesForUser(String userId);
    
    List<UserRO> allUsers();
    
    UserRO userForId(String userId);
    
    TeamspaceRO teamspaceForId(String teamspaceId);
    
    Membership membership(TeamspaceRO teamspace);
    
    /**
     * update the current members of a teamspace
     * @param membership, contains lists of up to date members 
     * and users that should be members after update
     * @returns 
     */
    Membership updateMembership(Membership membership);
}
