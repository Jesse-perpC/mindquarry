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
package com.mindquarry.model.source;

import java.net.URL;

import com.mindquarry.teamspace.TeamspaceTestBase;

/**
 * @author <a href="bastian(dot)steinert(at)mindquarry(dot)com">Bastian Steinert</a>
 */
public abstract class ModelSourceTestBase extends TeamspaceTestBase {
        
    /**
     * Initializes the ComponentLocator
     * 
     * The configuration file is determined by the class name plus .xtest
     * appended, all '.' replaced by '/' and loaded as a resource via classpath
     */
    protected void prepare() throws Exception {
        String className = ModelSourceTestBase.class.getName();
        String xtestResourceName = className.replace('.', '/') + ".xtest";

        URL xtestResource = classLoader().getResource(xtestResourceName);
        this.prepare(xtestResource.openStream());
    }
    
    private ClassLoader classLoader() {
        return getClass().getClassLoader();
    }
}
