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
package com.mindquarry.auth.manager;

import com.mindquarry.auth.ActionRO;
import com.mindquarry.auth.AuthorizationAdmin;
import com.mindquarry.auth.AuthorizationCheck;
import com.mindquarry.teamspace.TeamspaceTestBase;
import com.mindquarry.user.RoleRO;
import com.mindquarry.user.User;
import com.mindquarry.user.UserAdmin;
import com.mindquarry.user.UserQuery;
import com.mindquarry.user.UserRO;

/**
 * Add summary documentation here.
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public class AuthorizationTest extends TeamspaceTestBase {
    
    private static final String fooUserId = "foo-user";
    private static final String fooRoleId = "foo-role";
    
    private static final String user1Id = "user1";
    private static final String user2Id = "user2";

    

    private AuthorizationAdmin authAdmin;
    private AuthorizationCheck authCheck;
    private UserQuery userQuery;
    
    protected void setUp() throws Exception {
        super.setUp();
        
        authAdmin = (AuthorizationAdmin) lookup(AuthorizationAdmin.ROLE);
        authCheck = (AuthorizationCheck) lookup(AuthorizationCheck.ROLE);
        userQuery = (UserQuery) lookup(UserQuery.ROLE);
        
        UserRO fooUser = createUser(fooUserId);
        createUser(user1Id);
        createUser(user2Id);
        
        UserAdmin userAdmin = lookupUserAdmin();                
        RoleRO fooRole = userAdmin.createRole(fooRoleId);
        
        userAdmin.addUser(fooUser, fooRole);
    }
    
    private User createUser(String userId) throws Exception {
        UserAdmin userAdmin = lookupUserAdmin();                
        return userAdmin.createUser(userId, "password", 
                "name", "surname", "email", "skills");
    }
    
    protected void tearDown() throws Exception {
        
        UserAdmin userAdmin = lookupUserAdmin();                
        userAdmin.deleteRole(userAdmin.roleById(fooRoleId));
        
        userAdmin.deleteUser(userAdmin.userById(fooUserId));
        userAdmin.deleteUser(userAdmin.userById(user1Id));
        userAdmin.deleteUser(userAdmin.userById(user2Id));
        
        super.tearDown();
    }
    
    public void testResource() {
        
        final String resource = "/teamspaces/foo-team";
        
        final String readOperation = "READ";
        final String writeOperation = "WRITE";
        
        UserRO user = userQuery.userById(fooUserId);
        ActionRO action = authAdmin.createAction(resource, readOperation);
        authAdmin.addAllowance(action, user);
        
        assertTrue(authCheck.mayPerform(resource, readOperation, user));
        assertFalse(authCheck.mayPerform(resource, writeOperation, user));
        
        authAdmin.deleteAction(action);
    }
    
    public void testResourceTree() {
        final String readOperation = "READ";
        final String writeOperation = "WRITE";
        
        String higherLevelResource = "/teamspaces";
        String explicitGrantedResource = "/teamspaces/foo-team";
        String implicitGrantedResource = "/teamspaces/foo-team/wiki";
        
        UserRO user1 = userQuery.userById(user1Id);
        UserRO user2 = userQuery.userById(user2Id);
        ActionRO action = authAdmin.createAction(explicitGrantedResource, readOperation);
        authAdmin.addAllowance(action, user1);
        
        assertTrue(authAdmin.mayPerform(higherLevelResource, readOperation, user1));
        assertTrue(authAdmin.mayPerform(higherLevelResource, writeOperation, user1));
        assertTrue(authAdmin.mayPerform(higherLevelResource, readOperation, user2));
        assertTrue(authAdmin.mayPerform(higherLevelResource, writeOperation, user2));
        
        assertTrue(authAdmin.mayPerform(explicitGrantedResource, readOperation, user1));
        assertFalse(authAdmin.mayPerform(explicitGrantedResource, writeOperation, user1));
        assertFalse(authAdmin.mayPerform(explicitGrantedResource, readOperation, user2));
        assertFalse(authAdmin.mayPerform(explicitGrantedResource, writeOperation, user2));
        
        assertTrue(authAdmin.mayPerform(implicitGrantedResource, readOperation, user1));
        assertFalse(authAdmin.mayPerform(implicitGrantedResource, writeOperation, user1));
        assertFalse(authAdmin.mayPerform(implicitGrantedResource, readOperation, user2));
        assertFalse(authAdmin.mayPerform(implicitGrantedResource, writeOperation, user2));
        
        authAdmin.deleteAction(action);
    }
    
    public void testDeniedRights() {
        String operation = "READ";
        UserRO user1 = userQuery.userById(user1Id);
        UserRO user2 = userQuery.userById(user2Id);
        
        String fooTeamspace = "/teamspaces/foo-team";
        String fooTeamspaceWiki = "/teamspaces/foo-team/wiki";
        String fooTeamspaceTasks = "/teamspaces/foo-team/tasks";
        
        ActionRO fooReadAction = authAdmin.createAction(fooTeamspace, operation);
        authAdmin.addAllowance(fooReadAction, user1);
        authAdmin.addAllowance(fooReadAction, user2);
                
        ActionRO fooWikiAction = authAdmin.createAction(fooTeamspaceWiki, operation);
        authAdmin.addDenial(fooWikiAction, user2);
        
        assertTrue(authAdmin.mayPerform(fooTeamspace, operation, user1));
        assertTrue(authAdmin.mayPerform(fooTeamspace, operation, user2));
        
        assertTrue(authAdmin.mayPerform(fooTeamspaceTasks, operation, user1));
        assertTrue(authAdmin.mayPerform(fooTeamspaceTasks, operation, user2));
        
        assertTrue(authAdmin.mayPerform(fooTeamspaceWiki, operation, user1));
        assertFalse(authAdmin.mayPerform(fooTeamspaceWiki, operation, user2));
        
        authAdmin.deleteAction(fooReadAction);
        authAdmin.deleteAction(fooWikiAction);
    }    
    
    public void testGroupAllowances() {
        final String operation = "READ";
        final UserRO fooUser = userQuery.userById(fooUserId);
        final RoleRO fooRole = userQuery.roleById(fooRoleId);
        
        String fooTeamspace = "/teamspaces/foo-team";
        
        ActionRO fooReadAction = authAdmin.createAction(fooTeamspace, operation);        
        authAdmin.addAllowance(fooReadAction, fooRole);
                
        assertTrue(authAdmin.mayPerform(fooTeamspace, operation, fooUser));
        
        authAdmin.deleteAction(fooReadAction);
    }
}
