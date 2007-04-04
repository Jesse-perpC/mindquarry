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

import org.apache.avalon.framework.service.ServiceException;

import com.mindquarry.persistence.api.JavaConfiguration;
import com.mindquarry.persistence.api.Session;
import com.mindquarry.persistence.api.SessionFactory;


public class JcrOnDeleteCascadeTest extends JcrPersistenceTestBase {
    
    private SessionFactory sessionFactory_;
    
    protected void setUp() throws Exception {
        super.setUp();
        
        JavaConfiguration configuration = new JavaConfiguration();
        configuration.addClass(User.class);
        configuration.addClass(Group.class);
        configuration.addClass(Team.class);
        
        Persistence persistence = 
            (Persistence) lookup(Persistence.class.getName());        
        persistence.configure(configuration);
        
        sessionFactory_ = persistence;    
    }
    
    public void testOnDeleteCascadeUser() throws ServiceException {
        
        Session session;
        try {
            session = sessionFactory_.currentSession();        
            User user = new User();
            user.setLogin("testUser");
            user.setPwd("pwd");
            user.firstname = "test";
            user.lastname = "test";        
            session.persist(user);
            session.commit();
            
            
            session = sessionFactory_.currentSession();        
            Group group = new Group();
            group.id = "testGroup";
            group.members.add(user);        
            session.persist(group);
            session.commit();
            
            session = sessionFactory_.currentSession();
            deleteTestUserIfExists();
            session.commit();
        }
        finally {
            session = sessionFactory_.currentSession();
            deleteTestGroupIfExists();
            deleteTestUserIfExists();
            session.commit();            
        }
    }
    
    private void deleteTestGroupIfExists() {
        Session session = sessionFactory_.currentSession();
        List<Object> queryResult = session.query(
                "groupById", new Object[] {"testGroup"});
        
        if (queryResult.size() == 1) {
            Group group = (Group) queryResult.get(0);
            session.delete(group);
        }
    }
    
    private void deleteTestUserIfExists() {
        Session session = sessionFactory_.currentSession();
        List<Object> queryResult = session.query(
                "userByLogin", new Object[] {"testUser"});
        
        if (queryResult.size() == 1) {
            User user = (User) queryResult.get(0);
            session.delete(user);
        }
    }
}
