/**
 * Copyright (C) 2005 MindQuarry GmbH, All Rights Reserved
 */
package com.mindquarry.teamspace.file;

import com.mindquarry.teamspace.TeamspaceRO;

/**
 * 
 *
 * @author <a href="bastian(dot)steinert(at)mindquarry(dot)com">Bastian Steinert</a>
 */
public class Teamspace implements TeamspaceRO {
	
	private String name_;

	/**
	 * default constructor
	 */
	public Teamspace() {
	}

	/**
	 * @param name
	 * @param title
	 */
	public Teamspace(String name) {
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
