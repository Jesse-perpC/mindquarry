/**
 * 
 */
package com.mindquarry.teamspace;


/**
 * @author bastian
 *
 */
public interface TeamspaceAdmin extends TeamspaceQuery {

	void create(String name) throws TeamspaceAlreadyExistsException;
	
	void remove(String name);
}
