/**
 * 
 */
package com.mindquarry.teamspace;


/**
 * @author bastian
 *
 */
public interface TeamspaceAdmin extends TeamspaceQuery {

	TeamspaceRO createTeamspace(String id, String name, 
                String description, UserRO teamspaceCreator);
	
	void removeTeamspace(String id);
    
	UserRO createUser(String id, String name, String email);
    
    void removeUser(String id);
}
