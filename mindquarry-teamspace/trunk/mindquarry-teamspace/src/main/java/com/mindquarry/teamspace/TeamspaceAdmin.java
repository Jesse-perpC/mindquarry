/**
 * 
 */
package com.mindquarry.teamspace;

import com.mindquarry.user.UserRO;


/**
 * @author bastian
 *
 */
public interface TeamspaceAdmin extends TeamspaceQuery {

    public static final String ROLE = TeamspaceAdmin.class.getName();
    
    /**
     * creates a new teamspace and the associated workspace (svn repo)
     * @param teamspaceId
     * @param name of the teamspace
     * @param a String describing the teamspace
     * @param the user that triggered the creation of the new teamspace 
     */
	TeamspaceDefinition createTeamspace(String id, String name, 
                String description, UserRO teamspaceCreator);
	
    TeamspaceDefinition teamspaceDefinitionForId(String teamspaceId);
    
    void updateTeamspace(TeamspaceDefinition teamspace);
    
	void removeTeamspace(String teamspaceId);
}
