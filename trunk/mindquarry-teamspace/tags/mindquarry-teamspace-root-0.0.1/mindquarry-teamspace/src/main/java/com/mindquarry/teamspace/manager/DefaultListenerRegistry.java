/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.teamspace.manager;

import java.util.HashSet;
import java.util.Set;

import com.mindquarry.teamspace.Teamspace;
import com.mindquarry.teamspace.TeamspaceListener;
import com.mindquarry.teamspace.TeamspaceListenerRegistry;

/**
 * Add summary documentation here.
 *
 * @author
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public class DefaultListenerRegistry implements TeamspaceListenerRegistry {

    private Set<TeamspaceListener> listeners_;
    
    public DefaultListenerRegistry() {
        listeners_ = new HashSet<TeamspaceListener>();
    }
    
    /**
     * @see com.mindquarry.teamspace.TeamspaceListenerRegistry#addListener(com.mindquarry.teamspace.TeamspaceListener)
     */
    public void addListener(TeamspaceListener listener) {
        listeners_.add(listener);
    }

    /**
     * @see com.mindquarry.teamspace.TeamspaceListenerRegistry#removeListener(com.mindquarry.teamspace.TeamspaceListener)
     */
    public void removeListener(TeamspaceListener listener) {
        listeners_.remove(listener);
    }
    
    void signalBeforeTeamspaceCreated(Teamspace teamspace) throws Exception {
        for (TeamspaceListener listener : listeners_) {
            listener.beforeTeamspaceCreated(teamspace);
        }
    }
    
    void signalAfterTeamspaceRemoved(Teamspace teamspace) throws Exception {
        for (TeamspaceListener listener : listeners_) {
            listener.afterTeamspaceRemoved(teamspace);
        }
    }
}
