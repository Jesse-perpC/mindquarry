/**
 * Copyright (C) 2005 MindQuarry GmbH, All Rights Reserved
 */
package com.mindquarry.teamspace.file;

import java.util.List;

import com.mindquarry.teamspace.TeamspaceRO;
import com.mindquarry.teamspace.UserRO;

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

    public String getDescription() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getId() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getWorkspaceUri() {
        // TODO Auto-generated method stub
        return null;
    }

    public List<UserRO> getUsers() {
        // TODO Auto-generated method stub
        return null;
    }
}
