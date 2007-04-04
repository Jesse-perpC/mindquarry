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
package com.mindquarry.tasks;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public void afterTeamspaceCreated(Teamspace teamspace)
            throws Exception {
        initializeTasks(teamspace.getId());
        initializeTasksFilter(teamspace.getId());
    }

    /**
     * Called on deletion of a teamspace.
     */
    public void beforeTeamspaceRemoved(Teamspace teamspace)
            throws Exception {
    }
    
    private String extractTeamspaceFromURI(String jcrURI) throws Exception {
        // simple regular expression that looks for the teamspace name...
        Pattern p = Pattern.compile("jcr:///teamspaces/([^/]*)/(.*)");
        Matcher m = p.matcher(jcrURI);
        if (m.matches()) {
            return m.group(1); // "mindquarry";
        }
        
        throw new Exception("Invalid JCR URI.");
    }
    
    /**
     * Creates a unique id for the given jcr path. This id will look like
     * "task" + id, where id is a unique number below the given path.
     */
    public String getUniqueTaskId(String jcrPath) throws Exception {
        initializeTasks(extractTeamspaceFromURI(jcrPath));
        
        long id = uniqueIDGenerator.getNextID(jcrPath);
        return "task" + id;
    }

    /**
     * Creates a unique id for the given jcr path. The string will only be the
     * id, where id is a unique number below the given path.
     */
    public long getUniqueId(String jcrPath) throws Exception {
        initializeTasksFilter(extractTeamspaceFromURI(jcrPath));
        
        return uniqueIDGenerator.getNextID(jcrPath);
    }

    /**
     * Initialization is only done, if the things to initialize are not yet
     * existing, so it can be called anytime to ensure the existence.
     */
    private void initializeTasks(String teamspace) throws Exception {
        initializeUniqueIdDirectory("/teamspaces/" + teamspace + "/tasks");
    }

    /**
     * Initialization is only done, if the things to initialize are not yet
     * existing, so it can be called anytime to ensure the existence.
     */
    private void initializeTasksFilter(String teamspace) throws Exception {
        initializeUniqueIdDirectory("/teamspaces/" + teamspace + "/tasks/filters");
    }

    /**
     * Initialization is only done, if the things to initialize are not yet
     * existing, so it can be called anytime to ensure the existence.
     */
    private void initializeUniqueIdDirectory(String path) throws Exception {
        // create sub directory
        ModifiableTraversableSource source;
        source = (ModifiableTraversableSource) this.sourceResolver
                .resolveURI("jcr://" + path);
        if (!source.exists()) {
            source.makeCollection();
        }

        // create id subnode
        this.uniqueIDGenerator.initializePath(path);
    }

}
