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
package com.mindquarry.user;

import static com.mindquarry.user.manager.DefaultUsers.defaultUsers;
import static com.mindquarry.user.manager.DefaultUsers.login;
import static com.mindquarry.user.manager.DefaultUsers.password;
import static com.mindquarry.user.manager.DefaultUsers.username;

import java.util.List;

import org.apache.avalon.framework.service.ServiceException;

import com.mindquarry.jcr.jackrabbit.JcrSimpleTestBase;
import com.mindquarry.persistence.api.JavaConfiguration;
import com.mindquarry.persistence.api.SessionFactory;
import com.mindquarry.user.manager.RoleEntity;
import com.mindquarry.user.manager.UserEntity;

/**
 * @author <a href="bastian(dot)steinert(at)mindquarry(dot)com">Bastian Steinert</a>
 */
public abstract class UserTestBase extends JcrSimpleTestBase {
    
    protected List<String> springConfigClasspathResources() {
        List<String> result = super.springConfigClasspathResources();    
        result.add("META-INF/cocoon/spring/jcr-persistence-context.xml");
        result.add("META-INF/cocoon/spring/user-context.xml");
        return result;
    }

    protected void setUp() throws Exception {
        super.setUp();
        
        JavaConfiguration configuration = new JavaConfiguration();
        configuration.addClass(UserEntity.class);
        configuration.addClass(RoleEntity.class);
        
        SessionFactory sessionFactory = 
            (SessionFactory) lookup(SessionFactory.ROLE);        
        sessionFactory.configure(configuration);
        
        UserAdmin userAdmin = lookupUserAdmin();
        for (String[] userProfile : defaultUsers()) {
            userAdmin.createUser(login(userProfile), password(userProfile), 
                    username(userProfile), "", null, null);
        }
    }
    
    protected void tearDown() throws Exception {
        UserAdmin userAdmin = lookupUserAdmin();
        for (String[] userProfile : defaultUsers()) {
            User user = userAdmin.userById(login(userProfile));
            userAdmin.deleteUser(user);
        }
        super.tearDown();
    }
    
    protected UserAdmin lookupUserAdmin() throws ServiceException {
        return (UserAdmin) lookup(UserAdmin.class.getName());
    }
}
