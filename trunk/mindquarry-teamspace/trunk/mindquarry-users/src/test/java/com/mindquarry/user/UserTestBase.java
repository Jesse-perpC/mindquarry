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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.avalon.framework.service.ServiceException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;

import com.mindquarry.auth.manager.ActionEntity;
import com.mindquarry.auth.manager.ResourceEntity;
import com.mindquarry.jcr.jackrabbit.JcrSimpleTestBase;
import com.mindquarry.persistence.api.JavaConfiguration;
import com.mindquarry.persistence.api.SessionFactory;
import com.mindquarry.user.manager.RoleEntity;
import com.mindquarry.user.manager.UserEntity;
import com.mindquarry.user.util.Initializer;
import com.mindquarry.user.webapp.CurrentUser;

/**
 * @author <a href="bastian(dot)steinert(at)mindquarry(dot)com">Bastian Steinert</a>
 */
public abstract class UserTestBase extends JcrSimpleTestBase {
    
    protected List<String> springConfigClasspathResources() {
        List<String> result = super.springConfigClasspathResources();    
        result.add("META-INF/cocoon/spring/jcr-persistence-context.xml");
        result.add("META-INF/cocoon/spring/authorization-context.xml");
        result.add("META-INF/cocoon/spring/user-context.xml");
        return result;
    }
    
    private void registerCurrentUserBeanAsSingleton() {
        BeanDefinition beanDefiniton = 
            new RootBeanDefinition(CurrentUser.class);
        beanDefiniton.setScope(BeanDefinition.SCOPE_SINGLETON);
        
        BeanDefinitionRegistry beanRegistry = 
            (BeanDefinitionRegistry) getBeanFactory(); 
        beanRegistry.registerBeanDefinition(CurrentUser.ROLE, beanDefiniton);
    }

    protected void setUp() throws Exception {
        super.setUp();
        registerCurrentUserBeanAsSingleton(); 
        
        JavaConfiguration configuration = new JavaConfiguration();
        for (Class clazz : entityClasses()) {
            configuration.addClass(clazz);
        }
        
        SessionFactory sessionFactory = 
            (SessionFactory) lookup(SessionFactory.ROLE);        
        sessionFactory.configure(configuration);
        
        lookupUserInitializer().initialize();
    }
    
    protected Collection<Class> entityClasses() {
        Collection<Class> result = new ArrayList<Class>();
        
        result.add(UserEntity.class);
        result.add(RoleEntity.class);

        result.add(ActionEntity.class);
        result.add(ResourceEntity.class);
        
        return result;
    }
    
    protected UserAdmin lookupUserAdmin() throws ServiceException {
        return (UserAdmin) lookup(UserAdmin.class.getName());
    }
    
    protected Initializer lookupUserInitializer() throws ServiceException {
        return (Initializer) lookup(Initializer.ROLE);
    }
}
