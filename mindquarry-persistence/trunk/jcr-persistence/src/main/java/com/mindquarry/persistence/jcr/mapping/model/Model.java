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
package com.mindquarry.persistence.jcr.mapping.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mindquarry.persistence.jcr.JcrPersistenceInternalException;
import com.mindquarry.persistence.jcr.mapping.trafo.CollectionTransformer;
import com.mindquarry.persistence.jcr.mapping.trafo.EntityTransformer;
import com.mindquarry.persistence.jcr.mapping.trafo.StringTransformer;
import com.mindquarry.persistence.jcr.mapping.trafo.Transformer;


public class Model  {

    private List<Class<?>> clazzes_;
    private Map<Class<?>, EntityType> entityTypes_;
    private Map<Class<?>, EntityTransformer> entityTransformers_;
    
    private Model(List<Class<?>> clazzes) {
        clazzes_ = clazzes;
        
        entityTypes_ = new HashMap<Class<?>, EntityType>();
        entityTransformers_ = new HashMap<Class<?>, EntityTransformer>();
    }
    
    private void initialize() {
        for (Class<?> clazz : clazzes_) {
            EntityType entityType = new EntityType(clazz);
            entityType.initialize(this);
            entityTypes_.put(clazz, entityType);
            
            entityTransformers_.put(clazz, new EntityTransformer(entityType));
        }      
    }
    
    public static Model buildFromClazzes(
            List<Class<?>> entityClazzes) {
        
        Model result = new Model(entityClazzes);
        result.initialize();
        return result;
    }
    
    public String entityFolderName(Object entity) {
        return entityTypes_.get(entity.getClass()).folder();
    }
    
    public EntityTransformer entityTransformer(Object entity) {
        return entityTransformers_.get(entity.getClass());
    }
    
    public String jcrPathForEntity(Object entity) {
        EntityType entityClazz = entityTypes_.get(entity.getClass());
        String clazzPath = entityClazz.pathForEntity(entity);
        return clazzPath;
    }
    
    public EntityType entityType(Object entity) {
        return entityTypes_.get(entity.getClass());
    }
}
