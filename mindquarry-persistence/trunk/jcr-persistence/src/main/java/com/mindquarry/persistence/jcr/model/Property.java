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

import java.beans.IndexedPropertyDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;

import org.apache.commons.beanutils.PropertyUtils;

import com.mindquarry.persistence.jcr.JcrPersistenceInternalException;

/**
 * Add summary documentation here.
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public final class Property {

    private Field field_;
    
    public Property(Field field) {
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
            return PropertyUtils.getProperty(bean, getName());
        } catch (IllegalAccessException e) {
            throw new JcrPersistenceInternalException(e);
        } catch (InvocationTargetException e) {
            throw new JcrPersistenceInternalException(e);
        } catch (NoSuchMethodException e) {
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
            PropertyUtils.setProperty(bean, getName(), value);
        } catch (IllegalAccessException e) {
            throw new JcrPersistenceInternalException(e);
        } catch (InvocationTargetException e) {
            throw new JcrPersistenceInternalException(e);
        } catch (NoSuchMethodException e) {
            throw new JcrPersistenceInternalException(e);
        }
    }
    
    public boolean isReadable() {
        PropertyDescriptor descriptor = getPropertyDescriptor();
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
    
    private PropertyDescriptor getPropertyDescriptor() {
        
        PropertyDescriptor result = null;
        
        for (PropertyDescriptor descriptor : 
            PropertyUtils.getPropertyDescriptors(getDeclaringClass())) {
            
            if (getName().equals(descriptor.getName())) {
                result = descriptor;
                break;
            }
        }
        return result;
    }
    
    public String toString() {
        return "property '" + getName() + 
               "' of class: " + getDeclaringClass().getSimpleName();
    }
}
