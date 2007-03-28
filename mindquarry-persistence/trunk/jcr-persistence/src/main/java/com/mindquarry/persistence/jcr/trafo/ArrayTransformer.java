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

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

import com.mindquarry.persistence.jcr.api.JcrNode;
import com.mindquarry.persistence.jcr.api.JcrNodeIterator;

/**
 * Add summary documentation here.
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
class ArrayTransformer implements Transformer {
    
    private Class<?> componentType_;
    private Transformer componentTransformer_;

    ArrayTransformer(Type componentType) {
        componentType_ = (Class<?>) componentType;
    }
    
    public void initialize(TransformerRegistry transformerRegistry) {
        componentTransformer_ = 
            transformerRegistry.findContentTransformer(componentType_);
    }
    
    public Object readFromJcr(JcrNode jcrNode) {
        List<Object> children = new LinkedList<Object>();
        for (JcrNode itemNode : jcrNode.getNodes()) {
            // we need a wrapping item element because the content
            // of the item can also be a collection
            Object child = componentTransformer_.readFromJcr(itemNode);
            children.add(child);
        }
        return children.toArray(newComponentArray(children.size()));
    }
    
    private Object[] newComponentArray(int size) {
        return (Object[]) Array.newInstance(componentType_, size);
    }

    public JcrNode writeToJcr(Object object, JcrNode jcrNode) {
        Object[] arrayContent = (Object[]) object;
        JcrNodeIterator collectionNodeIt = jcrNode.getNodes();
        
        for (Object item : arrayContent) {
            // we need a wrapping item element because the content
            // of the item can also be a collection
            JcrNode itemNode;
            if (collectionNodeIt.hasNext())
                itemNode = collectionNodeIt.next();
            else
                itemNode = jcrNode.addNode("item", "xt:element");
            
            componentTransformer_.writeToJcr(item, itemNode);
        }
        
        // if there are currently more collection items 
        // within the jcr repository than now should be,
        // we have to remove the items at the end of list
        while (collectionNodeIt.hasNext()) {
            collectionNodeIt.next().remove();
        }
        
        return jcrNode;
    }
}
