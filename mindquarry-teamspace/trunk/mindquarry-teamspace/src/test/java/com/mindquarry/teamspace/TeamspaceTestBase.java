/**
 * Copyright (C) 2005 MindQuarry GmbH, All Rights Reserved
 */
package com.mindquarry.teamspace;

import java.io.IOException;
import java.net.URL;

import org.apache.avalon.framework.service.ServiceException;
import org.apache.cocoon.core.container.ContainerTestCase;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceResolver;



/**
 * @author <a href="bastian(dot)steinert(at)mindquarry(dot)com">Bastian Steinert</a>
 */
abstract class TeamspaceTestBase extends ContainerTestCase {
    
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    protected Source resolveSource(String uri) throws ServiceException, IOException {
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
        String className = TeamspaceTestBase.class.getName();
        String xtestResourceName = className.replace('.', '/') + ".xtest";

        URL xtestResource = classLoader().getResource(xtestResourceName);
        this.prepare(xtestResource.openStream());
    }
    
    private ClassLoader classLoader() {
        return getClass().getClassLoader();
    }
}
