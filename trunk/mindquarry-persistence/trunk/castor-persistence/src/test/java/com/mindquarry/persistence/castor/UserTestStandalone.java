/*
 * Copyright (C) 2006-2007 MindQuarry GmbH, All Rights Reserved
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

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.DefaultConfiguration;

import com.mindquarry.jcr.jackrabbit.JCRTestBaseStandalone;

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
//        
//		CastorSessionFactoryStandalone sessionFactory;
//		sessionFactory = new CastorSessionFactoryStandalone();
//        sessionFactory.enableLogging(getLogger());
//        sessionFactory.configure(jcrCredentialsConfig());
//		sessionFactory.initialize();
//
//		Session session = sessionFactory.currentSession();
//
//		UserEntity user = (UserEntity) session.newEntity(UserEntity.class);
//
//		user.setId("bastian");
//
//		user.setName("Bastain");
//		user.setSurname("Steinert");
//
//		session.commit();
//
//		List queryResult = session.query("getUserById",
//				new Object[] { "bastian" });
//        UserEntity queriedUser = (UserEntity) queryResult.get(0);
//		assertEquals("bastian", queriedUser.getId());
	}

    private Configuration jcrCredentialsConfig() {
        DefaultConfiguration result = new DefaultConfiguration("credentials");
        result.setAttribute("login", "alexander.saar");
        result.setAttribute("password", "myPwd");
        result.setAttribute("rmi", "rmi://localhost:1099/jackrabbit");
        return result;
    }
    
}
