/**
 * 
 */
package com.mindquarry.teamspace;


/**
 * @author bastian
 *
 */
public interface TeamspaceAdmin extends TeamspaceQuery {

    /**
     * creates a new teamspace
     * @param teamspaceId
     * @param name of the teamspace
     * @param a String describing the teamspace
     * @param the user that triggered the creation of the new teamspace 
     */
	TeamspaceRO createTeamspace(String id, String name, 
                String description, UserRO teamspaceCreator);
	
	void removeTeamspace(String teamspaceId);
    
    /**
     * creates a new user account
     * @param userId
     * @param password
     * @param first name of the new user
     * @param last name of the new user
     * @param the user's email address
     * @param the skills of the user
     */
	UserRO createUser(String id, String password, 
            String name, String surName, String email, String skills);
    
    /**
     * change the password of a user if the old password matches
     * 
     * @param a user
     * @param the old password
     * @param the new password
     * @returns true if change succeeds otherwise false
     */
    boolean changePassword(String userId, 
            String oldPassword, String newPassword);
    
    void removeUser(String userId);
}
