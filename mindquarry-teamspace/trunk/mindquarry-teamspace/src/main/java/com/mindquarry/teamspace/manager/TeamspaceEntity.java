/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.teamspace.manager;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.mindquarry.common.persistence.EntityBase;
import com.mindquarry.teamspace.TeamspaceRO;
import com.mindquarry.teamspace.UserRO;


/**
 * Add summary documentation here.
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">your full name</a>
 */
public class TeamspaceEntity extends EntityBase implements TeamspaceRO {

    private String name;
    private String description;
    private String workspaceUri;
    private List<UserRO> users;

    
    /**
     * 
     */
    public TeamspaceEntity() {
        this.id = "".intern();
        this.name = "".intern();
        this.description = "".intern();
        this.workspaceUri = "".intern();
        this.users = new LinkedList<UserRO>();
    }

    /**
     * @param id
     * @param name
     * @param description
     * @param workspaceUri
     */
    public TeamspaceEntity(String id, String name, 
            String description, String workspaceUri) {
        
        this.id = id;
        this.name = name;
        this.description = description;
        this.workspaceUri = workspaceUri;
        this.users = new LinkedList<UserRO>();
    }
    
    /**
     * @see com.mindquarry.teamspace.TeamspaceRO#getDescription()
     */
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @see com.mindquarry.teamspace.TeamspaceRO#getName()
     */
    public String getName() {
        return name;
    }
    
    
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * @see com.mindquarry.teamspace.TeamspaceRO#getWorkspaceUri()
     */
    public String getWorkspaceUri() {
        return workspaceUri;
    }
    
    
    public void setWorkspaceUri(String workspaceUri) {
        this.workspaceUri = workspaceUri;
    }
    
    
    public List<UserRO> getUsers() {
        List<UserRO> userList = new LinkedList<UserRO>(users);   
        return Collections.unmodifiableList(userList);
    }
    
    void setUsers(List<UserRO> value) {
        this.users = value;
    }
}
