/*
 * Coypright (c) 2006 Mindquarry GmbH, Potsdam, Germany 
 */
package com.mindquarry.tasks;

import org.apache.cocoon.environment.SourceResolver;
import org.apache.excalibur.source.ModifiableTraversableSource;

import com.mindquarry.jcr.id.IDException;
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
    public String getUniqueTaskId(String jcrPath) {
        try {
            long id = uniqueIDGenerator.getNextID(jcrPath);
            return "task" + id;
        } catch (IDException e) {
            return "task_error";
        }        
    }

}
