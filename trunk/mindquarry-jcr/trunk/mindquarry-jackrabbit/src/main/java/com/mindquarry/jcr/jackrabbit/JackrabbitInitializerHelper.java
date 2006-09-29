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
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.excalibur.source.SourceNotFoundException;
import org.apache.jackrabbit.core.nodetype.InvalidNodeTypeDefException;
import org.apache.jackrabbit.core.nodetype.NodeTypeDef;
import org.apache.jackrabbit.core.nodetype.NodeTypeManagerImpl;
import org.apache.jackrabbit.core.nodetype.NodeTypeRegistry;
import org.apache.jackrabbit.core.nodetype.compact.CompactNodeTypeDefReader;
import org.apache.jackrabbit.core.nodetype.compact.ParseException;

/**
 * Helper class for the JackrabbitInitializer.
 * 
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public class JackrabbitInitializerHelper {
	public static final String MQ_JCR_XML_NAMESPACE_PREFIX = "xt";

	public static final String MQ_JCR_XML_NAMESPACE_URI = "http://mindquarry.com/ns/cnd/xt";

	public static final String MQ_JCR_XML_NODETYPES_FILE = "resource://com/mindquarry/jcr/jackrabbit/node-types.txt";

	public static void setupRepository(Session session,
			InputStreamReader nDefs, String uri)
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
			throws InvalidNodeTypeDefException, RepositoryException {
		// Loop through the prepared NodeTypeDefs
		for (Iterator i = ntdList.iterator(); i.hasNext();) {
			// Get the NodeTypeDef...
			NodeTypeDef ntd = (NodeTypeDef) i.next();

			// ...and register it
			ntreg.registerNodeType(ntd);
		}
	}

	private static void setupInitialRepositoryStructure(Session session)
			throws RepositoryException {
		// add users folder
		Node root = session.getRootNode();
		root.addNode("users", "nt:folder");
		root.addNode("teamspaces", "nt:folder");
		root.addNode("tags", "nt:folder");
	}
}
