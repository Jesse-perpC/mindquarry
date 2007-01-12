/*
 * Copyright (C) 2006-2007 MindQuarry GmbH, All Rights Reserved
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
package com.mindquarry.tasks;

import org.apache.cocoon.environment.SourceResolver;
import org.apache.excalibur.source.ModifiableTraversableSource;

import com.mindquarry.jcr.id.JCRUniqueIDGenerator;
import com.mindquarry.teamspace.Teamspace;
import com.mindquarry.teamspace.TeamspaceListener;
import com.mindquarry.teamspace.TeamspaceListenerRegistry;

/**
 * @author <a href="mailto:alexander(dot)klimetschek(at)mindquarry(dot)com">
 *         Alexander Klimetschek</a>
 * 
 */
public class TasksManager implements TeamspaceListener {

    private SourceResolver sourceResolver;

    private JCRUniqueIDGenerator uniqueIDGenerator;

    public void setTeamspaceListenerRegistry(TeamspaceListenerRegistry registry) {
        registry.addListener(this);
    }

    public void setSourceResolver(SourceResolver resolver) {
        this.sourceResolver = resolver;
    }

    public void setUniqueIDGenerator(JCRUniqueIDGenerator uniqueIDGenerator) {
        this.uniqueIDGenerator = uniqueIDGenerator;
    }

    /**
     * Called on creation of a teamspace.
     */
    public void beforeTeamspaceCreated(Teamspace teamspace)
            throws Exception {
        final String tasksDirPath = "/teamspaces/" + teamspace.getId()
                + "/tasks";

        // create tasks sub directory
        ModifiableTraversableSource source;
        source = (ModifiableTraversableSource) this.sourceResolver
                .resolveURI("jcr://" + tasksDirPath);
        if (!source.exists()) {
            source.makeCollection();
        }

        // create id subnode
        this.uniqueIDGenerator.initializePath(tasksDirPath);
    }

    /**
     * Called on deletion of a teamspace.
     */
    public void afterTeamspaceRemoved(Teamspace teamspace)
            throws Exception {
    }
    
    /**
     * Creates a unique id for the given jcr path. This id will look like
     * "task" + id, where id is a unique number below the given path.
     */
    public String getUniqueTaskId(String jcrPath) throws Exception {
        long id = uniqueIDGenerator.getNextID(jcrPath);
        return "task" + id;
    }

}
