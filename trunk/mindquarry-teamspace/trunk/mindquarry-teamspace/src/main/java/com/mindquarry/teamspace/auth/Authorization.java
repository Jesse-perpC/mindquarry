/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.teamspace.auth;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Add summary documentation here.
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public class Authorization implements AuthorizationCheck, 
    AuthorizationAdmin, UserAdmin {

    private Map<String, User> usersMap;
    private Map<String, Group> groupsMap;
    private Resource resourcesRoot;
    
    public Authorization() {
        this.usersMap = new HashMap<String, User>();
        this.groupsMap = new HashMap<String, Group>();
        this.resourcesRoot = new Resource("root");
    }
    
    public boolean mayPerform(
            String resourceUri, String operation, String userId) {
        
        AbstractUser user = userById(userId);
        return this.mayPerform(resourceUri, operation, user);
    }

    public boolean mayPerform(
            String resourceUri, String operation, AbstractUser user) {
        
        Iterator<String> pathItemsIt = pathItemsFromUri(resourceUri).iterator();
        
        Resource resource = resourcesRoot;
        boolean result = true;
        boolean isEmptyRightList = true;
        
        while (pathItemsIt.hasNext()) {
            String pathItem = pathItemsIt.next();            
            if (! resource.hasChild(pathItem)) {
                break;
            }
            
            Resource child = resource.getChild(pathItem);
            if (isEmptyRightList && child.hasRights()) {
                isEmptyRightList = false;
                result = false;
            }
            
            Right right = child.rightForOperation(operation);
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

    public Right createRight(String resourceUri, String operation) {
        StringBuilder nameSB = new StringBuilder();
        nameSB.append(operation).append(": ").append(resourceUri);
        return this.createRight(nameSB.toString(), resourceUri, operation);
    }

    public Right createRight(
            String name, String resourceUri, String operation) {        
        
        Resource resource = navigateToResource(resourceUri);
        Right result = new Right(name, resource, operation);
        resource.addRight(result);
        return result;
    }

    public void addAllowance(AbstractRight right, AbstractUser user) {
        right.allowAccessTo(user);
    }

    public void removeAllowance(AbstractRight right, AbstractUser user) {
        right.removeAllowanceFor(user);
    }

    public void addDenial(AbstractRight right, AbstractUser user) {
        right.denyAccessTo(user);
    }

    public void removeDenial(AbstractRight right, AbstractUser user) {
        right.removeDenialFor(user);
    }
    
    private List<String> pathItemsFromUri(String resourceUri) {
        String[] pathItems = resourceUri.replaceFirst("/", "").split("/");
        return Arrays.asList(pathItems);
    }
    
    private Resource navigateToResource(String resourceUri) {
        Iterator<String> pathItemsIt = pathItemsFromUri(resourceUri).iterator();        
        return navigateToResource(resourcesRoot, pathItemsIt);
    }
    
    private Resource navigateToResource(Resource parent, Iterator<String> pathItemsIt) {
        Resource result;
        if (! pathItemsIt.hasNext()) { 
            result = parent;
        }
        else {
            String pathItem = pathItemsIt.next();
            Resource child = parent.getChild(pathItem);
            if (child == null) {
                child = new Resource(pathItem);
                parent.addChild(child);
            }
            result = navigateToResource(child, pathItemsIt);
        }
        return result;
    }

    public Group createGroup(String groupId) {
        Group result = new Group(groupId);
        groupsMap.put(groupId, result);
        return result;
    }

    public Group groupById(String groupId) {
        return groupsMap.get(groupId);
    }

    public void deleteGroup(Group group) {
        groupsMap.remove(group.id);
    }

    public User createUser(String userId) {
        User result = new User(userId);
        usersMap.put(userId, result);
        return result;
    }

    public User userById(String userId) {
        return usersMap.get(userId);
    }

    public void deleteUser(User user) {
        usersMap.remove(user.id);
    }

    public void addUser(AbstractUser user, Group group) {
        group.add(user);
        
    }

    public void removeUser(AbstractUser user, Group group) {
       group.remove(user);
    }


    public Profile createProfile(String profileId) {
        return new Profile(profileId);
    }
    
    public void addRight(Right right, Profile profile) {
        right.addTo(profile);
        profile.add(right);
    }

    public void removeRight(Right right, Profile profile) {
        right.removeFrom(profile);
        profile.remove(right);
    }
}