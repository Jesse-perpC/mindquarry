/**
 * Copyright (C) 2005 MindQuarry GmbH, All Rights Reserved
 */
package com.mindquarry.project;

import java.util.List;

/**
 * 
 *
 * @author <a href="bastian(dot)steinert(at)mindquarry(dot)com">Bastian Steinert</a>
 */
public interface ProjectQuery {

	/**
	 * @param name, the name (id) of the project
	 * @return the absolute path to the dma repository
	 * (without concluding separator)
	 */
	String repositoryPath(String name);
    
    List<ProjectRO> list();
}
