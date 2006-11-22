/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.teamspace.auth;

import java.util.HashSet;
import java.util.Set;



/**
 * Add summary documentation here.
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
final class Right extends AbstractRight {
    
    final Resource resource;
    final String operation;
    
    // profiles that reference this right
    private final Set<Profile> profiles;
    
    /**
     * @param name
     */
    Right(String id, Resource resource, String operation) {    
        super(id);
        this.resource = resource;
        this.operation = operation;
        this.profiles = new HashSet<Profile>();
    }
    
    boolean isAccessAllowed(AbstractUser user) {
        boolean result = super.isAccessAllowed(user);
        if (! result) {
            for (Profile profile : profiles) {
                result = profile.isAccessAllowed(user);
                if (result) break;
            }
        }
        return result;
    }
    
    boolean isAccessDenied(AbstractUser user) {
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
