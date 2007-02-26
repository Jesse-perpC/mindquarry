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
package com.mindquarry.auth.manager;

import com.mindquarry.common.persistence.Session;
import com.mindquarry.common.persistence.SessionFactory;
import com.mindquarry.teamspace.TeamspaceTestBase;

/**
 * Add summary documentation here.
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public class AuthorizationPersistenceTest extends TeamspaceTestBase {

    private SessionFactory sessionFactory;
    
    protected void setUp() throws Exception {
        super.setUp();
        this.sessionFactory = (SessionFactory) lookup(SessionFactory.ROLE);
    }
    
    public void testResourcePersitence() {
        ResourceEntity resource = new ResourceEntity("_", "root");
        ResourceEntity child = new ResourceEntity("_teamspaces", "teamspaces");
        ResourceEntity child2 = new ResourceEntity("_teamspaces_mindquarry", "mindquarry");
        resource.addChild(child);
        child.addChild(child2);
        
        Session session = this.sessionFactory.currentSession();
        session.persist(resource);
        session.commit();
        
        Object obj = session.query("getResourceById", new Object[] {"_"}).get(0);
        ResourceEntity persistentResource = (ResourceEntity) obj;
        assertTrue(persistentResource.hasChild("teamspaces"));
        assertTrue(persistentResource.getChild("teamspaces").hasChild("mindquarry"));
    }
}
