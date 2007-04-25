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


/**
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public class EntityId  {

    private Property idProperty_;
    
    public EntityId(Property idProperty) {
        idProperty_ = idProperty;
    }
    
    public String getName() {
        return idProperty_.getName();
    }
    
    public String getValue(Object bean) {
        return idProperty_.getContent(bean).toString();
    }
    
    public void setValue(Object bean, String value) {
        idProperty_.setContent(bean, value);
    }
}
