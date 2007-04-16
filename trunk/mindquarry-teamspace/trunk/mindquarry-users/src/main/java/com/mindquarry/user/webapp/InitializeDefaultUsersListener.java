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

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.mindquarry.common.init.InitializationException;
import com.mindquarry.user.util.Initializer;

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
        log_.info("started initializing default users and roles");
        ServletContext servletContext = initializedEvent.getServletContext();
        BeanFactory beanFactory = lookupBeanFactory(servletContext);

        lookupUserInitializer(beanFactory).initialize();
        
        log_.info("finished initializing default users and roles");
    }
    
    private BeanFactory lookupBeanFactory(ServletContext servletContext) {
        return WebApplicationContextUtils
                .getRequiredWebApplicationContext(servletContext);
    }
    
    private Initializer lookupUserInitializer(BeanFactory beanFactory) {
                
        if (! beanFactory.containsBean(Initializer.ROLE)) {
            throw new InitializationException(
                    "there is no spring bean with name: " + 
                    Initializer.ROLE + " available.");
        }
        
        return (Initializer) beanFactory.getBean(Initializer.ROLE);
    }
}
