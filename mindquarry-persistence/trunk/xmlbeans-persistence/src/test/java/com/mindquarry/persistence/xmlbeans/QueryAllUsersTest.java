package com.mindquarry.persistence.xmlbeans;

import java.util.List;

import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import org.apache.avalon.framework.service.ServiceException;

import com.mindquarry.common.persistence.Session;
import com.mindquarry.common.persistence.SessionFactory;
import com.mindquarry.types.teamspace.Teamspace;
import com.mindquarry.types.user.Email;
import com.mindquarry.types.user.User;

public class QueryAllUsersTest extends XmlBeansPersistenceTestBase {

    public void testConversation() throws ServiceException, NotSupportedException, SystemException {
                
        String[] userIds = new String[] {"bastian", "lars", "alex", "alexs"};
        SessionFactory sessionFactory = (SessionFactory) lookup(SessionFactory.ROLE);
        Session session = sessionFactory.currentSession();
        
        for (String userId : userIds)
            makeUser(session, userId);
        
        session.commit();
        
        
        session = sessionFactory.currentSession();
        
        List queryResult = session.query("getAllUsers", new Object[0]);
        assertEquals(4, queryResult.size());
        
        for (Object user : queryResult)
            session.delete(user);
        
        session.commit();
    }
    
    private User makeUser(Session session, String userId) {
        User result = (User) session.newEntity(User.class);
        
        result.setId(userId);
        result.setPassword("password");
        
        result.setName("firstname");
        result.setSurname("lastname");
        
        Email email = result.addNewEmail();
        email.setAddress("firstname.lastname@mindquarry.com");
        email.setIsConversationRecipient(true);
        
        return result;
    }
}
