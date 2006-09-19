/**
 * 
 */
package com.mindquarry.project;

import java.util.List;

/**
 * @author bastian
 *
 */
public interface ProjectAdmin {

	void create(String name) throws ProjectAlreadyExistsException;
	
	List<ProjectRO> list();
	
	void remove(String name);
}
