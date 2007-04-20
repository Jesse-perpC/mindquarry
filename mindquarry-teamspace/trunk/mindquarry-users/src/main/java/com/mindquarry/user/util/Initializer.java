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

import static com.mindquarry.auth.Operations.READ;
import static com.mindquarry.user.util.DefaultUsers.ROLE_EVERYONE;
import static com.mindquarry.user.util.DefaultUsers.ROLE_USER;
import static com.mindquarry.user.util.DefaultUsers.defaultRoleUsers;
import static com.mindquarry.user.util.DefaultUsers.defaultUsers;
import static com.mindquarry.user.util.DefaultUsers.login;
import static com.mindquarry.user.util.DefaultUsers.password;
import static com.mindquarry.user.util.DefaultUsers.username;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mindquarry.user.RoleRO;
import com.mindquarry.user.UserRO;
import com.mindquarry.user.auth.UserAuthorization;
import com.mindquarry.user.manager.UserManager;

/**
 * @author
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public class Initializer {
    
    public static final String ROLE = Initializer.class.getName();
        
    private Log log_ = LogFactory.getLog(this.getClass());
    
    private UserAuthorization userAuthorization_;
    
    private UserManager userManager_;
    
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
        
        if (! existsRole(ROLE_EVERYONE)) {
            createRole(ROLE_EVERYONE);
        }
        
        if (! existsRole(ROLE_USER)) {
            RoleRO role = createRole(ROLE_USER);
            
            userAuthorization_.allowAccessToUserResources(READ, role);
            userAuthorization_.allowAccessToRoleResources(READ, role);
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
