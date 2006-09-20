/**
 * Copyright (C) 2006 MindQuarry GmbH, All Rights Reserved
 */
package com.mindquarry.jcr.source.xml;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.jcr.Repository;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import org.apache.avalon.framework.service.ServiceException;
import org.apache.cocoon.core.container.ContainerTestCase;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceResolver;

/**
 * Abstract base classes for all JCR XML source test cases.
 * 
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public abstract class JCRSourceTestBase extends ContainerTestCase {

    protected static final String SCHEME = "jcr";

    protected static final String BASE_URL = SCHEME + ":" + "//";

    protected void setUp() throws Exception {
        // remove old repository
        File repoFolder = new File("/tmp/repository");
        removeRepository(repoFolder);

        // setup new repository
        super.setUp();
        setupRepositoryContent();
    }

    private void setupRepositoryContent() throws Exception {
        Repository repo = (Repository) lookup(Repository.class.getName());
        Session session = repo.login(new SimpleCredentials("alexander.saar",
                "mypwd".toCharArray()));

        // TODO register XML node types
        

        // TODO add some repository content
        session.getRootNode().addNode("users", "nt:folder");
        session.getRootNode().addNode("xml", "xt:element");
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
