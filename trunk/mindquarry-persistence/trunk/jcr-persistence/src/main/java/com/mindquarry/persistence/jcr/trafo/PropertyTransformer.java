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
package com.mindquarry.persistence.jcr.trafo;

import java.lang.reflect.Type;

import com.mindquarry.persistence.jcr.api.JcrNode;
import com.mindquarry.persistence.jcr.model.Property;

/**
 * Add summary documentation here.
 *
 * @author
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public class PropertyTransformer implements Transformer {

    private Property property_;    
    private Transformer contentTransformer_;
    
    public PropertyTransformer(Property property) {        
        property_ = property;
    }
    
    public void initialize(TransformerRegistry transformerRegistry) {
        Type contentType = property_.getContentType();
        contentTransformer_ = 
            transformerRegistry.findContentTransformer(contentType);
    }
    
    public Object readFromJcr(JcrNode entityNode) {
        JcrNode propertyNode = entityNode.getNode(property_.getName());
        return contentTransformer_.readFromJcr(propertyNode);
    }

    public JcrNode writeToJcr(Object propertyValue, JcrNode entityNode) {
        String propertyName = property_.getName();
        
        JcrNode propertyNode;
        if (entityNode.hasNode(propertyName))
            propertyNode = entityNode.getNode(propertyName);
        else
            propertyNode = entityNode.addNode(propertyName, "xt:element");
        
        contentTransformer_.writeToJcr(propertyValue, propertyNode);
        
        return propertyNode;
    }
}
