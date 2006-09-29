/**
 * Copyright (C) 2006 MindQuarry GmbH, All Rights Reserved
 */
package com.mindquarry.jcr.jackrabbit;

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
public abstract class JCRTestBase extends ContainerTestCase {
    public static final String SCHEME = "jcr";

    public static final String BASE_URL = SCHEME + ":///";
    
    protected Session session;

    @Override
    protected void setUp() throws Exception {
        // remove old repository
        File repoFolder = new File("target/repository");
        removeRepository(repoFolder);

        // setup new repository
        super.setUp();

        Repository repo = (Repository) lookup(Repository.class.getName());
        session = repo.login(new SimpleCredentials("alexander.saar",
                "mypwd".toCharArray()));
    }

    /**
     * @see org.apache.cocoon.core.container.ContainerTestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    protected Source resolveSource(String uri) throws ServiceException,
            IOException {
        SourceResolver resolver = (SourceResolver) lookup(SourceResolver.ROLE);
        return resolver.resolveURI(uri);
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
