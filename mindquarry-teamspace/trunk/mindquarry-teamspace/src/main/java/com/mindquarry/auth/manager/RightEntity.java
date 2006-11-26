/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.auth.manager;

import java.util.HashSet;
import java.util.Set;

import com.mindquarry.user.AbstractUserRO;



/**
 * Add summary documentation here.
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public final class RightEntity extends AbstractRight {
    
    //private ResourceEntity resource;
    final String operation;
    
    // profiles that reference this right
    private final Set<Profile> profiles;
    
    public RightEntity() {
        this("", null, null);
    }
    
    public RightEntity(String id, ResourceEntity resource, String operation) {    
        super(id);
        this.operation = operation;
        this.profiles = new HashSet<Profile>();
    }
    
    boolean isAccessAllowed(AbstractUserRO user) {
        boolean result = super.isAccessAllowed(user);
        if (! result) {
            for (Profile profile : profiles) {
                result = profile.isAccessAllowed(user);
                if (result) break;
            }
        }
        return result;
    }
    
    boolean isAccessDenied(AbstractUserRO user) {
        boolean result = super.isAccessDenied(user);
        if (! result) {
            for (Profile profile : profiles) {
                result = profile.isAccessDenied(user);
                if (result) break;
            }
        }
        return result;
    }
    
    void addTo(Profile profile) {
        this.profiles.add(profile);
    }
    
    void removeFrom(Profile profile) {
        this.profiles.remove(profile);
    }
}
