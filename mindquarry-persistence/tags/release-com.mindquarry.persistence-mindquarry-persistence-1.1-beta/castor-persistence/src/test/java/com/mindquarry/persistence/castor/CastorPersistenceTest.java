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
package com.mindquarry.persistence.castor;

import java.util.List;

import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import org.apache.avalon.framework.service.ServiceException;

import com.mindquarry.common.persistence.PersistenceException;
import com.mindquarry.common.persistence.Session;
import com.mindquarry.common.persistence.SessionFactory;

public class CastorPersistenceTest extends CastorPersistenceTestBase {

    public void testConversation() throws ServiceException, NotSupportedException, SystemException {
                
        SessionFactory sessionFactory = (SessionFactory) lookup(SessionFactory.ROLE);
        Session session = sessionFactory.currentSession();
        
        UserEntity user = new UserEntity();        
        user.setId("bastian");        
        user.setName("Bastain");
        user.setSurname("Steinert");
        session.persist(user);
        
        TeamspaceEntity teamspace = new TeamspaceEntity();
        teamspace.setDescription("a great description");
        teamspace.setId("mindquarry");
        teamspace.setName("Mindquarry");
        session.persist(teamspace);
        
        session.commit();
        
        
        session = sessionFactory.currentSession();
        
        List queryResult = session.query("getUserById", new Object[] {"bastian"});
        UserEntity persistentUser = (UserEntity) queryResult.get(0);
        assertEquals("bastian", persistentUser.getId());
        
        session.delete(persistentUser);
        
        session.commit();
        
        
        session = sessionFactory.currentSession();
        
        queryResult = session.query("getUserById", new Object[] {"bastian"});
        assertTrue(queryResult.isEmpty());       
        
        session.commit();
    }
    
    public void testDuplicateIds() throws ServiceException {
        SessionFactory sessionFactory = (SessionFactory) lookup(SessionFactory.ROLE);
        
        
        UserEntity user = new UserEntity();        
        user.setId("bastian");        
        user.setName("Bastain");
        user.setSurname("Steinert");
        
        Session session = sessionFactory.currentSession();
        session.persist(user);        
        session.commit();
        
        
        UserEntity duplicateUser = new UserEntity();        
        duplicateUser.setId("bastian");        
        duplicateUser.setName("Bastain");
        duplicateUser.setSurname("Steinert");
        
        boolean recognizedDuplicateIds = false;
        session = sessionFactory.currentSession();
        try {
            session.persist(duplicateUser);
            session.commit();
        } catch (PersistenceException e) {
            recognizedDuplicateIds = true;
        }
        
        session = sessionFactory.currentSession();        
        session.delete(user);
        
        if (! recognizedDuplicateIds)
            fail("did not recognize duplicate ids");
    }
}
