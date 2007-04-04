/*
 * Copyright (C) 2006-2007 Mindquarry GmbH, All Rights Reserved
 * 
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 */
package com.mindquarry.persistence.mock;

import java.util.Set;

import com.mindquarry.persistence.api.EntityBase;

/**
 * Add summary documentation here.
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">your full name</a>
 */
public class TeamspaceEntity extends EntityBase {

    private String name;
    private String description;
    private String workspaceUri;
    private Set<UserEntity> users;

    
    /**
     * 
     */
    public TeamspaceEntity() {
        this.id = "".intern();
        this.name = "".intern();
        this.description = "".intern();
        this.workspaceUri = "".intern();
    }

    /**
     * @param id
     * @param name
     * @param description
     * @param workspaceUri
     */
    public TeamspaceEntity(String id, String name, String description, String workspaceUri) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.workspaceUri = workspaceUri;
    }
    
    /**
     * @see com.mindquarry.types.teamspace.Teamspace#getDescription()
     */
    public String getDescription() {
        return description;
    }
    /**
     * @see com.mindquarry.types.teamspace.Teamspace#setDescription(java.lang.String)
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @see com.mindquarry.types.teamspace.Teamspace#getName()
     */
    public String getName() {
        return name;
    }
    /**
     * @see com.mindquarry.types.teamspace.Teamspace#setName(java.lang.String)
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @see com.mindquarry.types.teamspace.Teamspace#getWorkspaceUri()
     */
    public String getWorkspaceUri() {
        return workspaceUri;
    }
    /**
     * @see com.mindquarry.types.teamspace.Teamspace#setWorkspaceUri(java.lang.String)
     */
    public void setWorkspaceUri(String workspaceUri) {
        this.workspaceUri = workspaceUri;
    }

    /**
     * Getter for users.
     *
     * @return the users
     */
    public Set<UserEntity> getUsers() {
        return users;
    }

    /**
     * Setter for users.
     *
     * @param users the users to set
     */
    public void setUsers(Set<UserEntity> users) {
        this.users = users;
    }
}
