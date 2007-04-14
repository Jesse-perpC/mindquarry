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
import java.util.Map;
import java.util.Map.Entry;

import com.mindquarry.persistence.jcr.JcrNode;
import com.mindquarry.persistence.jcr.JcrNodeIterator;
import com.mindquarry.persistence.jcr.JcrPersistenceInternalException;
import com.mindquarry.persistence.jcr.model.ModelException;

/**
 * Add summary documentation here.
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
class ParametrizedMapTransformer implements Transformer {

    private Type keyType_;
    private Type valueType_;
    
    private Transformer keyTransformer_;
    private Transformer valueTransformer_;
    
    private Class<?> mapImplementation_;

    ParametrizedMapTransformer(Type keyType, Type valueType,
            Class<?> mapImplementation) {
        
        keyType_ = keyType;
        valueType_ = valueType;
        mapImplementation_ = mapImplementation;
    }
    
    public void initialize(TransformerRegistry registry) {
        keyTransformer_ = registry.findContentTransformer(keyType_);
        valueTransformer_ = registry.findContentTransformer(valueType_);
    }
    
    public Object readFromJcr(JcrNode jcrNode) {
        Map result = newMap();
        for (JcrNode entryNode : jcrNode.getNodes()) {
            // deletion of possibly referenced nodes can cause invalid
            // key or value nodes. if we detect such an invalid entry,
            // we will remove it
            if (entryNode.hasNode("key") && entryNode.hasNode("value")) {
                JcrNode keyNode = entryNode.getNode("key");
                Object key = keyTransformer_.readFromJcr(keyNode);
                    
                JcrNode valueNode = entryNode.getNode("value");
                Object value = valueTransformer_.readFromJcr(valueNode);
                
                result.put(key, value);
            }
            else {
                entryNode.remove();
            }
            
        }
        return result;
    }
    
    private Map newMap() {
        try {
            return (Map) mapImplementation_.newInstance();
        } catch (InstantiationException e) {
            throw new ModelException("error during transforming jcr entries " +
                    "into a map. could not create an instance of " +
                    "type: " + mapImplementation_);
        } catch (IllegalAccessException e) {
            throw new JcrPersistenceInternalException(e);
        }
    }

    public void writeToJcr(Object object, JcrNode jcrNode) {
        Map<Object, Object> map = (Map<Object, Object>) object;
        JcrNodeIterator collectionNodeIt = jcrNode.getNodes();
        
        for (Entry<Object, Object> entry : map.entrySet()) {
            
            JcrNode entryNode;
            if (collectionNodeIt.hasNext())
                entryNode = collectionNodeIt.next();
            else
                entryNode = jcrNode.addNode("entry", "xt:element");
            
            JcrNode keyNode = entryNode.addNode("key", "xt:element");
            keyTransformer_.writeToJcr(entry.getKey(), keyNode);
            
            JcrNode valueNode = entryNode.addNode("value", "xt:element");
            valueTransformer_.writeToJcr(entry.getValue(), valueNode);
        }
        
        // if there are currently more collection items 
        // within the jcr repository than now should be,
        // we have to remove the items at the end of list
        while (collectionNodeIt.hasNext()) {
            collectionNodeIt.next().remove();
        }
    }
}
