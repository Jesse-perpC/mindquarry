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

import static com.mindquarry.auth.Operations.*;
import static com.mindquarry.user.util.DefaultUsers.*;
import static com.mindquarry.common.lang.StringUtil.concat;

import java.util.Collection;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import com.mindquarry.auth.ActionRO;
import com.mindquarry.auth.AuthorizationAdmin;
import com.mindquarry.auth.AuthorizationException;
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

    private static final String USERS_FOLDER = "users";
    private static final String ROLES_FOLDER = "roles";
    
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
            throw new AuthorizationException("the request context does not" +
                    "contain a valid id of the current user");
        }
        
        return userId;
    }
    
    private String usersFolderResourcePath() {
        return "/" + USERS_FOLDER;
    }
    
    private String rolesFolderResourcePath() {
        return "/" + ROLES_FOLDER;
    }
    
    public String userResourcePath(String userId) {
        return concat(usersFolderResourcePath(), "/", userId);
    }
    
    public String roleResourcePath(String roleId) {
        return concat(rolesFolderResourcePath(), "/", roleId);
    }
    
    private void validateOperation(String operation) {
        assert defaultOperations().contains(operation) : 
            operation + " is not a default operation.";
    }
    
    private void authorizeUserContainerAccess(String operation) {
        String resourceUri = usersFolderResourcePath();
        authorizeContainerAccess(operation, resourceUri, USERS_FOLDER);
    }
    
    private void authorizeRoleContainerAccess(String operation) {
        String resourceUri = rolesFolderResourcePath();
        authorizeContainerAccess(operation, resourceUri, ROLES_FOLDER);
    }
    
    private void authorizeContainerAccess(String operation, 
            String resourceUri, String resourceName) {
        
        validateOperation(operation);
        
        String currentUserId = currentUserIdFromRequest();
        
        if (! authAdmin_.mayPerform(resourceUri, operation, currentUserId)) {
            // not authorised. throw exception
            throw new AuthorizationException("user '" + currentUserId + 
                    "' is not allowed to " + operation + " " + resourceName);
        }
    }
    
    private void authorizeUserAccess(String operation, String userId) {        
        String resourceUri = userResourcePath(userId);
        authorizeEntityAccess(operation, resourceUri, userId, "user");
    }
    
    private void authorizeRoleAccess(String operation, String roleId) {        
        String resourceUri = roleResourcePath(roleId);
        authorizeEntityAccess(operation, resourceUri, roleId, "role");
    }
    
    private void authorizeEntityAccess(String operation, String resourceUri,
            String entityId, String resourceName) {
        
        validateOperation(operation);
        
        String currentUserId = currentUserIdFromRequest();
        
        if (! authAdmin_.mayPerform(resourceUri, operation, currentUserId)) {
            // not authorised. throw exception
            throw new AuthorizationException("user: '" + currentUserId + 
                    "' is not allowed to " + operation + " " + 
                    resourceName + ": " + entityId);
        }
    }

    public User createUser(String id, String password, String name, 
            String surName, String email, String skills) {
        
        long start = System.currentTimeMillis();
        authorizeUserContainerAccess(WRITE);
        for (String roleId : defaultUserRoles()) {
            authorizeRoleAccess(WRITE, roleId);
        }
        
        long s = System.currentTimeMillis();
        User user = userManager_.createUser(id, password, 
                name, surName, email, skills);
        long e = System.currentTimeMillis();
        
        String resourceUri = userResourcePath(user.getId());
        for (String operation : readWrite()) {
            ActionRO action = authAdmin_.createAction(resourceUri, operation);
            authAdmin_.addAllowance(action, user);
        }
        
        for (String roleId : defaultUserRoles()) {
            RoleRO role = userManager_.roleById(roleId);
            userManager_.addUser(user, role);
        }
        long end = System.currentTimeMillis();
        System.out.println("create user: " + (e - s));
        System.out.println("complete create user: " + (end - start));
        return user;
    }

    public void deleteUser(User user) {
        
        authorizeUserContainerAccess(WRITE);
        for (String role : defaultUserRoles()) {
            authorizeRoleAccess(WRITE, role);
        }
        
        for (String roleId : defaultUserRoles()) {
            RoleRO role = userManager_.roleById(roleId);
            userManager_.removeUser(user, role);
        }
        
        String resourceUri = userResourcePath(user.getId());
        authAdmin_.deleteResource(resourceUri);
        
        userManager_.deleteUser(user);
    }

    public void updateUser(User user) {
        authorizeUserAccess(WRITE, user.getId());        
        userManager_.updateUser(user);
    }

    public User userById(String userId) {
        authorizeUserAccess(READ, userId);
        return userManager_.userById(userId);
    }

    public Collection<? extends UserRO> allUsers() {
        authorizeUserContainerAccess(READ); 
        return userManager_.allUsers();
    }

    public RoleRO createRole(String roleId) {
        authorizeRoleContainerAccess(WRITE);
        return userManager_.createRole(roleId);
    }

    public void deleteRole(RoleRO role) {
        authorizeRoleContainerAccess(WRITE);
        userManager_.deleteRole(role);
    }

    public void addUser(AbstractUserRO user, RoleRO role) {
        authorizeRoleAccess(WRITE, role.getId());
        userManager_.addUser(user, role);
    }

    public void removeUser(AbstractUserRO user, RoleRO role) {
        authorizeRoleAccess(WRITE, role.getId());
        userManager_.removeUser(user, role);
    }

    public Collection<? extends RoleRO> allRoles() {
        authorizeRoleContainerAccess(READ);
        return userManager_.allRoles();
    }

    public RoleRO roleById(String roleId) {
        authorizeRoleAccess(READ, roleId);
        return userManager_.roleById(roleId);
    }
    
    /**
     * allow a particular user or role access to role resources 
     */
    public void allowAccessToRoleResources(
            String operation, AbstractUserRO abstractUser) {
        
        String resourceUri = rolesFolderResourcePath();
        ActionRO action = authAdmin_.createAction(resourceUri, operation);
        authAdmin_.addAllowance(action, abstractUser);
    }
    
    /**
     * allow a particular user or role access to users resources 
     */
    public void allowAccessToUserResources(
            String operation, AbstractUserRO abstractUser) {
        
        String resourceUri = usersFolderResourcePath();
        ActionRO action = authAdmin_.actionBy(resourceUri, operation);
        authAdmin_.addAllowance(action, abstractUser);
    }
}