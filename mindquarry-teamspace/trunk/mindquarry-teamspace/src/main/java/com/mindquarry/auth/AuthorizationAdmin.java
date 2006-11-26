/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.auth;

import com.mindquarry.user.AbstractUserRO;


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
    
    
    void addAllowance(RightRO right, AbstractUserRO user);
    void removeAllowance(RightRO right, AbstractUserRO user);

    void addAllowance(ProfileRO profile, AbstractUserRO user);
    void removeAllowance(ProfileRO profile, AbstractUserRO user);
    
    void addDenial(RightRO right, AbstractUserRO user);
    void removeDenial(RightRO right, AbstractUserRO user);
    
    void addDenial(ProfileRO profile, AbstractUserRO user);
    void removeDenial(ProfileRO profile, AbstractUserRO user);
}

