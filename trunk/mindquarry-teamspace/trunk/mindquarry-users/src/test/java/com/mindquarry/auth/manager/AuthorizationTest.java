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

import static com.mindquarry.auth.Operations.READ;
import static com.mindquarry.auth.Operations.WRITE;
import static com.mindquarry.user.util.DefaultUsers.adminLogin;

import com.mindquarry.auth.ActionRO;
import com.mindquarry.auth.AuthorizationAdmin;
import com.mindquarry.auth.AuthorizationCheck;
import com.mindquarry.user.RoleRO;
import com.mindquarry.user.User;
import com.mindquarry.user.UserAdmin;
import com.mindquarry.user.UserQuery;
import com.mindquarry.user.UserRO;
import com.mindquarry.user.UserTestBase;

/**
 * Add summary documentation here.
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public class AuthorizationTest extends UserTestBase {
    
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
        
        UserRO user = userQuery.userById(fooUserId);
        ActionRO action = authAdmin.createAction(resource, READ);
        authAdmin.addAllowance(action, user);
        
        assertTrue(authCheck.mayPerform(resource, READ, user));
        assertFalse(authCheck.mayPerform(resource, WRITE, user));
        
        authAdmin.deleteResource(resource);
    }
    
    public void testResourceTree() {        
        String higherLevelResource = "/teamspaces";
        String explicitGrantedResource = "/teamspaces/foo-team";
        String implicitGrantedResource = "/teamspaces/foo-team/wiki";
        
        UserRO admin = userQuery.userById(adminLogin());
        UserRO user1 = userQuery.userById(user1Id);
        ActionRO action = authAdmin.createAction(explicitGrantedResource, READ);
        authAdmin.addAllowance(action, user1);
        
        assertTrue(authAdmin.mayPerform(higherLevelResource, READ, admin));
        assertTrue(authAdmin.mayPerform(higherLevelResource, WRITE, admin));
        assertFalse(authAdmin.mayPerform(higherLevelResource, READ, user1));
        assertFalse(authAdmin.mayPerform(higherLevelResource, WRITE, user1));
        
        assertTrue(authAdmin.mayPerform(explicitGrantedResource, READ, user1));
        assertFalse(authAdmin.mayPerform(explicitGrantedResource, WRITE, user1));
        
        assertTrue(authAdmin.mayPerform(implicitGrantedResource, READ, user1));
        assertFalse(authAdmin.mayPerform(implicitGrantedResource, WRITE, user1));
        
        authAdmin.deleteResource(explicitGrantedResource);
    }
    
    public void testDeniedRights() {
        UserRO user1 = userQuery.userById(user1Id);
        UserRO user2 = userQuery.userById(user2Id);
        
        String fooTeamspace = "/teamspaces/foo-team";
        String fooTeamspaceWiki = "/teamspaces/foo-team/wiki";
        String fooTeamspaceTasks = "/teamspaces/foo-team/tasks";
        
        ActionRO fooReadAction = authAdmin.createAction(fooTeamspace, READ);
        authAdmin.addAllowance(fooReadAction, user1);
        authAdmin.addAllowance(fooReadAction, user2);
                
        ActionRO fooReadWikiAction = authAdmin.createAction(fooTeamspaceWiki, READ);
        authAdmin.addDenial(fooReadWikiAction, user2);
        
        assertTrue(authAdmin.mayPerform(fooTeamspace, READ, user1));
        assertTrue(authAdmin.mayPerform(fooTeamspace, READ, user2));
        
        assertTrue(authAdmin.mayPerform(fooTeamspaceTasks, READ, user1));
        assertTrue(authAdmin.mayPerform(fooTeamspaceTasks, READ, user2));
        
        assertTrue(authAdmin.mayPerform(fooTeamspaceWiki, READ, user1));
        assertFalse(authAdmin.mayPerform(fooTeamspaceWiki, READ, user2));
        
        authAdmin.deleteResource(fooTeamspace);
    }    
    
    public void testGroupAllowances() {
        final UserRO fooUser = userQuery.userById(fooUserId);
        final RoleRO fooRole = userQuery.roleById(fooRoleId);
        
        String fooTeamspace = "/teamspaces/foo-team";
        
        ActionRO fooReadAction = authAdmin.createAction(fooTeamspace, READ);        
        authAdmin.addAllowance(fooReadAction, fooRole);
                
        assertTrue(authAdmin.mayPerform(fooTeamspace, READ, fooUser));
        
        authAdmin.deleteResource(fooTeamspace);
    }
}
