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

import static com.mindquarry.common.fp.FP.not;
import static com.mindquarry.common.fp.FP.select;
import static com.mindquarry.common.lang.StringUtil.concat;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mindquarry.common.fp.UnaryPredicate;
import com.mindquarry.persistence.jcr.annotations.Entity;
import com.mindquarry.persistence.jcr.annotations.Id;
import com.mindquarry.persistence.jcr.mapping.trafo.PropertyTransformer;

/**
 * Add summary documentation here.
 *
 * @author
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public class EntityType {

    private Class<?> clazz_;
    private EntityId entityId_;
    private Map<String, PropertyTransformer> propertyTransformers_;
    
    EntityType(Class<?> clazz) {
        clazz_ = clazz;
    }
    
    void initialize(Model model) {
        assert containsExactlyOneIdProperty() : 
            "each entity class must contain exactly one id annotated property";
        
        entityId_ = findIdProperty();
        
        List<Field> allNonIdFields = select(not(idAnnotated()), allFields());
        
        propertyTransformers_ = new HashMap<String, PropertyTransformer>();
        for (Field field : allNonIdFields) {
            
            Property property = new Property(field);
            
            PropertyTransformer propertyTransformer = 
                new PropertyTransformer(property);
            propertyTransformer.initialize();
            
            propertyTransformers_.put(property.getName(), propertyTransformer);
        }
    }
    
    public EntityId getEntityId() {
        return entityId_;
    }
    
    public PropertyTransformer propertyTransformer(String propertyName) {
        return propertyTransformers_.get(propertyName);
    }
    
    public Collection<PropertyTransformer> propertyTransformers() {
        return propertyTransformers_.values();
    }
    
    private boolean containsExactlyOneIdProperty() {
        return select(idAnnotated(), allFields()).size() == 1;
    }
    
    private EntityId findIdProperty() {
        List<Field> allIdFields = select(idAnnotated(), allFields());
        if (allIdFields.size() != 1) {
            throw new ModelException("each entity class must " +
                    "contain exactly one id annotated property");
        }
        
        Field idField = allIdFields.get(0);
        boolean isStringField = idField.getType().equals(String.class);
        boolean isPrimitveField = idField.getType().isPrimitive(); 
        
        if (!isStringField && !isPrimitveField) {
            throw new ModelException("the id field must be " +
                    "of a primitive type or of type String");
        }
        
        Property idProperty = new Property(idField);
        if (! idProperty.isReadable()) {
            throw new ModelException("the id field must be readable.");
        }
        
        return new EntityId(idProperty);
    }
    
    private List<Field> allFields() {
        return Arrays.asList(clazz_.getDeclaredFields());
    }
    
    private UnaryPredicate<Field> idAnnotated() {
        return new UnaryPredicate<Field>() {
            public boolean execute(Field field) {
                return field.isAnnotationPresent(Id.class);
            }
        };
    }
    
    String pathForEntity(Object entity) {
        return concat(folder(), "/", id(entity));
    }
    
    public String folder() {
        return clazz_.getAnnotation(Entity.class).folder();
    }
    
    String id(Object entity) {
        return entityId_.getValue(entity);
    }
}