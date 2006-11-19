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
class Resource {

    private String name;
    private Map<String, Resource> children; 
    
    Resource(String name) {
        this.name = name;
        this.children = new HashMap<String, Resource>();
    }
    
    boolean hasChildren() {
        return this.children.size() != 0;
    }
    
    void addChild(Resource child) {
        children.put(child.name, child);
    }
    
    void removeChild(Resource child) {
        children.remove(child.name);
    }
    
    Resource getChild(String name) {
        return this.children.get(name);
    }
}
