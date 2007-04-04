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
        String entityId = entityNode.getName();
        Object entity = entityType_.createNewEntity(entityId);
        readContentFromJcr(entityNode, entity);
        return entity;
    }
    
    protected void readContentFromJcr(JcrNode entityNode, Object entity) {
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
