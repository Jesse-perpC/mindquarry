/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.teamspace.auth;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Add summary documentation here.
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
final class Resource {

    private String name;
    List<Right> rights;
    private Map<String, Resource> children; 
    
    Resource(String name) {
        this.name = name;
        this.rights = new LinkedList<Right>();
        this.children = new HashMap<String, Resource>();
    }
    
    final boolean hasChildren() {
        return this.children.isEmpty();
    }
    
    final Right rightForOperation(String operation) {
        Right result = null;
        for (Right right : rights) {
            if (right.operation.equals(operation)) {
                result = right;
            }
        }
        return result;
    }
    
    final void addChild(Resource child) {
        children.put(child.name, child);
    }
    
    final void removeChild(Resource child) {
        children.remove(child.name);
    }
    
    final boolean hasChild(String name) {
        return this.children.containsKey(name);
    }
    
    final Resource getChild(String name) {
        return this.children.get(name);
    }
}
