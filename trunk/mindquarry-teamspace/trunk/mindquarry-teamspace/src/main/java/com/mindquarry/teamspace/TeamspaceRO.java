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
public interface TeamspaceRO {

    /**
     * Getter for description.
     *
     * @return the description
     */
    public String getDescription();

    /**
     * Getter for id.
     *
     * @return the id
     */
    public String getId();

    /**
     * Getter for name.
     *
     * @return the name
     */
    public String getName();
    
    /**
     * Getter for users.
     *
     * @return an umodifiable view of the users participating this project
     */
    public List<UserRO> getUsers();
}