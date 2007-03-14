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

import com.mindquarry.persistence.jcr.api.JcrNode;
import com.mindquarry.persistence.jcr.model.EntityType;
import com.mindquarry.persistence.jcr.model.Property;

/**
 * Add summary documentation here.
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public class EntityTransformer implements Transformer {
    
    private EntityType entityType_;
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
        Object entity = entityType_.createNewEntity();        
        entityType_.getEntityId().setValue(entity, entityNode.getName());
        
        JcrNode contentNode = entityNode.getNode("jcr:content");        
        for (Property property : entityType_.properties()) {
            Object content = transformer(property).readFromJcr(contentNode);
            property.setContent(entity, content);
        }
        return entity;
    }
    
    private Transformer transformer(Property property) {
        return propertyTransformers_.get(property);
    }

    public JcrNode writeToJcr(Object entity, JcrNode folderNode) {
        
        String entityId = id(entity);
        
        JcrNode contentNode;
        if (folderNode.hasNode(entityId))
            contentNode = folderNode.getNode(entityId + "/jcr:content");
        else
            contentNode = createNewEntityNode(entityId, folderNode);     
        
        for (Property property : entityType_.properties()) {
            Object propertyValue = property.getContent(entity);
            transformer(property).writeToJcr(propertyValue, contentNode);
        }
        
        return folderNode.getNode(entityId);
    }
    
    private JcrNode createNewEntityNode(String entityId, JcrNode folderNode) {
        JcrNode entityNode = folderNode.addNode(entityId, "nt:file");
        entityNode.addMixin("mix:referenceable");
        
        JcrNode result = entityNode.addNode("jcr:content", "xt:document");
        result.setProperty("jcr:lastModified", System.currentTimeMillis());
        return result;
    }
    
    private String id(Object entity) {
        return entityType_.getEntityId().getValue(entity);
    }
}
