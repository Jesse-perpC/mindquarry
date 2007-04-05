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
    
    public JcrNode nodeByEntity(Object entity) {
        String key = keyFromEntity(entity);
        return store_.get(key).entityNode;
    }
    
    public Object entityByNode(JcrNode entityNode) {
        String key = keyFromNode(entityNode);
        return store_.get(key).entity;
    }
    
    public Object put(Object entity, JcrNode jcrNode) {
        String key = keyFromEntity(entity);
        Entry entry = new Entry(entity, jcrNode);
        return store_.put(key, entry);
    }
    
    public boolean containsEntryForEntity(Object entity) {
        return store_.containsKey(keyFromEntity(entity));
    }
    
    public boolean containsEntryForNode(JcrNode node) {
        return store_.containsKey(keyFromNode(node));
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
        
        Entry(Object entity, JcrNode entityNode) {
            this.entity = entity;
            this.entityNode = entityNode;
        }
    }
}
