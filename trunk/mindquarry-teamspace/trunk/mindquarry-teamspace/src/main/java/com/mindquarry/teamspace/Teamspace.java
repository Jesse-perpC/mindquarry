/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.teamspace;

import java.util.List;

/**
 * Add summary documentation here.
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">your full name</a>
 */
public interface Teamspace extends TeamspaceRO {

    /**
     * Setter for description.
     *
     * @param description the description to set
     */
    public void setDescription(String description);

    /**
     * Setter for name.
     *
     * @param name the name to set
     */
    public void setName(String name);

    /**
     * Setter for workspaceUri.
     *
     * @param workspaceUri the workspaceUri to set
     */
    public void setWorkspaceUri(String workspaceUri);

    /**
     * Getter for users.
     *
     * @return an umodifiable view of the users participating this project
     */
    public List<UserRO> getUsers();
}