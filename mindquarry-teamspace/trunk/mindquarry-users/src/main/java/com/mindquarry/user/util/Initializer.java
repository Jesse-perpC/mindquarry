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
package com.mindquarry.user.util;

import static com.mindquarry.auth.Operations.readWrite;
import static com.mindquarry.user.util.DefaultUsers.EVERYONE_ROLE;
import static com.mindquarry.user.util.DefaultUsers.USERS_ROLE;
import static com.mindquarry.user.util.DefaultUsers.adminLogin;
import static com.mindquarry.user.util.DefaultUsers.defaultRoleUsers;
import static com.mindquarry.user.util.DefaultUsers.defaultUsers;
import static com.mindquarry.user.util.DefaultUsers.login;
import static com.mindquarry.user.util.DefaultUsers.password;
import static com.mindquarry.user.util.DefaultUsers.username;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import com.mindquarry.user.RoleRO;
import com.mindquarry.user.UserRO;
import com.mindquarry.user.auth.UserAuthorization;
import com.mindquarry.user.manager.UserManager;
import com.mindquarry.user.webapp.CurrentUser;

/**
 * @author
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public class Initializer implements BeanFactoryAware {
    
    public static final String ROLE = Initializer.class.getName();
        
    private Log log_ = LogFactory.getLog(this.getClass());
    
    private UserAuthorization userAuthorization_;
    
    private UserManager userManager_;
    
    private BeanFactory beanFactory_;
    
    /**
     * set by spring at object creation
     */
    public void setUserAuthorization(UserAuthorization userAuthorization) {
        userAuthorization_ = userAuthorization;
    }

    /**
     * set by spring at object creation
     */
    public void setUserManager(UserManager userManager) {
        userManager_ = userManager;
    }

    /**
     * set by spring at object creation
     */
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        beanFactory_ = beanFactory;
    }
    
    private void setCurrentUserTo(String userId) {
        CurrentUser currentUser = 
            (CurrentUser) beanFactory_.getBean(CurrentUser.ROLE);
        
        currentUser.setId(userId);
    }
    
    public void initialize() {
        initializeUsers();
        initializeRoles();
    }
    
    private void initializeUsers() {
        for (String[] userProfile : defaultUsers()) {
            if (!existsUser(userProfile)) {
                createUser(userProfile);
                log_.info("created default users: " + login(userProfile));
            }
        }
    }
    
    private void initializeRoles() {
        if (! existsRole(EVERYONE_ROLE)) {
            createRole(EVERYONE_ROLE);
        }
        
        if (! existsRole(USERS_ROLE)) {
            RoleRO role = createRole(USERS_ROLE);
            
            // during initialization there is no request context
            // but the UserAuthorization component user request scoped bean
            // CurrentUser to check if the current user is privileged for 
            // a particular operation.
            // Thus we temporarily set admin as the current user.
            setCurrentUserTo(adminLogin());
            for (String operation : readWrite()) {
                userAuthorization_.allowAccessToUserResources(operation, role);
                userAuthorization_.allowAccessToRoleResources(operation, role);
                
            }
            setCurrentUserTo(null);
        }
    }

    private boolean existsUser(String[] userProfile) {
        return null != userManager_.userById(login(userProfile));
    }

    private void createUser(String[] userProfile) {
        userManager_.createUser(login(userProfile), password(userProfile), 
                   username(userProfile), "", null, null);
    }

    private boolean existsRole(String role) {
        return null != userManager_.roleById(role);
    }

    private RoleRO createRole(String roleId) {
        RoleRO role = userManager_.createRole(roleId);
        for (String[] userProfile : defaultRoleUsers(roleId)) {
            UserRO user = userManager_.userById(login(userProfile));
            userManager_.addUser(user, role);
        }
        log_.info("created default role: " + roleId);
        return role;
    }
}
