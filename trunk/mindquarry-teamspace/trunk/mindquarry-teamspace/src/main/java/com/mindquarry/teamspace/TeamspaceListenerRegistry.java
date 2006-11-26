/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.teamspace;

/**
 * Add summary documentation here.
 *
 * @author
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public interface TeamspaceListenerRegistry {

    public static final String ROLE = TeamspaceListenerRegistry.class.getName();
    
    void addListener(TeamspaceListener listener);
    
    void removeListener(TeamspaceListener listener);
}
