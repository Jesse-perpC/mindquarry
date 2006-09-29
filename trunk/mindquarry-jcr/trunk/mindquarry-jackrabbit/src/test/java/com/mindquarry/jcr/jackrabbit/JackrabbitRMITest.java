/**
 * Copyright (C) 2006 MindQuarry GmbH, All Rights Reserved
 */
package com.mindquarry.jcr.jackrabbit;

import java.net.URL;
import java.rmi.registry.Registry;

import javax.jcr.Repository;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import org.apache.jackrabbit.rmi.client.ClientRepositoryFactory;

/**
 * Test cases for the FileOrFolderSource implementation.
 * 
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public class JackrabbitRMITest extends JCRTestBase {
    /**
     * @see com.mindquarry.jcr.source.xml.JCRTestBase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * @see org.apache.cocoon.core.container.ContainerTestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    /**
     * Initializes the ComponentLocator
     * 
     * The configuration file is determined by the class name plus .xtest
     * appended, all '.' replaced by '/' and loaded as a resource via classpath
     */
    @Override
    protected void prepare() throws Exception {
        String className = JackrabbitRMITest.class.getName();
        String xtestResourceName = className.replace('.', '/') + ".xtest";

        URL xtestResource = getClass().getClassLoader().getResource(xtestResourceName);
        this.prepare(xtestResource.openStream());
    }

    public void testRepositoryRMIAccess() throws Exception {
        ClientRepositoryFactory factory = new ClientRepositoryFactory();
        Repository repo = factory.getRepository("rmi://localhost:"
                + Registry.REGISTRY_PORT + "/jackrabbit");
        Session session = repo.login(new SimpleCredentials("alexander.saar",
                "mypwd".toCharArray()), "default");
        assertNotNull(session.getRootNode());
    }
}
