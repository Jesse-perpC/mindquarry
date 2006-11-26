/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.auth;


/**
 * Add summary documentation here.
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public interface AuthorizationAdmin {
    
    // creates right with name "{operation}: {resource}";
    RightRO createRight(String resource, String operation);
    
    RightRO createRight(String name, String resource, String operation);
    
    
    ProfileRO createProfile(String profileId);
    
    void addRight(RightRO right, ProfileRO profile);
    
    void removeRight(RightRO right, ProfileRO profile);
}

