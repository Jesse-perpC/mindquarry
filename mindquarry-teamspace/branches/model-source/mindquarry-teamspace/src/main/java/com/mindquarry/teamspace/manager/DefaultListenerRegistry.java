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
            listener.afterTeamspaceCreated(teamspace);
        }
    }
    
    void signalAfterTeamspaceRemoved(Teamspace teamspace) throws Exception {
        for (TeamspaceListener listener : listeners_) {
            listener.beforeTeamspaceRemoved(teamspace);
        }
    }
}
