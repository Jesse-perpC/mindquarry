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
package com.mindquarry.persistence.jcr;

import static com.mindquarry.common.lang.StringUtil.concat;

import java.util.HashMap;
import java.util.Map;

import com.mindquarry.persistence.jcr.model.EntityType;
import com.mindquarry.persistence.jcr.model.Model;

/**
 * Add summary documentation here.
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public class Pool {
        
    private Model model_;
    private Map<String, Entry> store_;
    
    Pool(Persistence persistence) {
        model_ = persistence.getModel();
        store_ = new HashMap<String, Entry>();
    }
    
    void clear() {
        store_.clear();
    }
    
    public void allocate(Object entity) {
        entryByEntity(entity).inUse = true;
    }
    
    public boolean isAllocated(Object entity) {
        return entryByEntity(entity).inUse;
    }
    
    public void release(Object entity) {
        entryByEntity(entity).inUse = false;
    }
    
    public boolean isReleased(Object entity) {
        return ! entryByEntity(entity).inUse;
    }
    
    public JcrNode nodeByEntity(Object entity) {
        return entryByEntity(entity).entityNode;
    }
    
    public Object entityByNode(JcrNode node) {
        return entryByNode(node).entity;
    }
    
    public Object put(Object entity, JcrNode jcrNode) {
        String key = keyFromEntity(entity);
        Entry entry = new Entry(entity, jcrNode);
        return store_.put(key, entry);
    }
    
    public boolean containsEntryForEntity(Object entity) {
        return entryByEntity(entity) != null;
    }
    
    public boolean containsEntryForNode(JcrNode node) {
        return entryByNode(node) != null;
    }
    
    private Entry entryByNode(JcrNode node) {
        return entryByKey(keyFromNode(node));
    }
    
    private Entry entryByEntity(Object entity) {
        return entryByKey(keyFromEntity(entity));
    }
    
    private Entry entryByKey(String key) {
        return store_.get(key);
    }
    
    private String keyFromEntity(Object entity) {
        String folderName = entityType(entity).parentFolder();        
        return buildKey(folderName, entityType(entity).getId(entity));
    }
    
    private EntityType entityType(Object entity) {
        return model_.findEntityType(entity);
    }
    
    private String keyFromNode(JcrNode jcrEntityNode) {
        JcrNode entityFolder = jcrEntityNode.getParent();
        return buildKey(entityFolder.getName(), jcrEntityNode.getName());
    }
    
    private String buildKey(String folderName, String entityId) {
        return concat(folderName, ":", entityId);
    }
    
    private static class Entry {

        Object entity;        
        JcrNode entityNode;
        boolean inUse;
        
        Entry(Object entity, JcrNode entityNode) {
            this.inUse = false;
            this.entity = entity;
            this.entityNode = entityNode;
        }
    }
}
