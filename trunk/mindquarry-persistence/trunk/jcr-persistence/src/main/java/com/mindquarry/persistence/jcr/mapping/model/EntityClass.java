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

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

import com.mindquarry.persistence.jcr.annotations.Entity;
import com.mindquarry.persistence.jcr.annotations.Id;

/**
 * Add summary documentation here.
 *
 * @author
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
class EntityClass {

    private Class<?> clazz_;
    private String idPropertyName_;
    
    EntityClass(Class<?> clazz) {
        clazz_ = clazz;
    }
    
    void initialize() {
        for (Field field : clazz_.getFields()) {
            field.isAnnotationPresent(Id.class);
            
        }
    }
    
    String findIdPropertyName() {
        String result = getIdAnnotatedField();
        if (result == null)
            result = getIdAnnotatedField();
        return null;
    }
    
    String getIdAnnotatedField() {
        String result = null;
        for (Field field : clazz_.getFields()) {
            if (isIdAnnotated(field)) {
                result = field.getName();
                break;
            }
        }
        return result;
    }
    
    String getIdAnnotatedGetter() {
        String result = null;
        for (Method method : clazz_.getMethods()) {
            if (isGetter(method) && isIdAnnotated(method)) {
                result = method.getName();
                break;
            }
        }
        return result;
    }
    
    private boolean isIdAnnotated(AccessibleObject accessibleObject) {
        return accessibleObject.isAnnotationPresent(Id.class);
    }
    
    private boolean isGetter(Method method) {
        return method.getName().startsWith("get") 
            && method.getParameterTypes().length == 0;
    }
    
    String path() {
        return clazz_.getAnnotation(Entity.class).path();
    }
}
