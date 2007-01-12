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

import com.mindquarry.auth.manager.Authorization;
import com.mindquarry.auth.manager.ProfileEntity;
import com.mindquarry.auth.manager.RightEntity;
import com.mindquarry.teamspace.TeamspaceTestBase;
import com.mindquarry.user.GroupRO;
import com.mindquarry.user.UserAdmin;
import com.mindquarry.user.UserRO;

/**
 * Add summary documentation here.
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public class AuthorizationTest extends TeamspaceTestBase {

    private Authorization auth;
    private UserAdmin userAdmin;
    
    protected void setUp() throws Exception {
        super.setUp();
        this.auth = new Authorization();
        this.userAdmin = lookupUserAdmin();
    }
    
    private UserRO createUser(String userId) {
        return this.userAdmin.createUser(userId, "password", 
                "name", "surname", "email", "skills");
    }
    
    public void testResource() {
        final UserRO user = this.createUser("testUser");
        final String resource = "/teamspaces/foo-team";
        
        final String readOperation = "READ";
        final String writeOperation = "WRITE";
        
        RightEntity right = this.auth.createRight(resource, readOperation);
        this.auth.addAllowance(right, user);
        assertTrue(this.auth.mayPerform(resource, readOperation, user));
        assertFalse(this.auth.mayPerform(resource, writeOperation, user));
    }
    
    public void testResourceTree() {
        final String readOperation = "READ";
        final String writeOperation = "WRITE";
        UserRO grantedUser = this.createUser("grantedUser");
        UserRO otherUser = this.createUser("otherUser");
        
        String higherLevelResource = "/teamspaces";
        String explicitGrantedResource = "/teamspaces/foo-team";
        String implicitGrantedResource = "/teamspaces/foo-team/wiki";
        
        RightEntity right = this.auth.createRight(explicitGrantedResource, readOperation);
        this.auth.addAllowance(right, grantedUser);
        
        assertTrue(this.auth.mayPerform(higherLevelResource, readOperation, grantedUser));
        assertTrue(this.auth.mayPerform(higherLevelResource, writeOperation, grantedUser));
        assertTrue(this.auth.mayPerform(higherLevelResource, readOperation, otherUser));
        assertTrue(this.auth.mayPerform(higherLevelResource, writeOperation, otherUser));
        
        assertTrue(this.auth.mayPerform(explicitGrantedResource, readOperation, grantedUser));
        assertFalse(this.auth.mayPerform(explicitGrantedResource, writeOperation, grantedUser));
        assertFalse(this.auth.mayPerform(explicitGrantedResource, readOperation, otherUser));
        assertFalse(this.auth.mayPerform(explicitGrantedResource, writeOperation, otherUser));
        
        assertTrue(this.auth.mayPerform(implicitGrantedResource, readOperation, grantedUser));
        assertFalse(this.auth.mayPerform(implicitGrantedResource, writeOperation, grantedUser));
        assertFalse(this.auth.mayPerform(implicitGrantedResource, readOperation, otherUser));
        assertFalse(this.auth.mayPerform(implicitGrantedResource, writeOperation, otherUser));
    }
    
    public void testDeniedRights() {
        String operation = "READ";
        UserRO fooUser = this.createUser("fooUser");
        UserRO fooTasksOnlyUser = this.createUser("fooTasksUser");
        
        String fooTeamspace = "/teamspaces/foo-team";
        String fooTeamspaceWiki = "/teamspaces/foo-team/wiki";
        String fooTeamspaceTasks = "/teamspaces/foo-team/tasks";
        
        RightEntity fooReadRight = this.auth.createRight(fooTeamspace, operation);
        this.auth.addAllowance(fooReadRight, fooUser);
        this.auth.addAllowance(fooReadRight, fooTasksOnlyUser);
                
        RightEntity fooWikiRight = this.auth.createRight(fooTeamspaceWiki, operation);
        this.auth.addDenial(fooWikiRight, fooTasksOnlyUser);
        
        assertTrue(this.auth.mayPerform(fooTeamspace, operation, fooUser));
        assertTrue(this.auth.mayPerform(fooTeamspace, operation, fooTasksOnlyUser));
        
        assertTrue(this.auth.mayPerform(fooTeamspaceTasks, operation, fooUser));
        assertTrue(this.auth.mayPerform(fooTeamspaceTasks, operation, fooTasksOnlyUser));
        
        assertTrue(this.auth.mayPerform(fooTeamspaceWiki, operation, fooUser));
        assertFalse(this.auth.mayPerform(fooTeamspaceWiki, operation, fooTasksOnlyUser));
    }
    
    public void testGroupAllowances() {
        final String operation = "READ";
        final UserRO fooUser = this.createUser("fooUser");
        final GroupRO fooGroup = this.userAdmin.createGroup("fooGroup");
        this.userAdmin.addUser(fooUser, fooGroup);
        
        String fooTeamspace = "/teamspaces/foo-team";
        
        RightEntity fooReadRight = this.auth.createRight(fooTeamspace, operation);        
        this.auth.addAllowance(fooReadRight, fooGroup);
                
        assertTrue(this.auth.mayPerform(fooTeamspace, operation, fooUser));        
    }
    
    public void testProfileAllowances() {
        final String readOperation = "READ";
        final String writeOperation = "WRITE";
        
        final UserRO fooUser = this.createUser("fooUser");
        
        final String fooTeamspace = "/teamspaces/foo-team";
        
        final RightEntity fooReadRight = this.auth.createRight(
                fooTeamspace, readOperation);
        final RightEntity fooWriteRight = this.auth.createRight(
                fooTeamspace, writeOperation);
        
        final ProfileEntity fooRights = this.auth.createProfile("fooRights");
        this.auth.addRight(fooReadRight, fooRights);
        this.auth.addRight(fooWriteRight, fooRights);
        
        this.auth.addAllowance(fooRights, fooUser);                
        assertTrue(this.auth.mayPerform(fooTeamspace, readOperation, fooUser));
        assertTrue(this.auth.mayPerform(fooTeamspace, writeOperation, fooUser));
    }
}
