package com.mindquarry.persistence.xmlbeans;

import java.util.List;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.DefaultConfiguration;

import com.mindquarry.common.persistence.Session;
import com.mindquarry.jcr.jackrabbit.JCRTestBaseStandalone;
import com.mindquarry.types.user.Email;
import com.mindquarry.types.user.User;

public class UserTestStandalone extends JCRTestBaseStandalone {
	/**
	 * @see com.mindquarry.jcr.jackrabbit.JCRTestBaseStandalone#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * @see com.mindquarry.jcr.jackrabbit.JCRTestBaseStandalone#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testConversation() throws Exception {
        
		XmlBeansSessionFactoryStandalone sessionFactory;
		sessionFactory = new XmlBeansSessionFactoryStandalone();
        sessionFactory.enableLogging(getLogger());
        sessionFactory.configure(jcrCredentialsConfig());
		sessionFactory.initialize();

		Session session = sessionFactory.currentSession();

		User user = (User) session.newEntity(User.class);

		user.setId("bastian");
		user.setPassword("password");

		user.setName("Bastain");
		user.setSurname("Steinert");

		Email email = user.addNewEmail();
		email.setAddress("bastian.steinert@mindquarry.com");
		email.setIsConversationRecipient(true);

		session.persist(user);

		List queryResult = session.query("getUserById",
				new Object[] { "bastian" });
		User queriedUser = (User) queryResult.get(0);
		assertEquals("bastian", queriedUser.getId());
	}

    private Configuration jcrCredentialsConfig() {
        DefaultConfiguration result = new DefaultConfiguration("credentials");
        result.setAttribute("login", "alexander.saar");
        result.setAttribute("password", "myPwd");
        result.setAttribute("rmi", "rmi://localhost:1099/jackrabbit");
        return result;
    }
    
}
