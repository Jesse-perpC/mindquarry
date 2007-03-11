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

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Model  {

    private Map<Class<?>, EntityClass> entityClazzes_;
    
    private Model(List<Class<?>> clazzes) {
        entityClazzes_ = new HashMap<Class<?>, EntityClass>();
        for (Class<?> clazz : clazzes) {
            entityClazzes_.put(clazz, new EntityClass(clazz));
        }
    }
    
    private void initialize() {
        
    }
    
    public static Model buildFromClazzes(
            List<Class<?>> entityClazzes) {
        
        Model result = new Model(entityClazzes);
        result.initialize();
        return result;
    }
    
    public String jcrPathForEntity(Object entity) {
        EntityClass entityClazz = entityClazzes_.get(entity.getClass());
        String clazzPath = entityClazz.pathForEntity(entity);
        return clazzPath;
    }
    
    public EntityClass entityClass(Object entity) {
        return entityClazzes_.get(entity.getClass());
    }
}
