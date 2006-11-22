/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.jcr.jackrabbit;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;

import javax.jcr.NamespaceException;
import javax.jcr.NamespaceRegistry;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.jcr.observation.Event;
import javax.jcr.observation.ObservationManager;

import org.apache.excalibur.source.SourceNotFoundException;
import org.apache.jackrabbit.core.nodetype.InvalidNodeTypeDefException;
import org.apache.jackrabbit.core.nodetype.NodeTypeDef;
import org.apache.jackrabbit.core.nodetype.NodeTypeManagerImpl;
import org.apache.jackrabbit.core.nodetype.NodeTypeRegistry;
import org.apache.jackrabbit.core.nodetype.compact.CompactNodeTypeDefReader;
import org.apache.jackrabbit.core.nodetype.compact.ParseException;

import com.mindquarry.common.index.IndexClient;
import com.mindquarry.jcr.jackrabbit.listener.ChangeEventListener;

/**
 * Helper class for the JackrabbitInitializer.
 * 
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public class JackrabbitInitializerHelper {
    public static final String MQ_JCR_XML_NAMESPACE_PREFIX = "xt";

    public static final String MQ_JCR_XML_NAMESPACE_URI = "http://mindquarry.com/ns/cnd/xt";

    public static void setupRepository(Session session,
            InputStreamReader nDefs, String uri, IndexClient iClient)
            throws ParseException, RepositoryException,
            InvalidNodeTypeDefException, SourceNotFoundException, IOException {
        // register xt:* namespace
        NamespaceRegistry nsRegistry = session.getWorkspace()
                .getNamespaceRegistry();
        try {
            // check if the namespace already exists
            nsRegistry.getURI(MQ_JCR_XML_NAMESPACE_PREFIX);
        } catch (NamespaceException ne) {
            nsRegistry.registerNamespace(MQ_JCR_XML_NAMESPACE_PREFIX,
                    MQ_JCR_XML_NAMESPACE_URI);
        }

        // Get the NodeTypeManager from the Workspace. Note that it must be
        // casted from the generic JCR NodeTypeManager to the
        // Jackrabbit-specific implementation.
        NodeTypeManagerImpl ntmgr = (NodeTypeManagerImpl) session
                .getWorkspace().getNodeTypeManager();

        // Acquire the NodeTypeRegistry
        NodeTypeRegistry ntreg = ntmgr.getNodeTypeRegistry();

        // register the default node types (xt:element etc.)
        registerNodeTypesFromTextFile(nDefs, ntreg, uri);

        setupInitialRepositoryStructure(session);
        registerUpdateListener(session, iClient);
    }

    private static void registerUpdateListener(Session session,
            IndexClient iClient)
            throws UnsupportedRepositoryOperationException, RepositoryException {
        ObservationManager om = session.getWorkspace().getObservationManager();

        om.addEventListener(new ChangeEventListener(iClient), Event.NODE_ADDED
                | Event.NODE_REMOVED | Event.PROPERTY_ADDED
                | Event.PROPERTY_REMOVED | Event.PROPERTY_CHANGED, "/", true,
                null, new String[] { "nt:folder", "nt:file", "xt:document",
                        "xt:element", "xt:text" }, true);
    }

    private static void registerNodeTypesFromTextFile(InputStreamReader reader,
            NodeTypeRegistry ntreg, String uri) throws IOException,
            SourceNotFoundException, ParseException,
            InvalidNodeTypeDefException, RepositoryException {
        // Create a CompactNodeTypeDefReader
        CompactNodeTypeDefReader cndReader = new CompactNodeTypeDefReader(
                reader, uri);

        // Get the List of NodeTypeDef objects
        List ntdList = cndReader.getNodeTypeDefs();

        registerNodeTypes(ntdList, ntreg);
    }

    private static void registerNodeTypes(List ntdList, NodeTypeRegistry ntreg)
            throws RepositoryException {
        // Loop through the prepared NodeTypeDefs
        for (Iterator i = ntdList.iterator(); i.hasNext();) {
            // Get the NodeTypeDef...
            NodeTypeDef ntd = (NodeTypeDef) i.next();

            // ...and register it
            try {
                ntreg.registerNodeType(ntd);
            } catch (InvalidNodeTypeDefException e) {
                // seems that the node type was already registered, so just
                // print a message and continue.
                System.out.println(e.getLocalizedMessage());
            }
        }
    }

    private static void setupInitialRepositoryStructure(Session session)
            throws RepositoryException {
        // add necessary folders folder
        Node root = session.getRootNode();
        try {
            root.getNode("users");
        } catch (PathNotFoundException e) {
            root.addNode("users", "nt:folder");
        }
        try {
            root.getNode("teamspaces");
        } catch (PathNotFoundException e) {
            root.addNode("teamspaces", "nt:folder");
        }
        try {
            root.getNode("tags");
        } catch (PathNotFoundException e) {
            root.addNode("tags", "nt:folder");
        }
    }
}
