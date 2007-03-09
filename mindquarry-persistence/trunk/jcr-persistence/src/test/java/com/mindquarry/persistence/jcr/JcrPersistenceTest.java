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

import com.mindquarry.common.persistence.Session;

import junit.framework.TestCase;


public class JcrPersistenceTest extends TestCase {
    
    public JcrPersistenceTest() {
    }
    
    public void testPersistUser() {
        JcrPersistence jcrPersistence = new JcrPersistence();
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
