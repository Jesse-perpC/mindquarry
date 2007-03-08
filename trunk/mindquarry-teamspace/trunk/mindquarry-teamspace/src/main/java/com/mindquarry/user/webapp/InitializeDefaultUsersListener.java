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
package com.mindquarry.user.webapp;

import static com.mindquarry.user.manager.DefaultUsers.defaultUsers;
import static com.mindquarry.user.manager.DefaultUsers.login;
import static com.mindquarry.user.manager.DefaultUsers.password;
import static com.mindquarry.user.manager.DefaultUsers.username;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.mindquarry.common.init.InitializationException;
import com.mindquarry.user.UserAdmin;

/**
 * Ensures that all defaults users exist.
 * This includes the admin users and the index user.
 * A mindquarry system admin can also enable (config file)
 * an anonymous user. 
 * 
 * @author 
 * <a href="mailto:bastian(dot)steinert(at)mindquarry(dot)com">Bastian Steinert</a>
 */
public class InitializeDefaultUsersListener implements ServletContextListener {
    
    private Log log_ = LogFactory.getLog(InitializeDefaultUsersListener.class);

    /**
     * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent destroyedEvent) {
        // nothing to do here
    }

    /**
     * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent initializedEvent) {
        log_.info("started initializing default users");
        ServletContext servletContext = initializedEvent.getServletContext();
        BeanFactory beanFactory = lookupBeanFactory(servletContext);

        UserAdmin userAdmin = lookupUserAdmin(beanFactory);
        
        for (String[] userProfile : defaultUsers()) {
            if (!existsUser(userAdmin, userProfile)) {
                createUser(userAdmin, userProfile);
                log_.info("created default users: " + login(userProfile));
            }
        }
        log_.info("finished initializing default users");
    }
    
    private BeanFactory lookupBeanFactory(ServletContext servletContext) {
        return WebApplicationContextUtils
                .getRequiredWebApplicationContext(servletContext);
    }
    
    private UserAdmin lookupUserAdmin(BeanFactory beanFactory) {
        
        String userAdminBeanName = UserAdmin.class.getName();
        
        if (! beanFactory.containsBean(userAdminBeanName)) {
            throw new InitializationException(
                    "there is no spring bean with name: " + 
                    userAdminBeanName + " available.");
        }
        
        return (UserAdmin) beanFactory.getBean(userAdminBeanName);
    }

    private boolean existsUser(UserAdmin userAdmin, String[] userProfile) {
        return null != userAdmin.userById(login(userProfile));
    }

    private void createUser(UserAdmin userAdmin, String[] userProfile) {
        userAdmin.createUser(login(userProfile), password(userProfile), 
                   username(userProfile), "", null, null);
    }
}
