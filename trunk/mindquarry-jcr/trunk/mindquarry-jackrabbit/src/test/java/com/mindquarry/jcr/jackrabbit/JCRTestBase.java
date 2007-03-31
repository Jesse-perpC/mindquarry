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
import java.net.MalformedURLException;
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
public abstract class JCRTestBase extends AvalonSpringContainerTestBase {
    public static final String SCHEME = "jcr"; //$NON-NLS-1$

    public static final String BASE_URL = SCHEME + ":///"; //$NON-NLS-1$
    public static final boolean USE_XENODOT = Boolean.parseBoolean(System.getProperty("xenodot", "false")); //$NON-NLS-1$

    protected Session session;

    @Override
    protected List<String> springConfigClasspathResources() {
        try {
            System.setProperty("mindquarry.jcr.path", //$NON-NLS-1$
                    new File("./target/repository").toURL().toString());//$NON-NLS-1$
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        System.setProperty("mindquarry.jcr.login", //$NON-NLS-1$
                "admin");//$NON-NLS-1$
        System.setProperty("mindquarry.jcr.pwd", //$NON-NLS-1$
                "admin");//$NON-NLS-1$

        List<String> result = super.springConfigClasspathResources();
        
        System.err.println("Using xenodot => " + USE_XENODOT);
        if (USE_XENODOT) {
            result.add("META-INF/cocoon/spring/xenodot-repository-context.xml"); //$NON-NLS-1$            
        } else {
            result.add("META-INF/cocoon/spring/jcr-repository-context.xml"); //$NON-NLS-1$
        }
        result.add("META-INF/cocoon/spring/jcr-session-context.xml"); //$NON-NLS-1$
        result.add("META-INF/cocoon/spring/jcr-rmi-server-context.xml"); //$NON-NLS-1$
        return result;
    }

    @Override
    protected void setUp() throws Exception {
        // remove old repository
        File repoFolder = new File("target/repository"); //$NON-NLS-1$
        removeRepository(repoFolder);
        repoFolder.mkdirs();

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
            System.err.println("Done cleaning Xenodot in setUp()");
        }

        super.setUp();
        session = (Session) lookup("jcrSession"); //$NON-NLS-1$
    }

    @Override
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
