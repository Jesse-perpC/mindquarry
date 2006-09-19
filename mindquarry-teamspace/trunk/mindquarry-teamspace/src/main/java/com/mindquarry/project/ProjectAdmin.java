/**
 * 
 */
package com.mindquarry.project;


/**
 * @author bastian
 *
 */
public interface ProjectAdmin extends ProjectQuery {

	void create(String name) throws ProjectAlreadyExistsException;
	
	void remove(String name);
}
