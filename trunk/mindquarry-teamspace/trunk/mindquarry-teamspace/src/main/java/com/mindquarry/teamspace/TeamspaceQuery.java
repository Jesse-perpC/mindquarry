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
    
    List<TeamspaceRO> allTeamspaces();
    
    List<TeamspaceRO> teamspacesForUser(String userId);
}
