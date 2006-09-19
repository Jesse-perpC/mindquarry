/**
 * Copyright (C) 2005 MindQuarry GmbH, All Rights Reserved
 */
package com.mindquarry.project.file;

import com.mindquarry.project.ProjectRO;

/**
 * 
 *
 * @author <a href="bastian(dot)steinert(at)mindquarry(dot)com">Bastian Steinert</a>
 */
public class Project implements ProjectRO {
	
	private String name_;

	/**
	 * default constructor
	 */
	public Project() {
	}

	/**
	 * @param name
	 * @param title
	 */
	public Project(String name) {
		super();
		name_ = name;
	}

	public String getName() {
		return name_;
	}

	public void setName(String name) {
		name_ = name;
	}
}
