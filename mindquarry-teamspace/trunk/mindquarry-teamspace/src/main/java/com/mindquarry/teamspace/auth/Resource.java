/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.teamspace.auth;

import java.util.HashMap;
import java.util.Map;

/**
 * Add summary documentation here.
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
final class Resource {

    private String name;
    private final RightSet rights;
    private Map<String, Resource> children; 
    
    Resource(String name) {
        this.name = name;
        this.rights = new RightSet();
        this.children = new HashMap<String, Resource>();
    }
    
    final boolean hasChildren() {
        return ! this.children.isEmpty();
    }
    
    final void addRight(Right right) {
        rights.add(right);
    }
    
    final void removeRight(AbstractRight right) {
        rights.remove(right);
    }
    
    final boolean hasRights() {
        return ! this.rights.isEmpty();
    }
    
    final Right rightForOperation(String operation) {
        return rights.rightForOperation(operation);
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
