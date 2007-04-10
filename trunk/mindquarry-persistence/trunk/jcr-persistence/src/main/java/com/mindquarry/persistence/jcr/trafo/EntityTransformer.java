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
package com.mindquarry.persistence.jcr.trafo;

import java.util.HashMap;
import java.util.Map;

import com.mindquarry.persistence.jcr.JcrNode;
import com.mindquarry.persistence.jcr.JcrSession;
import com.mindquarry.persistence.jcr.Pool;
import com.mindquarry.persistence.jcr.model.EntityType;
import com.mindquarry.persistence.jcr.model.Property;

/**
 * Add summary documentation here.
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public class EntityTransformer implements Transformer {
    
    protected EntityType entityType_;
    private Map<Property, Transformer> propertyTransformers_;
    
    public EntityTransformer(EntityType entityType) {
        entityType_ = entityType;
    }
    
    public void initialize(TransformerRegistry transformerRegistry) {
        propertyTransformers_ = new HashMap<Property, Transformer>();        
        for (Property property : entityType_.properties()) {
            Transformer transformer = new PropertyTransformer(property);
            transformer.initialize(transformerRegistry);
            propertyTransformers_.put(property, transformer);
        }
    }
    
    public Object readFromJcr(JcrNode entityNode) {
        Object entity;
        JcrSession session = entityNode.getSession();
        
        Pool pool = session.getPool();
        if (pool.containsEntryForNode(entityNode)) {
            entity = pool.entityByNode(entityNode);
        }
        else {
            // we fill the cache before we start to transform, so that we 
            // can use the cache if we have to re-write entity within the 
            // same "user write", i.e. within cyclic dependencies
            entity = createNewEntityObject(entityNode);            
            pool.put(entity, entityNode);
            readFromJcrInternal(entityNode, entity);
        }
        
        return entity;
    }
    
    private Object createNewEntityObject(JcrNode entityNode) {
        String entityId = entityNode.getName();
        return entityType_.createNewEntity(entityId);
    }
    
    // pay attention, this method is overwritten in CompositeEntityTransformer
    protected void readFromJcrInternal(JcrNode entityNode, Object entity) {
        JcrNode contentNode = entityNode.getNode("jcr:content");        
        for (Property property : entityType_.properties()) {
            Object content = transformer(property).readFromJcr(contentNode);
            property.setContent(entity, content);
        }
    }
    
    private Transformer transformer(Property property) {
        return propertyTransformers_.get(property);
    }

    public void writeToJcr(Object entity, JcrNode entityNode) {
        
        JcrSession session = entityNode.getSession();
        Pool pool = session.getPool();
        
        boolean isInitiator = session.getAttribute("WriteStarted") == null;
        if (isInitiator)
            session.setAttribute("WriteStarted", "true");
        
        if (pool.containsEntryForEntity(entity)) {
            entityNode = pool.nodeByEntity(entity);
            if (isInitiator)
                writeToJcrInternal(entity, entityNode);
        }
        else {            
            // we fill the cache before we start to transform, so that we 
            // can use the cache if we have to re-write entity within the
            // same "user write", i.e. within cyclic dependencies
            pool.put(entity, entityNode);            
            writeToJcrInternal(entity, entityNode);            
        }
        
        if (isInitiator)
            session.setAttribute("WriteStarted", null);
    }
    
    private void writeToJcrInternal(Object entity, JcrNode entityNode) {
        JcrNode contentNode;
        if (entityNode.hasNode("jcr:content"))
            contentNode = entityNode.getNode("jcr:content");
        else
            contentNode = entityNode.addNode("jcr:content", "xt:document");
        
        contentNode.setProperty("jcr:lastModified", System.currentTimeMillis());
        
        for (Property property : entityType_.properties()) {
            Object propertyValue = property.getContent(entity);
            transformer(property).writeToJcr(propertyValue, contentNode);
        }
    }
}
