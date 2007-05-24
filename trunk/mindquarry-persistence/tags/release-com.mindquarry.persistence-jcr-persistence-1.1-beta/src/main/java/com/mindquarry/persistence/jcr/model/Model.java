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
package com.mindquarry.persistence.jcr.model;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;


public class Model  {

    private Collection<Class<?>> clazzes_;
    private Collection<EntityType> entityTypes_;
    
    private Model(Collection<Class<?>> clazzes) {
        clazzes_ = clazzes;        
        entityTypes_ = new LinkedList<EntityType>();
    }
    
    private void initialize() {        
        for (Class<?> clazz : clazzes_) {
            EntityType entityType = new EntityType(clazz);
            entityType.initialize(this);
            
            entityTypes_.add(entityType);
        }
    }
    
    public static Model buildFromClazzes(Collection<Class<?>> entityClazzes) {        
        Model result = new Model(entityClazzes);
        result.initialize();
        return result;
    }
    
    public String jcrPathForEntity(Object entity) {
        EntityType entityType = entityType(entity);
        String clazzPath = entityType.pathForEntity(entity);
        return clazzPath;
    }
    
    public Collection<EntityType> allEntityTypes() {
        return entityTypes_;
    }
    
    public Collection<Class<?>> allEntityClasses() {
        List<Class<?>> result = new LinkedList<Class<?>>();
        for (EntityType entityType : allEntityTypes()) {
            result.add(entityType.entityClazz());
        }
        return result;
    }
    
    public boolean containsClass(Class<?> entityClazz) {
        return allEntityClasses().contains(entityClazz);
    }
    
    public Class<?> classForInterface(Class<?> interfaceClass) {
        Class<?> result = null;
        for (EntityType entityType : allEntityTypes()) {
            if (entityType.describesInterface(interfaceClass)) {
                result = entityType.entityClazz();
                break;
            }
        }
        return result;
    }
    
    public boolean containsInterface(Class<?> interfaceClass) {
        return classForInterface(interfaceClass) != null;
    }
    
    private EntityType entityType(Object entity) {
        EntityType result = null;
        for (EntityType entityType : allEntityTypes()) {
            if (entityType.describes(entity.getClass())) {
                result = entityType;
                break;
            }
        }
        return result;
    }
    
    public EntityType entityTypeForInterface(Class<?> interfaceClass) {
        EntityType result = null;
        for (EntityType entityType : allEntityTypes()) {
            if (entityType.describesInterface(interfaceClass)) {
                result = entityType;
                break;
            }
        }
        return result;
    }
    
    public EntityType findEntityType(Object entity) {
        EntityType result = entityType(entity);
        if (result == null) {
            throw new ModelException("could not find the class " + entity.getClass() +
                    " within the configured list of persistence entity classes");
        }
        return result;
    }
    
    public boolean isEntityClazz(Class<?> clazz) {
        return clazzes_.contains(clazz);
    }
}
