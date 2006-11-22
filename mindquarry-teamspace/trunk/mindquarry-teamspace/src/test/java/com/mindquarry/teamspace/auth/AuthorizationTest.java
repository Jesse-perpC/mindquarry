/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.teamspace.auth;

import junit.framework.TestCase;

/**
 * Add summary documentation here.
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public class AuthorizationTest extends TestCase {

    private Authorization auth;
    
    protected void setUp() throws Exception {
        super.setUp();
        this.auth = new Authorization();
    }
    
    public void testResource() {
        User user = this.auth.createUser("testUser");
        String resource = "/teamspaces/foo-team";
        String operation = "READ";
        Right right = this.auth.createRight(resource, operation);
        this.auth.addAllowance(right, user);
        assertTrue(this.auth.mayPerform(resource, operation, user));      
    }
    
    public void testResourceTree() {
        String operation = "READ";
        User grantedUser = this.auth.createUser("grantedUser");
        User otherUser = this.auth.createUser("otherUser");
        
        String higherLevelResource = "/teamspaces";
        String explicitGrantedResource = "/teamspaces/foo-team";
        String implicitGrantedResource = "/teamspaces/foo-team/wiki";
        
        Right right = this.auth.createRight(explicitGrantedResource, operation);
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
        User fooUser = this.auth.createUser("fooUser");
        User fooTasksOnlyUser = this.auth.createUser("fooTasksUser");
        
        String fooTeamspace = "/teamspaces/foo-team";
        String fooTeamspaceWiki = "/teamspaces/foo-team/wiki";
        String fooTeamspaceTasks = "/teamspaces/foo-team/tasks";
        
        Right fooReadRight = this.auth.createRight(fooTeamspace, operation);
        this.auth.addAllowance(fooReadRight, fooUser);
        this.auth.addAllowance(fooReadRight, fooTasksOnlyUser);
                
        Right fooWikiRight = this.auth.createRight(fooTeamspaceWiki, operation);
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
        final User fooUser = this.auth.createUser("fooUser");
        final Group fooGroup = this.auth.createGroup("fooGroup");
        this.auth.addUser(fooUser, fooGroup);
        
        String fooTeamspace = "/teamspaces/foo-team";
        
        Right fooReadRight = this.auth.createRight(fooTeamspace, operation);        
        this.auth.addAllowance(fooReadRight, fooGroup);
                
        assertTrue(this.auth.mayPerform(fooTeamspace, operation, fooUser));        
    }
    
    public void testProfileAllowances() {
        final String readOperation = "READ";
        final String writeOperation = "WRITE";
        
        final User fooUser = this.auth.createUser("fooUser");
        
        final String fooTeamspace = "/teamspaces/foo-team";
        
        final Right fooReadRight = this.auth.createRight(
                fooTeamspace, readOperation);
        final Right fooWriteRight = this.auth.createRight(
                fooTeamspace, writeOperation);
        
        final Profile fooRights = this.auth.createProfile("fooRights");
        this.auth.addRight(fooReadRight, fooRights);
        this.auth.addRight(fooWriteRight, fooRights);
        
        this.auth.addAllowance(fooRights, fooUser);                
        assertTrue(this.auth.mayPerform(fooTeamspace, readOperation, fooUser));
        assertTrue(this.auth.mayPerform(fooTeamspace, writeOperation, fooUser));
    }
}
