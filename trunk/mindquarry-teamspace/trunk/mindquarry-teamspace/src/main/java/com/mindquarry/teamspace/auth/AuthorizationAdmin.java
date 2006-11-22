/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.teamspace.auth;

/**
 * Add summary documentation here.
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public interface AuthorizationAdmin {
    
    // creates right with name "{operation}: {resource}";
    Right createRight(String resource, String operation);
    
    Right createRight(String name, String resource, String operation);
    
    
    Profile createProfile(String profileId);
    
    void addRight(Right right, Profile profile);
    
    void removeRight(Right right, Profile profile);
}

