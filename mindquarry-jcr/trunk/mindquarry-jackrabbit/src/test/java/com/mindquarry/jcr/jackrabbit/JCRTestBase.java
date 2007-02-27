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
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.jcr.Repository;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import org.apache.avalon.framework.service.ServiceException;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceResolver;

import com.mindquarry.common.test.AvalonSpringContainerTestBase;


/**
 * Abstract base classes for all JCR XML source test cases.
 * 
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public abstract class JCRTestBase extends AvalonSpringContainerTestBase {
    public static final String SCHEME = "jcr";

    public static final String BASE_URL = SCHEME + ":///";
    
    public static final String MQ_JCR_XML_NODETYPES_FILE = "/com/mindquarry/jcr/jackrabbit/node-types.txt";

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
        
        InputStream nodeTypeDefIn = getClass().getResourceAsStream(
                MQ_JCR_XML_NODETYPES_FILE);

        JackrabbitInitializerHelper.setupRepository(session,
                new InputStreamReader(nodeTypeDefIn), ""); //$NON-NLS-1$        
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
