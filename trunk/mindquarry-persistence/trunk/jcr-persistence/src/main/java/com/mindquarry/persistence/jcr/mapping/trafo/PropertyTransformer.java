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
package com.mindquarry.persistence.jcr.mapping.trafo;

import java.lang.reflect.Array;
import java.util.Collection;

import com.mindquarry.persistence.jcr.JcrPersistenceInternalException;
import com.mindquarry.persistence.jcr.api.JcrNode;
import com.mindquarry.persistence.jcr.mapping.model.Model;
import com.mindquarry.persistence.jcr.mapping.model.Property;

/**
 * Add summary documentation here.
 *
 * @author
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public class PropertyTransformer implements Transformer{

    private Property property_;    
    private Transformer contentTransformer_;
    
    public PropertyTransformer(Property property) {        
        property_ = property;
    }
    
    public void initialize() {
        
        Class<?> contentType = property_.getContentType();
        if (property_.hasIterableContentType())
            contentTransformer_ = new CollectionTransformer();
        else 
            contentTransformer_ = new StringTransformer(); 
        
        
    }
    
    public Object readFromJcr(JcrNode entityNode) {
        // TODO Auto-generated method stub
        return null;
    }

    public void writeToJcr(Object entity, JcrNode entityNode) {
        String propertyName = property_.getName();
        Object propertyValue = property_.getContent(entity);
        JcrNode propertyNode = entityNode.addNode(propertyName, "xt:element");        
        contentTransformer_.writeToJcr(propertyValue, propertyNode);
    }
}
