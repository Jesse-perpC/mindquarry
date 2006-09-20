/**
 * Copyright (C) 2006 MindQuarry GmbH, All Rights Reserved
 */
package com.mindquarry.jcr.source.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.jcr.NamespaceRegistry;
import javax.jcr.Node;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.Workspace;

import org.apache.avalon.framework.service.ServiceException;
import org.apache.cocoon.core.container.ContainerTestCase;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceResolver;
import org.apache.jackrabbit.core.nodetype.InvalidNodeTypeDefException;
import org.apache.jackrabbit.core.nodetype.NodeTypeDef;
import org.apache.jackrabbit.core.nodetype.NodeTypeManagerImpl;
import org.apache.jackrabbit.core.nodetype.NodeTypeRegistry;
import org.apache.jackrabbit.core.nodetype.compact.CompactNodeTypeDefReader;
import org.apache.jackrabbit.core.nodetype.compact.ParseException;

/**
 * Abstract base classes for all JCR XML source test cases.
 * 
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public abstract class JCRSourceTestBase extends ContainerTestCase {
    protected static final String NODE_TYPES_DEFINITION = "src/test/resources/node-types.txt";

    protected static final String SCHEME = "jcr";

    protected static final String BASE_URL = SCHEME + ":" + "//";

    @Override
    protected void setUp() throws Exception {
        // remove old repository
        File repoFolder = new File("/tmp/repository");
        removeRepository(repoFolder);

        // setup new repository
        super.setUp();

        Repository repo = (Repository) lookup(Repository.class.getName());
        Session session = repo.login(new SimpleCredentials("alexander.saar",
                "mypwd".toCharArray()));

        registerNodeTypes(session.getWorkspace());
        setupRepositoryContent(session);
        session.save();

        // export repo content to file
        FileOutputStream fos = new FileOutputStream("repo-content.xml");
        session.exportDocumentView("/", fos, true, false);
        fos.flush();
        fos.close();
    }

    private void registerNodeTypes(Workspace workspace)
            throws FileNotFoundException, ParseException, RepositoryException,
            InvalidNodeTypeDefException {
        // register xt:* namespace
        NamespaceRegistry nsRegistry = workspace.getNamespaceRegistry();
        nsRegistry.registerNamespace("xt", "http://mindquarry.com/ns/cnd/xt");

        // Read in the CND file
        FileReader fileReader = new FileReader(NODE_TYPES_DEFINITION);

        // Create a CompactNodeTypeDefReader
        CompactNodeTypeDefReader cndReader = new CompactNodeTypeDefReader(
                fileReader, NODE_TYPES_DEFINITION);

        // Get the List of NodeTypeDef objects
        List ntdList = cndReader.getNodeTypeDefs();

        // Get the NodeTypeManager from the Workspace. Note that it must be
        // casted from the generic JCR NodeTypeManager to the
        // Jackrabbit-specific implementation.
        NodeTypeManagerImpl ntmgr = (NodeTypeManagerImpl) workspace
                .getNodeTypeManager();

        // Acquire the NodeTypeRegistry
        NodeTypeRegistry ntreg = ntmgr.getNodeTypeRegistry();

        // Loop through the prepared NodeTypeDefs
        for (Iterator i = ntdList.iterator(); i.hasNext();) {
            // Get the NodeTypeDef...
            NodeTypeDef ntd = (NodeTypeDef) i.next();

            // ...and register it
            ntreg.registerNodeType(ntd);
        }
    }

    private void setupRepositoryContent(Session session) throws Exception {
        // add a user entry
        Node root = session.getRootNode();
        Node usersNode = root.addNode("users", "nt:folder");

        Node userFileNode = usersNode.addNode("alexander.saar", "nt:file");
        Node userDocNode = userFileNode.addNode("jcr:content", "xt:document");
        userDocNode.setProperty("jcr:lastModified", new GregorianCalendar());

        Node userDocRootNode = userDocNode.addNode("user", "xt:element");

        Node userDocNameNode = userDocRootNode.addNode("name", "xt:element");
        Node userDocNameTextNode = userDocNameNode.addNode("text", "xt:text");
        userDocNameTextNode.setProperty("xt:characters", "Alexander Saar");

        Node userDocMailNode = userDocRootNode.addNode("email", "xt:element");
        userDocMailNode.setProperty("type", "business");
        Node userDocMailTextNode = userDocMailNode.addNode("text", "xt:text");
        userDocMailTextNode.setProperty("xt:characters",
                "alexander.saar@mindquarry.com");

        // add a document entry
        Node imgsNode = root.addNode("images", "nt:folder");
        Node imgDocNode = imgsNode.addNode("photo154", "nt:file");
        Node imgDocContentNode = imgDocNode.addNode("jcr:content",
                "nt:resource");
        imgDocContentNode.setProperty("jcr:lastModified",
                new GregorianCalendar());
        imgDocContentNode.setProperty("jcr:mimeType", "jpg");
        imgDocContentNode.setProperty("jcr:data", new FileInputStream(
                "src/test/resources/lamp.jpg"));
    }

    protected Source resolveSource(String uri) throws ServiceException,
            IOException {
        SourceResolver resolver = (SourceResolver) lookup(SourceResolver.ROLE);
        return resolver.resolveURI(uri);
    }

    /**
     * Initializes the ComponentLocator
     * 
     * The configuration file is determined by the class name plus .xtest
     * appended, all '.' replaced by '/' and loaded as a resource via classpath
     */
    @Override
    protected void prepare() throws Exception {
        String className = JCRSourceTestBase.class.getName();
        String xtestResourceName = className.replace('.', '/') + ".xtest";

        URL xtestResource = classLoader().getResource(xtestResourceName);
        this.prepare(xtestResource.openStream());
    }

    private ClassLoader classLoader() {
        return getClass().getClassLoader();
    }

    private void removeRepository(File file) {
        // check if the file exists
        if (!file.exists()) {
            return;
        }
        // check if it is a file or a folder
        if (!file.isDirectory()) {
            file.delete();
            return;
        }
        // if it is a folder, remove the childs before removing the folder
        for (File tmp : file.listFiles()) {
            removeRepository(tmp);
        }
        file.delete();
    }
}
