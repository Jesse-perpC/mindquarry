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

import java.util.LinkedList;
import java.util.List;

import org.apache.avalon.framework.service.ServiceException;

import com.mindquarry.persistence.api.JavaConfiguration;
import com.mindquarry.persistence.api.Session;
import com.mindquarry.persistence.api.SessionFactory;


public class JcrInheritanceTest extends JcrPersistenceTestBase {
    
    private SessionFactory sessionFactory_;
    
    protected void setUp() throws Exception {
        super.setUp();
        
        JavaConfiguration configuration = new JavaConfiguration();
        configuration.addClass(User.class);
        configuration.addClass(ExtendedUser.class);
        configuration.addClass(Group.class);
        configuration.addClass(Team.class);
        
        Persistence persistence = 
            (Persistence) lookup(Persistence.class.getName());        
        persistence.configure(configuration);
        
        sessionFactory_ = persistence;    
    }
    
    public void testExtendedUser() throws ServiceException {
        
        Session session = sessionFactory_.currentSession();
        ExtendedUser user = new ExtendedUser();
        user.setLogin("testUser");
        user.setPwd("pwd");
        user.firstname = "test";
        user.lastname = "test";
        user.setNameExtension("foo");
        
        List<String> skills = new LinkedList<String>(); 
        skills.add("foo");
        skills.add("bar");
        
        user.setSkills(skills);        
        user.setSkillsArray(skills.toArray(new String[skills.size()]));
        
        session.persist(user);
    }
    
    public void testQueryDeleteExtendedUser() throws ServiceException {
        
        Session session = sessionFactory_.currentSession();
        List<Object> queryResult = session.query(
                "extendedUserByLogin", "testUser");
        
        ExtendedUser user = (ExtendedUser) queryResult.get(0);
        assertEquals("test", user.lastname);
        assertEquals("foo", user.getNameExtension());
        assertEquals(2, user.getSkills().size());
        assertEquals(2, user.getSkillsArray().length);
        
        session.delete(user);
    }
}
