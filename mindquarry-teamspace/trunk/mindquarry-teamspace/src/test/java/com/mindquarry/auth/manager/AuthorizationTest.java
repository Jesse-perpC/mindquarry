/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
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
        UserRO user = this.createUser("testUser");
        String resource = "/teamspaces/foo-team";
        String operation = "READ";
        RightEntity right = this.auth.createRight(resource, operation);
        this.auth.addAllowance(right, user);
        assertTrue(this.auth.mayPerform(resource, operation, user));      
    }
    
    public void testResourceTree() {
        String operation = "READ";
        UserRO grantedUser = this.createUser("grantedUser");
        UserRO otherUser = this.createUser("otherUser");
        
        String higherLevelResource = "/teamspaces";
        String explicitGrantedResource = "/teamspaces/foo-team";
        String implicitGrantedResource = "/teamspaces/foo-team/wiki";
        
        RightEntity right = this.auth.createRight(explicitGrantedResource, operation);
        this.auth.addAllowance(right, grantedUser);
        
        assertTrue(this.auth.mayPerform(higherLevelResource, operation, grantedUser));
        assertTrue(this.auth.mayPerform(higherLevelResource, operation, otherUser));
        
        assertTrue(this.auth.mayPerform(explicitGrantedResource, operation, grantedUser));
        assertFalse(this.auth.mayPerform(explicitGrantedResource, operation, otherUser));
        
        assertTrue(this.auth.mayPerform(implicitGrantedResource, operation, grantedUser));
        assertFalse(this.auth.mayPerform(explicitGrantedResource, operation, otherUser));
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
