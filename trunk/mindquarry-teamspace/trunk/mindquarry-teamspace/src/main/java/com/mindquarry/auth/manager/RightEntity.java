/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.auth.manager;

import java.util.HashSet;
import java.util.Set;

import com.mindquarry.auth.RightRO;
import com.mindquarry.user.AbstractUserRO;



/**
 * Add summary documentation here.
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public final class RightEntity extends AbstractRight implements RightRO {
    
    private String operation;
    
    private ResourceEntity resource;
    
    // profiles that reference this right
    private Set<ProfileEntity> profiles;
    
    public RightEntity() {
        this("", null, null);
    }
    
    public RightEntity(String id, ResourceEntity resource, String operation) {    
        super(id);
        this.resource = resource;
        this.operation = operation;
        this.profiles = new HashSet<ProfileEntity>();
    }

    public String getOperation() {
        return operation;
    }  
    
    public void setOperation(String operation) {
        this.operation = operation;
    }    
    
    public ResourceEntity getResource() {
        return resource;
    }

    public void setResource(ResourceEntity resource) {
        this.resource = resource;
    }

    boolean isAccessAllowed(AbstractUserRO user) {
        boolean result = super.isAccessAllowed(user);
        if (! result) {
            for (ProfileEntity profile : profiles) {
                result = profile.isAccessAllowed(user);
                if (result) break;
            }
        }
        return result;
    }
    
    boolean isAccessDenied(AbstractUserRO user) {
        boolean result = super.isAccessDenied(user);
        if (! result) {
            for (ProfileEntity profile : profiles) {
                result = profile.isAccessDenied(user);
                if (result) break;
            }
        }
        return result;
    }
    
    void addTo(ProfileEntity profile) {
        this.profiles.add(profile);
    }
    
    void removeFrom(ProfileEntity profile) {
        this.profiles.remove(profile);
    }
}
