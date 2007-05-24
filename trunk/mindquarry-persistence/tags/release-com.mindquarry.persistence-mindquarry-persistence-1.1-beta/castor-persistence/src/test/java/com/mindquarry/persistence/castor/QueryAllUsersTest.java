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

import com.mindquarry.common.persistence.Session;
import com.mindquarry.common.persistence.SessionFactory;

public class QueryAllUsersTest extends CastorPersistenceTestBase {

    public void testConversation() throws ServiceException, NotSupportedException, SystemException {
                
        String[] userIds = new String[] {"bastian", "lars", "alex", "alexs"};
        
        for (String userId : userIds)
            makeUser(userId);
        
        SessionFactory sessionFactory = (SessionFactory) lookup(SessionFactory.ROLE);
        Session session = sessionFactory.currentSession();
        
        List queryResult = session.query("getAllUsers", new Object[0]);
        assertEquals(4, queryResult.size());
        
        for (Object user : queryResult)
            session.delete(user);
        
        session.commit();
    }
    
    private UserEntity makeUser(String userId) throws ServiceException {
        UserEntity result = new UserEntity();        
        result.setId(userId);
        
        result.setName("firstname");
        result.setSurname("lastname");
        
        SessionFactory sessionFactory = (SessionFactory) lookup(SessionFactory.ROLE);
        Session session = sessionFactory.currentSession();
        session.persist(result);
        session.commit();
        
        return result;
    }
}
