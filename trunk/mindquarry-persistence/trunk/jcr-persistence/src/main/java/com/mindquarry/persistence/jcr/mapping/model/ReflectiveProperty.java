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

import java.lang.reflect.InvocationTargetException;

import javax.jcr.Node;

import org.apache.commons.beanutils.PropertyUtils;

import com.mindquarry.persistence.jcr.JcrPersistenceInternalException;
import com.mindquarry.persistence.jcr.mapping.trafo.Transformer;

/**
 * Add summary documentation here.
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public class ReflectiveProperty implements Transformer {

    private String name_;
    private Transformer contentTransformer_;
    
    public ReflectiveProperty(String name) {
        name_ = name;
    }
    
    public String getName() {
        return name_;
    }
    
    public Transformer getContentTransformer() {
        return contentTransformer_;
    }
    
    public Object getValue(Object bean) {
        try {
            return PropertyUtils.getProperty(bean, name_);
        } catch (IllegalAccessException e) {
            throw new JcrPersistenceInternalException(e);
        } catch (InvocationTargetException e) {
            throw new JcrPersistenceInternalException(e);
        } catch (NoSuchMethodException e) {
            throw new JcrPersistenceInternalException(e);
        }
    }
    
    public void setValue(Object bean, Object value) {
        try {
            PropertyUtils.setProperty(bean, name_, value);
        } catch (IllegalAccessException e) {
            throw new JcrPersistenceInternalException(e);
        } catch (InvocationTargetException e) {
            throw new JcrPersistenceInternalException(e);
        } catch (NoSuchMethodException e) {
            throw new JcrPersistenceInternalException(e);
        }
    }

    public Object fromJcr(Node entityNode) {
        // TODO Auto-generated method stub
        return null;
    }

    public void toJcr(Object object, Node entityNode) {
        
    }
}
