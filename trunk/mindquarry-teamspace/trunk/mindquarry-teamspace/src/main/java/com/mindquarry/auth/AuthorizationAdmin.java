/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.auth;

import com.mindquarry.auth.manager.Profile;
import com.mindquarry.auth.manager.RightEntity;

/**
 * Add summary documentation here.
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public interface AuthorizationAdmin {
    
    // creates right with name "{operation}: {resource}";
    RightEntity createRight(String resource, String operation);
    
    RightEntity createRight(String name, String resource, String operation);
    
    
    Profile createProfile(String profileId);
    
    void addRight(RightEntity right, Profile profile);
    
    void removeRight(RightEntity right, Profile profile);
}

