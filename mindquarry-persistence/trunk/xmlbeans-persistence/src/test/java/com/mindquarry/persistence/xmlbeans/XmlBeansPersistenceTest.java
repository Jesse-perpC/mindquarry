package com.mindquarry.persistence.xmlbeans;

import java.util.List;

import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.apache.avalon.framework.service.ServiceException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.jta.JtaTransactionManager;

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
        
        session.commit();
        
        List queryResult = session.query("getUserById", new Object[] {"bastian"});
        User queriedUser = (User) queryResult.get(0);
        assertEquals("bastian", queriedUser.getId());
        
        
        Teamspace teamspace = (Teamspace) session.newEntity(Teamspace.class);
        teamspace.setDescription("a great description");
        teamspace.setId("mindquarry");
        teamspace.setName("Mindquarry");
        
        session.commit();
    }
    
    private UserTransaction newUserTransaction() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("/META-INF/spring/xmlbeans-persistence-context.xml", this.getClass());
        JtaTransactionManager txManager = (JtaTransactionManager) ctx.getBean("transactionManager");
        return txManager.getUserTransaction();
    }
}
