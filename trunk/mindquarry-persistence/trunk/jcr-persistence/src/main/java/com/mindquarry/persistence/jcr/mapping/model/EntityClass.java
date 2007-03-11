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

import java.beans.IndexedPropertyDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;

import com.mindquarry.common.fp.UnaryPredicate;
import com.mindquarry.persistence.jcr.annotations.Entity;
import com.mindquarry.persistence.jcr.annotations.Id;

/**
 * Add summary documentation here.
 *
 * @author
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public class EntityClass {

    private Class<?> clazz_;
    private IdProperty idProperty_;
    private Map<String, Property> nonIdPropertyMap_;
    
    EntityClass(Class<?> clazz) {
        clazz_ = clazz;
    }
    
    void initialize() {
        assert containsExactlyOneIdProperty() : 
            "each entity class must contain exactly one id annotated property";
        
        idProperty_ = findIdProperty();
        
        List<Field> allNonIdFields = select(not(idAnnotated()), allFields());
        
        nonIdPropertyMap_ = new HashMap<String, Property>();
        for (Field field : allNonIdFields) {
            Property property = new Property(name(field));
            nonIdPropertyMap_.put(property.getName(), property);
        }
    }
    
    public IdProperty getIdProperty() {
        return idProperty_;
    }
    
    public Property getNonIdProperty(String name) {
        return nonIdPropertyMap_.get(name);
    }
    
    public Collection<Property> getNonIdProperties() {
        return nonIdPropertyMap_.values();
    }
    
    private boolean containsExactlyOneIdProperty() {
        return select(idAnnotated(), allFields()).size() == 1;
    }
    
    private IdProperty findIdProperty() {
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
        
        final String idPropertyName = name(idField);
        if (! isReadableProperty(idPropertyName)) {
            throw new ModelException("the id field must be readable.");
        }
        
        return new IdProperty(idPropertyName);
    }
    
    private boolean isReadableProperty(String propertyName) {
        return isReadableProperty(clazz_, propertyName);
    }
    
    private boolean isReadableProperty(Class<?> clazz, String name) {

        PropertyDescriptor descriptor = getPropertyDescriptor(clazz, name);
        if (descriptor != null) {
            Method readMethod = descriptor.getReadMethod();
            if ((readMethod == null) &&
                (descriptor instanceof IndexedPropertyDescriptor)) {
                readMethod = getIndexedReadMethod(descriptor);
            }
            return readMethod != null;
        } else {
            return false;
        }
    }
    
    private Method getIndexedReadMethod(PropertyDescriptor descriptor) {
        return ((IndexedPropertyDescriptor) descriptor).getIndexedReadMethod();
    }
    
    private PropertyDescriptor getPropertyDescriptor(
            Class<?> clazz, String propertyName) {
        
        PropertyDescriptor result = null;
        
        for (PropertyDescriptor descriptor : 
            PropertyUtils.getPropertyDescriptors(clazz)) {
            
            if (propertyName.equals(descriptor.getName())) {
                result = descriptor;
                break;
            }
        }
        return result;
    }
    
    private String name(Field field) {
        return field.getName();
    }
    
    private List<Field> allFields() {
        return Arrays.asList(clazz_.getFields());
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
    
    String folder() {
        return clazz_.getAnnotation(Entity.class).folder();
    }
    
    String id(Object entity) {
        return idProperty_.getValue(entity);
    }
}