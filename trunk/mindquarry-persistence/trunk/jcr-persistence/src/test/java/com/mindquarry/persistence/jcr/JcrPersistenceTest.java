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

import org.apache.avalon.framework.service.ServiceException;

import com.mindquarry.common.persistence.Session;


public class JcrPersistenceTest extends JcrPersistenceTestBase {
    
    public JcrPersistenceTest() {
    }
    
    public void testPersistUser() throws ServiceException {
        
        JcrPersistence jcrPersistence = 
            (JcrPersistence) lookup(JcrPersistence.class.getName());
        
        jcrPersistence.addClass(User.class);
        jcrPersistence.configure();
        
        Session session = jcrPersistence.currentSession();
        User user = new User();
        user.setLogin("test");
        user.setPwd("pwd");
        user.setFirstname("test");
        user.setLastname("test");        
        session.persist(user);
    }
}
