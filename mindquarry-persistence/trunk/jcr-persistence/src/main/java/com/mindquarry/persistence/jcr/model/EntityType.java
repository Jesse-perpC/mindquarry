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

import static com.mindquarry.common.fp.FP.not;
import static com.mindquarry.common.fp.FP.select;
import static com.mindquarry.common.lang.ReflectionUtil.hasPublicDefaultConstructor;
import static com.mindquarry.common.lang.StringUtil.concat;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mindquarry.common.fp.UnaryPredicate;
import com.mindquarry.persistence.api.Entity;
import com.mindquarry.persistence.api.Id;
import com.mindquarry.persistence.jcr.JcrPersistenceInternalException;

/**
 * @author
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public class EntityType {

    private Class<?> entityClazz_;
    private Collection<Class<?>> interfaces_;
    private EntityId entityId_;
    private Map<String, Property> properties_;
    
    EntityType(Class<?> entityClazz) {
        entityClazz_ = entityClazz;
    }
    
    void initialize(Model model) {
        assert containsExactlyOneIdProperty() : 
            "each entity class must contain exactly one id annotated property";
        
        assert hasPublicDefaultConstructor(entityClazz_) : 
            "each entity class must provide a public default constructor";
        
        List<Field> fields = allNonStaticFields();
        Field idField = select(idAnnotated(), fields).get(0);
        List<Field> nonIdFields = select(not(idAnnotated()), fields);
        
        entityId_ = makeEntityId(idField);        
        
        properties_ = new HashMap<String, Property>();
        for (Field field : nonIdFields) {            
            Property property = createProperty(field);   
            if (property.isAccessible()) {
                properties_.put(property.getName(), property);
            }
        }
        
        interfaces_ = allInterfaces(entityClazz_);
    }
    
    private Collection<Class<?>> allInterfaces(Class<?> clazz) {
        Collection<Class<?>> result = new ArrayList<Class<?>>();
        for (Class<?> interfaceClazz : clazz.getInterfaces()) {
            result.add(interfaceClazz);
            result.addAll(allInterfaces(interfaceClazz));
        }
        return result;
    }

    public Object createNewEntity(String entityId) {
        try {
            Object result = entityClazz_.newInstance();
            setId(result, entityId);
            return result;
        } catch (InstantiationException e) {
            throw new ModelException("could not create an instance of " +
                    "entity type: " + entityClazz_, e);
        } catch (IllegalAccessException e) {
            throw new JcrPersistenceInternalException(e);
        }
    }
    
    /**
     * creates an id for an entity instance that is unique
     * for the persistence model in the persistence environment, 
     * i.e. for all configured entity types in the jcr backend working on
     */
    public String peristenceUniqueId(Object entity) {
        return concat(parentFolder(), "_", getId(entity));
    }
    
    public String getId(Object entity) {
        return entityId_.getValue(entity);
    }
    
    public String getIdName() {
        return entityId_.getName();
    }
    
    private void setId(Object entity, String id) {
        entityId_.setValue(entity, id);
    }
    
    public Property property(String propertyName) {
        return properties_.get(propertyName);
    }
    
    public Collection<Property> properties() {
        return properties_.values();
    }
    
    private boolean containsExactlyOneIdProperty() {
        return select(idAnnotated(), allNonStaticFields()).size() == 1;
    }
    
    private EntityId makeEntityId(Field idField) {
        boolean isStringField = idField.getType().equals(String.class);        
        if (!isStringField) {
            throw new ModelException("the id field must be of type String");
        }
        
        Property idProperty = createProperty(idField);
        if (! idProperty.isAccessible()) {
            throw new ModelException("the id field must be accessible.");
        }
        
        return new EntityId(idProperty);
    }
    
    private Property createProperty(Field field) {
        if (Modifier.isTransient(field.getModifiers())) {
            return new TransientProperty(field);
        }
        else if (Modifier.isPublic(field.getModifiers())) {
            return new FieldAccessProperty(field);
        }
        else {
            return new DefaultProperty(field);
        }
    }
    
    private List<Field> allNonStaticFields() {
        List<Field> result = new ArrayList<Field>();
        
        Class clazz = entityClazz_;
        while (! Object.class.equals(clazz)) {
            for (Field field : clazz.getDeclaredFields()) {
                if (! Modifier.isStatic(field.getModifiers()))
                    result.add(field);
            }
            clazz = clazz.getSuperclass();
        }
        
        return result;
    }
    
    private UnaryPredicate<Field> idAnnotated() {
        return new UnaryPredicate<Field>() {
            public boolean execute(Field field) {
                return field.isAnnotationPresent(Id.class);
            }
        };
    }
    
    String pathForEntity(Object entity) {
        return concat(parentFolder(), "/", id(entity));
    }
    
    public String parentFolder() {
        return entityClazz_.getAnnotation(Entity.class).parentFolder();
    }
    
    public boolean asComposite() {
        return entityClazz_.getAnnotation(Entity.class).asComposite();
    }
    
    public Class<?> entityClazz() {
        return entityClazz_;
    }
    
    public boolean describes(Class<?> clazz) {
        return entityClazz_.equals(clazz);
    }
    
    public boolean describesInterface(Class<?> interfaceClazz) {
        boolean result = false;
        for (Class clazz : interfaces_) {
            if (clazz.equals(interfaceClazz))
                result = true;
        }
        return result;
    }
    
    public boolean usesJcrFolder(String folder) {
        return parentFolder().equals(folder);
    }
    
    String id(Object entity) {
        return entityId_.getValue(entity);
    }
    
    public String toString() {
        return "EntityType for entity class: " + entityClazz_.getSimpleName();
    }
}