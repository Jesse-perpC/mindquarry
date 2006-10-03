/**
 * 
 */
package com.mindquarry.teamspace;


/**
 * @author bastian
 *
 */
public interface TeamspaceAdmin extends TeamspaceQuery {

	void create(String id, String name, String description);
	
	void remove(String id);
}
