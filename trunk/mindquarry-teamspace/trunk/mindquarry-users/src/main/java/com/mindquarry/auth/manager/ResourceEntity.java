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
import static com.mindquarry.auth.manager.ResourceUtil.pathItems;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    public Set<ActionEntity> actions;
    public ResourceEntity parent;
    public Map<String, ResourceEntity> children;
    
    public ResourceEntity() { }
    
    ResourceEntity(String id, String name, ResourceEntity parent) {
        this.id = id;
        this.name = name;
        this.actions = new HashSet<ActionEntity>();
        this.parent = parent;
        this.children = new HashMap<String, ResourceEntity>();
    }

    Collection<ActionEntity> getActions() {
        return actions;
    }

    ResourceRO getParent() {
        return parent;
    }

    boolean hasChildren() {
        return ! this.children.isEmpty();
    }

    Collection<ResourceEntity> getChildren() {
        return children.values();
    }

    Collection<ResourceEntity> getChildrenDeep() {
        Collection<ResourceEntity> result = new ArrayList<ResourceEntity>();
        addChildrenDeep(result);
        return result;
    }

    private void addChildrenDeep(Collection<ResourceEntity> allChildren) {
        allChildren.addAll(getChildren());
        for (ResourceEntity child : getChildren()) {
            child.addChildrenDeep(allChildren);
        }
    }
    
    boolean hasChild(String name) {
        return this.children.containsKey(name);
    }
    
    ResourceEntity getChild(String name) {
        return this.children.get(name);
    }
    
    ResourceEntity addChild(String name) {
        String childId = concat(id + "_" + name);
        ResourceEntity child = new ResourceEntity(childId, name, this);
        children.put(name, child);
        return child;
    }
    
    void removeChild(ResourceEntity child) {
        children.remove(child.name);
    }
    
    void addAction(ActionEntity action) {
        actions.add(action);
    }
    
    void removeRight(ActionEntity action) {
        actions.remove(action);
    }
    
    boolean hasActions() {
        return ! this.actions.isEmpty();
    }
    
    ActionEntity actionForOperation(String operation) {
        ActionEntity result = null;
        for (ActionEntity right : actions) {
            if (right.getOperation().equals(operation)) {
                result = right;
                break;
            }
        }
        return result;
    }
    
    boolean isObsolete() {
        return ! hasChildren() && ! hasActions();
    }
    
    Iterator<ResourceEntity> iteratePath(String path) {
        List<ResourceEntity> resources = new ArrayList<ResourceEntity>(5);
        resources.add(this);
        
        ResourceEntity parent = this;        
        Iterator<String> pathItemsIt = pathItems(path).iterator();
        while (pathItemsIt.hasNext()) {            
            String pathItem = pathItemsIt.next();            
            if (parent.hasChild(pathItem)) {
                parent = parent.getChild(pathItem);
                resources.add(parent);
            }
            else {
                break;
            }
        }
        return resources.iterator();
    }
}
