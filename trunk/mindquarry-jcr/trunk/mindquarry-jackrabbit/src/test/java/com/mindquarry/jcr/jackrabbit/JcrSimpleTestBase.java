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
package com.mindquarry.jcr.jackrabbit;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.jcr.Session;

import org.apache.avalon.framework.service.ServiceException;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceResolver;

import com.mindquarry.common.test.AvalonSpringContainerTestBase;
import com.mindquarry.dms.xenodot.jackrabbit.XenodotPersistenceManager;

/**
 * Abstract base classes for all JCR XML source test cases.
 * 
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public abstract class JcrSimpleTestBase extends AvalonSpringContainerTestBase {
    public static final String SCHEME = "jcr"; //$NON-NLS-1$

    public static final String BASE_URL = SCHEME + ":///"; //$NON-NLS-1$
    public static final boolean USE_XENODOT = Boolean.parseBoolean(System.getProperty("xenodot", "false")); //$NON-NLS-1$

    private Session jcrSession;

    @Override
    protected List<String> springConfigClasspathResources() {
        
        List<String> result = super.springConfigClasspathResources();
        
        System.out.println("Using xenodot => " + USE_XENODOT);        
        if (USE_XENODOT)
            result.add("META-INF/cocoon/spring/xenodot-repository-context.xml");
        else
            result.add("META-INF/cocoon/spring/jcr-repository-context.xml");
        
        result.add("META-INF/cocoon/spring/jcr-session-context.xml");
        result.add("META-INF/cocoon/spring/jcr-rmi-server-context.xml");
        return result;
    }
    
    /**
     * Initializes the ComponentLocator
     * 
     * The configuration file is determined by the class name plus .xtest
     * appended, all '.' replaced by '/' and loaded as a resource via classpath
     */
    @Override
    protected void prepare() throws Exception {
        Class clazz = getClass();
        while (clazz != Object.class) {
            String className = clazz.getName();
            String xtestResourceName = className.replace('.', '/') + ".xtest";

            URL xtestResource = classLoader(clazz).getResource(xtestResourceName);
            if (xtestResource != null) {
                prepare(xtestResource.openStream());
                break;
            }
            clazz = clazz.getSuperclass();
        }
    }
    
    private ClassLoader classLoader(Class clazz) {
        return clazz.getClassLoader();
    }

    @Override
    protected void setUp() throws Exception {        

        System.setProperty("mindquarry.jcr.path",
                new File("target/repository").toURI().toString());
        
        System.setProperty("mindquarry.jcr.login", "admin");
        System.setProperty("mindquarry.jcr.pwd", "admin");

        if (USE_XENODOT) {
            // TODO: Right now XenodotPersistenceManger is hardcoded, so this will work
            System.err.println("Start cleaning Xenodot in setUp()");
            XenodotPersistenceManager manager = new XenodotPersistenceManager();
            manager.init(null);
            manager.getConnection().prepareCall("delete from xenodot.property;").execute();            
            manager.getConnection().prepareCall("delete from xenodot.value where reference_id is not null;").execute();        
            manager.getConnection().prepareCall("delete from xenodot.node;").execute();
            manager.getConnection().prepareCall("select jackrabbit.init_root();").execute();
            
            manager.close();
            System.out.println("Done cleaning Xenodot in setUp()");
        }

        super.setUp();
        jcrSession = (Session) lookup("jcrSession");
    }
    
    protected Session getJcrSession() {
        return jcrSession;
    }
    
    protected Source resolveSource(String uri) throws ServiceException, IOException {
        SourceResolver resolver = (SourceResolver) lookup(SourceResolver.ROLE);
        return resolver.resolveURI(uri);
    }
}
