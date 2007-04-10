/*
 * Copyright (C) 2006-2007 Mindquarry GmbH, All Rights Reserved
 * 
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 */
package com.mindquarry.auth.manager;

import static com.mindquarry.common.lang.StringUtil.concat;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.mindquarry.auth.AuthorizationAdmin;
import com.mindquarry.auth.AuthorizationCheck;
import com.mindquarry.auth.ProfileRO;
import com.mindquarry.auth.RightRO;
import com.mindquarry.user.AbstractUserRO;
import com.mindquarry.user.UserQuery;
import com.mindquarry.user.manager.UserManager;


/**
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public class Authorization implements AuthorizationCheck, 
    AuthorizationAdmin {

    private ResourceEntity resourcesRoot;
    
    private UserQuery userManager;
    
    public Authorization() {
        this.resourcesRoot = new ResourceEntity("/", "root");
        this.userManager = new UserManager();
    }
    
    public boolean mayPerform(
            String resourceUri, String operation, String userId) {
        
        AbstractUserRO user = this.userManager.userById(userId);
        return mayPerform(resourceUri, operation, user);
    }

    public boolean mayPerform(
            String resourceUri, String operation, AbstractUserRO user) {
        
        Iterator<String> pathItemsIt = pathItems(resourceUri).iterator();
        
        ResourceEntity resource = resourcesRoot;
        boolean result = true;
        boolean isEmptyRightList = true;
        
        while (pathItemsIt.hasNext()) {
            String pathItem = pathItemsIt.next();            
            if (! resource.hasChild(pathItem)) {
                break;
            }
            
            ResourceEntity child = resource.getChild(pathItem);
            if (isEmptyRightList && child.hasRights()) {
                isEmptyRightList = false;
                result = false;
            }
            
            RightEntity right = child.rightForOperation(operation);
            if (right != null) {
                if (right.isAccessAllowed(user)) {
                    result = true;
                }
                else if (result && right.isAccessDenied(user)) {
                    result = false;
                }
            }
            resource = child;
        }
        return result;
    }

    public RightEntity createRight(String resourceUri, String operation) {
        String rightName = concat(operation, ":", resourceUri);
        return this.createRight(rightName, resourceUri, operation);
    }

    public RightEntity createRight(
            String name, String resourceUri, String operation) {        
        
        ResourceEntity resource = navigateToResource(resourceUri);
        RightEntity result = new RightEntity(name, resource, operation);
        resource.addRight(result);
        return result;
    }
    
    private List<String> pathItems(String resourceUri) {
        String preparedUri = resourceUri.replaceFirst("/", "");
        return Arrays.asList(preparedUri.split("/"));
    }
    
    private ResourceEntity navigateToResource(String resourceUri) {
        Iterator<String> pathItemsIt = pathItems(resourceUri).iterator();        
        return navigateToResource(resourcesRoot, resourceUri, pathItemsIt);
    }
    
    private ResourceEntity navigateToResource(ResourceEntity parent, 
            String resourceUri, Iterator<String> pathItemsIt) {
        
        ResourceEntity result;
        if (! pathItemsIt.hasNext()) { 
            result = parent;
        }
        else {
            String pathItem = pathItemsIt.next();
            ResourceEntity child = parent.getChild(pathItem);
            if (child == null) {
                child = new ResourceEntity(resourceUri, pathItem);
                parent.addChild(child);
            }
            result = navigateToResource(child, resourceUri, pathItemsIt);
        }
        return result;
    }


    public ProfileEntity createProfile(String profileId) {
        return new ProfileEntity(profileId);
    }
    
    public void addRight(RightRO right, ProfileRO profile) {
        RightEntity rightEntity = (RightEntity) right;
        ProfileEntity profileEntity = (ProfileEntity) profile;        
        rightEntity.addTo(profileEntity);
        profileEntity.add(rightEntity);
    }

    public void removeRight(RightRO right, ProfileRO profile) {
        RightEntity rightEntity = (RightEntity) right;
        ProfileEntity profileEntity = (ProfileEntity) profile;
        rightEntity.removeFrom(profileEntity);
        profileEntity.remove(rightEntity);
    }

    public void addAllowance(RightRO right, AbstractUserRO user) {
        addAllowance((AbstractRight) right, user);
    }

    public void addAllowance(ProfileRO profile, AbstractUserRO user) {
        addAllowance((AbstractRight) profile, user);        
    }

    public void addDenial(RightRO right, AbstractUserRO user) {
        addDenial((AbstractRight) right, user);
    }

    public void addDenial(ProfileRO profile, AbstractUserRO user) {
        addDenial((AbstractRight) profile, user);
    }

    public void removeAllowance(RightRO right, AbstractUserRO user) {
        removeAllowance((AbstractRight) right, user);
    }

    public void removeAllowance(ProfileRO profile, AbstractUserRO user) {
        removeAllowance((AbstractRight) profile, user);
    }

    public void removeDenial(RightRO right, AbstractUserRO user) {
        removeDenial((AbstractRight) right, user);
    }

    public void removeDenial(ProfileRO profile, AbstractUserRO user) {
        removeDenial((AbstractRight) profile, user);        
    }

    private void addAllowance(AbstractRight right, AbstractUserRO user) {
        right.allowAccessTo(user);
    }

    private void removeAllowance(AbstractRight right, AbstractUserRO user) {
        right.removeAllowanceFor(user);
    }

    private void addDenial(AbstractRight right, AbstractUserRO user) {
        right.denyAccessTo(user);
    }

    private void removeDenial(AbstractRight right, AbstractUserRO user) {
        right.removeDenialFor(user);
    }
}