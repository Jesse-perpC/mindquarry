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

public class XmlBeansPersistenceTest extends XmlBeansPersistenceTestBase {

    public void testConversation() throws ServiceException, NotSupportedException, SystemException {
                
        SessionFactory sessionFactory = (SessionFactory) lookup(SessionFactory.ROLE);
        Session session = sessionFactory.currentSession();
        
        User user = (User) session.newEntity(User.class);
        
        user.setId("bastian");
        user.setPassword("password");
        
        user.setName("Bastain");
        user.setSurname("Steinert");
        
        Email email = user.addNewEmail();
        email.setAddress("bastian.steinert@mindquarry.com");
        email.setIsConversationRecipient(true);
        
        Teamspace teamspace = (Teamspace) session.newEntity(Teamspace.class);
        teamspace.setDescription("a great description");
        teamspace.setId("mindquarry");
        teamspace.setName("Mindquarry");
        
        session.commit();
        
        
        session = sessionFactory.currentSession();
        
        List queryResult = session.query("getUserById", new Object[] {"bastian"});
        User persistentUser = (User) queryResult.get(0);
        assertEquals("bastian", persistentUser.getId());
        
        session.delete(persistentUser);
        
        session.commit();
        
        
        session = sessionFactory.currentSession();
        
        queryResult = session.query("getUserById", new Object[] {"bastian"});
        assertTrue(queryResult.isEmpty());       
        
        session.commit();
    }
}
