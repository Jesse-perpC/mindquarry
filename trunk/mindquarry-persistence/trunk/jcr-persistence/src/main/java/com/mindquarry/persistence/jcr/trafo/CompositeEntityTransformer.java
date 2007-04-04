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

import com.mindquarry.persistence.jcr.JcrNode;
import com.mindquarry.persistence.jcr.model.EntityType;

/**
 * Add summary documentation here.
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public class CompositeEntityTransformer extends EntityTransformer {
        
    public CompositeEntityTransformer(EntityType entityType) {
        super(entityType);
    }
    
    public Object readFromJcr(JcrNode compositeNode) {
        
        String entityId = compositeNode.getName();
        String entityNodeName = entityId + ".xml";        
        
        Object entity = entityType_.createNewEntity(entityId);        
        JcrNode entityNode = compositeNode.getNode(entityNodeName);
        readContentFromJcr(entityNode, entity);
        return entity;
    }

    public void writeToJcr(Object entity, JcrNode compositeNode) {
        
        String entityNodeName = idForEntity(entity) + ".xml";
        
        JcrNode entityNode;        
        if (compositeNode.hasNode(entityNodeName))
            entityNode = compositeNode.getNode(entityNodeName);
        else
            entityNode = compositeNode.addNode(entityNodeName, "nt:file");
        
        super.writeToJcr(entity, entityNode);
    }
    
    private String idForEntity(Object entity) {
        return entityType_.getId(entity);
    }
}
