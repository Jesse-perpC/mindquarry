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

import java.util.HashMap;
import java.util.Map;

import com.mindquarry.auth.ResourceRO;
import com.mindquarry.persistence.api.Entity;
import com.mindquarry.persistence.api.Id;
import com.mindquarry.persistence.api.NamedQuery;

/**
 * Add summary documentation here.
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
@Entity(parentFolder="resources")
@NamedQuery(name="getResourceById", query="/resources/{$id}")
public final class ResourceEntity implements ResourceRO {

    @Id public String id;
    public String name;
    public RightSet rights;
    public Map<String, ResourceEntity> children;
    
    public ResourceEntity() { }
    
    public ResourceEntity(String id, String name) {
        this.id = id;
        this.name = name;
        this.rights = new RightSet();
        this.children = new HashMap<String, ResourceEntity>();
    }

    boolean hasChildren() {
        return ! this.children.isEmpty();
    }
    
    void addRight(RightEntity right) {
        rights.add(right);
    }
    
    void removeRight(AbstractRight right) {
        rights.remove(right);
    }
    
    boolean hasRights() {
        return ! this.rights.isEmpty();
    }
    
    RightEntity rightForOperation(String operation) {
        return rights.rightForOperation(operation);
    }
    
    ResourceEntity addChild(String name) {
        ResourceEntity child = new ResourceEntity(id + name, name);
        children.put(name, child);
        return child;
    }
    
    void removeChild(ResourceEntity child) {
        children.remove(child.name);
    }
    
    boolean hasChild(String name) {
        return this.children.containsKey(name);
    }
    
    ResourceEntity getChild(String name) {
        return this.children.get(name);
    }
}
