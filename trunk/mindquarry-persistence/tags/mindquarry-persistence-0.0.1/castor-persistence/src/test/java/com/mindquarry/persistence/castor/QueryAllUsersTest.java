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
