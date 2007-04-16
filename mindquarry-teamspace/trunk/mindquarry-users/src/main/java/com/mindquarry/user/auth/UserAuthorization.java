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
package com.mindquarry.user.auth;

import static com.mindquarry.auth.Operations.READ;
import static com.mindquarry.auth.Operations.WRITE;
import static com.mindquarry.auth.Operations.readWrite;
import static com.mindquarry.common.lang.StringUtil.concat;

import java.util.Collection;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import com.mindquarry.auth.ActionRO;
import com.mindquarry.auth.AuthorizationAdmin;
import com.mindquarry.auth.cocoon.AuthorizationException;
import com.mindquarry.user.AbstractUserRO;
import com.mindquarry.user.RoleRO;
import com.mindquarry.user.User;
import com.mindquarry.user.UserAdmin;
import com.mindquarry.user.UserRO;
import com.mindquarry.user.manager.UserManager;
import com.mindquarry.user.webapp.CurrentUser;

/**
 * @author
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public class UserAuthorization implements UserAdmin, BeanFactoryAware {

    private AuthorizationAdmin authAdmin_;
    
    private UserManager userManager_;
    
    private BeanFactory beanFactory_;
    
    /**
     * set by spring at object creation
     */
    public void setAuthAdmin(AuthorizationAdmin authAdmin) {
        authAdmin_ = authAdmin;
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
    
    private String currentUserIdFromRequest() {
        CurrentUser currentUser = 
            (CurrentUser) beanFactory_.getBean(CurrentUser.ROLE);
        String userId = currentUser.getId();
        
        boolean isValidUserId = userId != null && userId.length() != 0;
        if (! isValidUserId) {
            throw new AuthorizationException("");
        }
        
        return userId;
    }
    
    private String usersFolderResourcePath() {
        return "/users";
    }
    
    private String rolesFolderResourcePath() {
        return "/roles";
    }
    
    public String userResourcePath(String userId) {
        return concat(usersFolderResourcePath(), "/", userId);
    }
    
    private void authorizeUserContainerAccess(String operation) {
        
        String currentUserId = currentUserIdFromRequest();
        
        String resourceUri = usersFolderResourcePath();
        if (! authAdmin_.mayPerform(resourceUri, operation, currentUserId)) {
            // not authorised. throw exception
            throw new AuthorizationException("user '" + currentUserId + 
                    "' is not allowed to " + operation + " users.");
        }
    }
    
    private void authorizeRoleContainerAccess(String operation) {
        
        String currentUserId = currentUserIdFromRequest();
        
        String resourceUri = rolesFolderResourcePath();
        if (! authAdmin_.mayPerform(resourceUri, operation, currentUserId)) {
            // not authorised. throw exception
            throw new AuthorizationException("user '" + currentUserId + 
                    "' is not allowed to " + operation + " roles.");
        }
    }
    
    private void authorizeUserAccess(String userId, String operation) {
        
        String currentUserId = currentUserIdFromRequest();
        
        String resourceUri = userResourcePath(userId);
        if (! authAdmin_.mayPerform(resourceUri, operation, currentUserId)) {
            // not authorised. throw exception
            throw new AuthorizationException("user: '" + currentUserId + 
                    "' is not allowed to " + operation + " user: " + userId);
        }
    }

    public User createUser(String id, String password, String name, 
            String surName, String email, String skills) {
        
        authorizeUserContainerAccess(WRITE);
        
        User user = userManager_.createUser(id, password, 
                name, surName, email, skills);
        
        String userUri = userResourcePath(user.getId());
        for (String operation : readWrite()) {
            ActionRO action = authAdmin_.createAction(userUri, operation);
            authAdmin_.addAllowance(action, user);
        }
        
        return user;
    }

    public void deleteUser(User user) {
        
        authorizeUserContainerAccess(WRITE);
        
        userManager_.deleteUser(user);
        
        String resourceUri = userResourcePath(user.getId());
        authAdmin_.deleteResource(resourceUri);
    }

    public void updateUser(User user) {
        authorizeUserAccess(user.getId(), WRITE);        
        userManager_.updateUser(user);
    }

    public User userById(String userId) {
        authorizeUserAccess(userId, READ);
        return userManager_.userById(userId);
    }

    public Collection<? extends UserRO> allUsers() {
        return userManager_.allUsers();
    }

    public RoleRO createRole(String roleId) {
        return userManager_.createRole(roleId);
    }

    public void deleteRole(RoleRO role) {
        userManager_.deleteRole(role);
    }

    public void addUser(AbstractUserRO user, RoleRO role) {
        userManager_.addUser(user, role);
    }

    public void removeUser(AbstractUserRO user, RoleRO role) {
        userManager_.removeUser(user, role);
    }

    public Collection<? extends RoleRO> allRoles() {
        return userManager_.allRoles();
    }

    public RoleRO roleById(String roleId) {
        return userManager_.roleById(roleId);
    }
    
    /**
     * allow a particular user or role access to role resources 
     */
    public void allowAccessToRoleResources(
            String operation, AbstractUserRO abstractUser) {
        
        authorizeRoleContainerAccess(operation);
        
        String resourceUri = rolesFolderResourcePath();
        ActionRO action = authAdmin_.createAction(resourceUri, operation);
        authAdmin_.addAllowance(action, abstractUser);
    }
    
    /**
     * allow a particular user or role access to users resources 
     */
    public void allowAccessToUserResources(
            String operation, AbstractUserRO abstractUser) {

        authorizeUserContainerAccess(operation);
        
        String resourceUri = usersFolderResourcePath();
        ActionRO action = authAdmin_.actionBy(resourceUri, operation);
        authAdmin_.addAllowance(action, abstractUser);
    }
}