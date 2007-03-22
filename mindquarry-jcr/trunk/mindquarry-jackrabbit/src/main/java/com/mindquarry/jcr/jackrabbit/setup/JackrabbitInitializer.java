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
package com.mindquarry.jcr.jackrabbit.setup;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.jackrabbit.core.nodetype.InvalidNodeTypeDefException;
import org.apache.jackrabbit.core.nodetype.NodeTypeDef;
import org.apache.jackrabbit.core.nodetype.NodeTypeManagerImpl;
import org.apache.jackrabbit.core.nodetype.NodeTypeRegistry;
import org.apache.jackrabbit.core.nodetype.compact.CompactNodeTypeDefReader;
import org.apache.jackrabbit.core.nodetype.compact.ParseException;

/**
 * Helper class for Jackrabbit initialization. Hope to remove this soon when
 * loading node type via spring configuration is working.
 * 
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public class JackrabbitInitializer {
    public static final String JCR_XML_NODETYPES = "/com/mindquarry/jcr/jackrabbit/node-types.txt"; //$NON-NLS-1$

    private Session session;

    public void initRepository() throws Exception {
        // Get the NodeTypeManager from the Workspace. Note that it must be
        // casted from the generic JCR NodeTypeManager to the
        // Jackrabbit-specific implementation.
        NodeTypeManagerImpl ntmgr = (NodeTypeManagerImpl) session
                .getWorkspace().getNodeTypeManager();

        // Acquire the NodeTypeRegistry
        NodeTypeRegistry ntreg = ntmgr.getNodeTypeRegistry();

        // register the default node types (xt:element etc.)
        InputStream is = getClass().getResourceAsStream(JCR_XML_NODETYPES);
        registerNodeTypesFromTextFile(new InputStreamReader(is), ntreg, ""); //$NON-NLS-1$
        setupInitialRepositoryStructure(session);
        session.save();
    }

    private static void registerNodeTypesFromTextFile(InputStreamReader reader,
            NodeTypeRegistry ntreg, String uri) throws ParseException,
            RepositoryException {
        // Create a CompactNodeTypeDefReader
        CompactNodeTypeDefReader cndReader = new CompactNodeTypeDefReader(
                reader, uri);

        // Get the List of NodeTypeDef objects
        List ntdList = cndReader.getNodeTypeDefs();

        registerNodeTypes(ntdList, ntreg);
    }

    private static void registerNodeTypes(List types, NodeTypeRegistry registry)
            throws RepositoryException {
        // Loop through the prepared NodeTypeDefs
        for (Iterator i = types.iterator(); i.hasNext();) {
            // Get the NodeTypeDef...
            NodeTypeDef ntd = (NodeTypeDef) i.next();

            // ...and register it
            try {
                registry.registerNodeType(ntd);
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
            root.getNode("users"); //$NON-NLS-1$
        } catch (PathNotFoundException e) {
            root.addNode("users", "nt:folder"); //$NON-NLS-1$ //$NON-NLS-2$
        }
        try {
            root.getNode("teamspaces"); //$NON-NLS-1$
        } catch (PathNotFoundException e) {
            root.addNode("teamspaces", "nt:folder"); //$NON-NLS-1$ //$NON-NLS-2$
        }
        try {
            root.getNode("tags"); //$NON-NLS-1$
        } catch (PathNotFoundException e) {
            root.addNode("tags", "nt:folder"); //$NON-NLS-1$ //$NON-NLS-2$
        }
    }

    /**
     * Setter for session.
     * 
     * @param session the session to set
     */
    public void setSession(Session session) {
        this.session = session;
    }
}
