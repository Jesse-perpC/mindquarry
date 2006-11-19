/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.teamspace.auth;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
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

    private Resource resourcesRoot;
    private List<Right> rights = new LinkedList<Right>();
    private Map<String, List<Right>> userRightsMap = new HashMap<String, List<Right>>(); 
    
    public Authorization() {
        this.resourcesRoot = new Resource("root");
    }
    
    public boolean mayPerform(
            String resourceUri, String operation, String userId) {
        
        Resource resource = navigateToResource(resourceUri);
        
        List<Right> userRights = userRightsMap.get(userId);
        if (userRights != null) {
            for (Right right : userRights) {
                if (right.resource.equals(resourceUri))
                    return true;
            }
        }
        return false;
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
        rights.add(result);        
        return result;
    }

    public void grant(Right right, String userId) {
        List<Right> userRights = new LinkedList<Right>();
        userRights.add(right);
        userRightsMap.put(userId, userRights);
    }
    
    private Resource navigateToResource(String resourceUri) {
        String[] pathItems = resourceUri.replaceFirst("/", "").split("/");
        Iterator<String> pathItemsIt = Arrays.asList(pathItems).iterator();
        
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
}
