/**
 * Copyright (C) 2006 MindQuarry GmbH, All Rights Reserved
 */
package com.mindquarry.jcr.source.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.GregorianCalendar;

import javax.jcr.AccessDeniedException;
import javax.jcr.InvalidItemStateException;
import javax.jcr.ItemExistsException;
import javax.jcr.LoginException;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.nodetype.NoSuchNodeTypeException;
import javax.jcr.version.VersionException;

import org.apache.avalon.framework.service.ServiceException;
import org.apache.cocoon.core.container.ContainerTestCase;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceResolver;
import org.apache.jackrabbit.core.nodetype.InvalidNodeTypeDefException;
import org.apache.jackrabbit.core.nodetype.compact.ParseException;

import com.mindquarry.jcr.jackrabbit.JackrabbitInitializer;

/**
 * Abstract base classes for all JCR XML source test cases.
 * 
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public abstract class JCRSourceTestBase extends ContainerTestCase {
    protected static final String SCHEME = "jcr";

    protected static final String BASE_URL = SCHEME + "://";

    @Override
    protected void setUp() throws Exception {
        // remove old repository
        File repoFolder = new File("target/repository");
        removeRepository(repoFolder);

        // setup new repository
        super.setUp();

        Repository repo = (Repository) lookup(Repository.class.getName());
        Session session = repo.login(new SimpleCredentials("alexander.saar",
                "mypwd".toCharArray()));

        lookup(JackrabbitInitializer.ROLE);
        setupRepositoryContent(session);
        session.save();
    }

    protected void exportRepository() throws ServiceException, LoginException,
            RepositoryException, FileNotFoundException, ParseException,
            InvalidNodeTypeDefException, Exception, AccessDeniedException,
            ItemExistsException, ConstraintViolationException,
            InvalidItemStateException, VersionException, LockException,
            NoSuchNodeTypeException, IOException, PathNotFoundException {
        Repository repo = (Repository) lookup(Repository.class.getName());
        Session session = repo.login(new SimpleCredentials("alexander.saar",
                "mypwd".toCharArray()));

        // export repo content to file
        FileOutputStream fos = new FileOutputStream("repo-content.xml");
        session.exportDocumentView("/", fos, true, false);
        fos.flush();
        fos.close();
    }

    /**
     * @see org.apache.cocoon.core.container.ContainerTestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    private void setupRepositoryContent(Session session) throws Exception {
        // add a user entry
        Node root = session.getRootNode();
        Node usersNode = root.getNode("users");

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

        // test for additional-node-types.txt
        Node encodingTextNode = userDocMailNode.addNode("text-with-enc", "xt:textenc");
        encodingTextNode.setProperty("xt:encoding", "UTF-8");

        // test code to trigger exception for a non-existing node type
        //Node errorTextNode = userDocMailNode.addNode("error", "xt:doesnotexist");
        
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
