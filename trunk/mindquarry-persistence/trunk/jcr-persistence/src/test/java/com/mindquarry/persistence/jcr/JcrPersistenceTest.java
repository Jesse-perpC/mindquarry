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
