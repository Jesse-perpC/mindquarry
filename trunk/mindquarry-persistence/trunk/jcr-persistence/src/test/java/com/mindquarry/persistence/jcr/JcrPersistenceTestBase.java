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

import java.util.List;

import com.mindquarry.jcr.jackrabbit.JcrSimpleTestBase;



public abstract class JcrPersistenceTestBase extends JcrSimpleTestBase {
        
    protected List<String> springConfigClasspathResources() {        
        List<String> result = super.springConfigClasspathResources();
        result.add("META-INF/cocoon/spring/jcr-persistence-context.xml");
        return result;
    }
    
    protected void tearDown() throws Exception {
        ((Persistence) lookup(Persistence.ROLE)).clear();        
        super.tearDown();
    }
}
