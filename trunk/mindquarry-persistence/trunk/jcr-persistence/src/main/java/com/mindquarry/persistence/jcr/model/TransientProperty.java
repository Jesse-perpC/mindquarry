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

/**
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public final class TransientProperty implements Property {

    private Field field_;
    
    public TransientProperty(Field field) {
        field_ = field;
    }
    
    public String getName() {
        return field_.getName();
    }
    
    public Type getContentType() {
        return field_.getGenericType();
    }
    
    private Class<?> getDeclaringClass() {
        return field_.getDeclaringClass();
    }
    
    public Object getContent(Object bean) {
        throw new UnsupportedOperationException();
    }
    
    public Iterable getIterableContent(Object bean) {
        throw new UnsupportedOperationException();
    }
    
    public void setContent(Object bean, Object value) {
        throw new UnsupportedOperationException();
    }
    
    public boolean isAccessible() {
        return false;
    }
    
    public String toString() {
        return "property '" + getName() + 
               "' of class: " + getDeclaringClass().getSimpleName();
    }
}
