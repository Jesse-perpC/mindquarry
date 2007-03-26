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
package com.mindquarry.persistence.jcr;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.avalon.framework.service.ServiceException;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceResolver;

import com.mindquarry.common.test.AvalonSpringContainerTestBase;



public class JcrPersistenceTestBase extends AvalonSpringContainerTestBase {
    
    protected List<String> springConfigClasspathResources() {
        System.setProperty("mindquarry.jcr.path",
                    new File("target/repository").toURI().toString());
        
        System.setProperty("mindquarry.jcr.login", "admin");
        System.setProperty("mindquarry.jcr.pwd", "admin");

        List<String> result = super.springConfigClasspathResources();
        result.add("META-INF/cocoon/spring/jcr-repository-context.xml");
        result.add("META-INF/cocoon/spring/jcr-session-context.xml");
        result.add("META-INF/cocoon/spring/jcr-rmi-server-context.xml");

        result.add("META-INF/cocoon/spring/jcr-persistence-context.xml");
        return result;
    }
    
    protected Source resolveSource(String uri) throws ServiceException, IOException {
        SourceResolver resolver = (SourceResolver) lookup(SourceResolver.ROLE);
        return resolver.resolveURI(uri);
    }
}
