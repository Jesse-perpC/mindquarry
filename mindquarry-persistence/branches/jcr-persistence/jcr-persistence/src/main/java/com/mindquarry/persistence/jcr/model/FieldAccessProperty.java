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

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Arrays;

import com.mindquarry.persistence.jcr.JcrPersistenceInternalException;

/**
 * Add summary documentation here.
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public final class FieldAccessProperty implements Property {

    private Field field_;
    
    public FieldAccessProperty(Field field) {
        field_ = field;
    }
    
    public String getName() {
        return field_.getName();
    }
    
    public Type getContentType() {
        return field_.getGenericType();
    }
    
    private Class<?> getContentClass() {
        return field_.getClass();
    }
    
    private Class<?> getDeclaringClass() {
        return field_.getDeclaringClass();
    }
    
    public Object getContent(Object bean) {
        try {
            return field_.get(bean);
        } catch (IllegalAccessException e) {
            throw new JcrPersistenceInternalException(e);
        }
    }
    
    public Iterable getIterableContent(Object bean) {
        Object content = getContent(bean);
        
        field_.getGenericType();
        if (getContentClass().isArray()) {
            return Arrays.asList((Object[]) content);
        }
        else {
            return (Iterable) content;
        }
    }
    
    public void setContent(Object bean, Object value) {
        try {
            field_.set(bean, value);
        } catch (IllegalAccessException e) {
            throw new JcrPersistenceInternalException(e);
        }
    }
    
    public boolean isAccessible() {
        return true;
    }
    
    public String toString() {
        return "property '" + getName() + 
               "' of class: " + getDeclaringClass().getSimpleName();
    }
}
