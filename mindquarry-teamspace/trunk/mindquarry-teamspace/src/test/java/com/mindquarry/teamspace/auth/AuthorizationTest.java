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
    private String userId = "testUser";
    
    protected void setUp() throws Exception {
        super.setUp();
        this.auth = new Authorization();
    }
    
    public void testResource() {
//        String resource = "/teamspaces/foo-team";
//        String operation = "READ";
//        Right right = this.auth.createRight(resource, operation);
//        this.auth.grant(right, userId);
//        assertTrue(this.auth.mayPerform(resource, operation, userId));
    }
    
    public void testResourceTree() {
//        String operation = "READ";
//        
//        String explicitGrantedResource = "/teamspaces/foo-team";
//        String implicitGrantedResource = "/teamspaces/foo-team/wiki";
//        
//        Right right = this.auth.createRight(explicitGrantedResource, operation);
//        this.auth.grant(right, userId);
//        
//        assertTrue(this.auth.mayPerform(implicitGrantedResource, operation, userId));
    }
}
