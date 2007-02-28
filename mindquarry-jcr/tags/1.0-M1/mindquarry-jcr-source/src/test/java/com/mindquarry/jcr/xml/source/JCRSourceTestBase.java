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
package com.mindquarry.jcr.xml.source;

import java.io.FileInputStream;
import java.net.URL;
import java.util.GregorianCalendar;

import javax.jcr.Node;
import javax.jcr.Session;

import com.mindquarry.jcr.jackrabbit.JCRTestBase;

/**
 * Abstract base classes for all JCR XML source test cases.
 * 
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public abstract class JCRSourceTestBase extends JCRTestBase {
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        setupRepositoryContent(session);
        session.save();
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
        String className = JCRSourceTestBase.class.getName();
        String xtestResourceName = className.replace('.', '/') + ".xtest";
        URL xtestResource = getClass().getClassLoader().getResource(xtestResourceName);
        this.prepare(xtestResource.openStream());
    }

    protected void setupRepositoryContent(Session session) throws Exception {
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
}
