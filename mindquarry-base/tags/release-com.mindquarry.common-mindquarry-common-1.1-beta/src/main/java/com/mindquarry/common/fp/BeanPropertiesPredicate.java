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
package com.mindquarry.common.fp;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

/**
 * Add summary documentation here.
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public class BeanPropertiesPredicate implements UnaryPredicate<Object> {

    private Map<String, String> fieldPredicates_;
    
    public BeanPropertiesPredicate() {
        fieldPredicates_ = new HashMap<String, String>();
    }
    
    public BeanPropertiesPredicate(Map<String, String> fieldValues) {
        fieldPredicates_ = fieldValues;
    }
    
    public BeanPropertiesPredicate where(String field, String value) {
        fieldPredicates_.put(field, value);
        return this;
    }
    
    public boolean execute(Object object) {
        Map propertyMap = describe(object);
        for (String fieldName : fieldPredicates_.keySet()) {
            if (! propertyMap.containsKey(fieldName)) {
                return false;
            }
            String expected = fieldPredicates_.get(fieldName);
            String actual = (String) propertyMap.get(fieldName);
            if (actual == null) 
                return false;
            
            if (! actual.equals(expected)) 
                return false;
        }
        return true;
    }
    
    private Map describe(Object bean) {
        try {
            return BeanUtils.describe(bean);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
