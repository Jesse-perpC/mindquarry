/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.auth.manager;

import com.mindquarry.common.persistence.Session;
import com.mindquarry.common.persistence.SessionFactory;
import com.mindquarry.teamspace.TeamspaceTestBase;
import com.mindquarry.user.UserAdmin;
import com.mindquarry.user.UserRO;

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
