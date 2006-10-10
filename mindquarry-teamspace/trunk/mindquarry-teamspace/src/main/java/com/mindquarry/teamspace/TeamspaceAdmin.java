/**
 * 
 */
package com.mindquarry.teamspace;


/**
 * @author bastian
 *
 */
public interface TeamspaceAdmin extends TeamspaceQuery {

	TeamspaceRO createTeamspace(String id, String name, String description);
	
	void removeTeamspace(String id);
    
	UserRO createUser(String id, String name);
    
    void removeUser(String id);
}
