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

import com.mindquarry.persistence.jcr.Persistence;
import com.mindquarry.persistence.jcr.model.EntityType;
import com.mindquarry.persistence.jcr.model.Model;

/**
 * @author
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public class TransformationManager {
    
    private Model model_;
    private Persistence persistence_;
    private TransformerRegistry transformerRegistry_;
    
    private Map<EntityType, Transformer> entityTransformers_;
    
    public TransformationManager(Model model, Persistence persistence) {
        model_ = model;
        persistence_ = persistence;
        entityTransformers_ = new HashMap<EntityType, Transformer>();
    }
    
    public void initialize() {
        transformerRegistry_ = new TransformerRegistry(persistence_);
        for (EntityType entityType : getModel().allEntityTypes()) {
            
            Transformer transformer;
            if (entityType.asComposite())
                transformer = new CompositeEntityTransformer(entityType);
            else
                transformer = new EntityTransformer(entityType);
            
            transformer.initialize(transformerRegistry_);            
            entityTransformers_.put(entityType, transformer);
        }
    }
    
    public Transformer entityTransformer(Object entity) {
        return entityTransformers_.get(entityType(entity));
    }
    
    public Transformer entityTransformerByFolder(String folder) {
        EntityType entityType = entityTypeByFolder(folder);
        return entityTransformers_.get(entityType);
    }
    
    private EntityType entityType(Object entity) {
        return getModel().findEntityType(entity);
    }
    
    EntityType entityTypeByFolder(String folder) {
        EntityType result = null;
        for (EntityType entityType : getModel().allEntityTypes()) {
            if (entityType.usesJcrFolder(folder)) {
                result = entityType;
                break;
            }
        }
        return result;
    }
    
    Transformer createReferenceTransformer(Class<?> referenceesClazz) {
        Transformer result =
            new ReferenceTransformer(referenceesClazz, persistence_);
        result.initialize(transformerRegistry_);
        return result;
    }
    
    private Model getModel() {
        return model_;
    }
}
