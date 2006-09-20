/**
 * Copyright (C) 2006 MindQuarry GmbH, All Rights Reserved
 */
package com.mindquarry.jcr.jackrabbit;

import java.io.IOException;
import java.rmi.registry.Registry;

import javax.jcr.Repository;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import org.apache.avalon.framework.service.ServiceException;
import org.apache.jackrabbit.rmi.client.ClientRepositoryFactory;

import com.mindquarry.jcr.source.xml.JCRSourceTestBase;

/**
 * Test cases for the FileOrFolderSource implementation.
 * 
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public class JackrabbitRMITest extends JCRSourceTestBase {
    /**
     * @see com.mindquarry.jcr.source.xml.JCRSourceTestBase#setUp()
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

    public void testRepositoryRMIAccess() throws Exception {
        ClientRepositoryFactory factory = new ClientRepositoryFactory();
        Repository repo = factory.getRepository("rmi://localhost:"
                + Registry.REGISTRY_PORT + "/jackrabbit");
        Session session = repo.login(new SimpleCredentials("alexander.saar",
                "mypwd".toCharArray()), "default");
        assertNotNull(session.getRootNode());
    }
}
