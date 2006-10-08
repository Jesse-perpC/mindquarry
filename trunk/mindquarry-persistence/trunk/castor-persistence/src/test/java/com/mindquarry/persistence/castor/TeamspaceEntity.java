/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.persistence.castor;

import com.mindquarry.common.persistence.EntityBase;

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
}
