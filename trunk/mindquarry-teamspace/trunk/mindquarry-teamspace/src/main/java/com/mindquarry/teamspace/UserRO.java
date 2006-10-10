/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.teamspace;

import java.util.Set;

/**
 * Add summary documentation here.
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">your full name</a>
 */
public interface UserRO {

    /**
     * Getter for id.
     *
     * @return the id
     */
    String getId();

    /**
     * Getter for name.
     *
     * @return the name
     */
    String getName();

    /**
     * Getter for teamspaces.
     *
     * @return an unmodifiable view of the teamspaces
     */
    Set<String> getTeamspaceReferences();

}