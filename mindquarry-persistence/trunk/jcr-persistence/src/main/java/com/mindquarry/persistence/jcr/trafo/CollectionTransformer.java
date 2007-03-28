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

import java.util.Collection;

import com.mindquarry.persistence.jcr.JcrPersistenceInternalException;
import com.mindquarry.persistence.jcr.api.JcrNode;
import com.mindquarry.persistence.jcr.api.JcrNodeIterator;
import com.mindquarry.persistence.jcr.model.ModelException;

/**
 * Add summary documentation here.
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
class CollectionTransformer implements Transformer {

    private TransformerRegistry transformerRegistry_;
    private Class<?> collectionImplementation_;

    CollectionTransformer(Class<?> collectionImplementation) {
        collectionImplementation_ = collectionImplementation;
    }
    
    public void initialize(TransformerRegistry transformerRegistry) {
        transformerRegistry_ = transformerRegistry;
    }
    
    public Object readFromJcr(JcrNode jcrNode) {
        Collection children = newComponentCollection();
        for (JcrNode itemNode : jcrNode.getNodes()) {
            // we need a wrapping item element because the content
            // of the item can also be a collection
            Object child = componentTransformer(itemNode).readFromJcr(itemNode);
            children.add(child);
        }
        return children;
    }
    
    private Collection newComponentCollection() {
        try {
            return (Collection) collectionImplementation_.newInstance();
        } catch (InstantiationException e) {
            throw new ModelException("error during transforming jcr entries " +
                    "into a collection. could not create an instance of " +
                    "type: " + collectionImplementation_);
        } catch (IllegalAccessException e) {
            throw new JcrPersistenceInternalException(e);
        }
    }

    public JcrNode writeToJcr(Object object, JcrNode jcrNode) {
        Collection collection = (Collection) object;
        JcrNodeIterator collectionNodeIt = jcrNode.getNodes();
        
        for (Object item : collection) {
            // we need a wrapping item element because the content
            // of the item can also be a collection
            
            JcrNode itemNode;
            if (collectionNodeIt.hasNext())
                itemNode = collectionNodeIt.next();
            else
                itemNode = jcrNode.addNode("item", "xt:element");
            
            componentTransformer(item).writeToJcr(item, itemNode);
        }
        
        // if there are currently more collection items 
        // within the jcr repository than now should be,
        // we have to remove the items at the end of list
        while (collectionNodeIt.hasNext()) {
            collectionNodeIt.next().remove();
        }
        
        return jcrNode;
    }
    
    private Transformer componentTransformer(Object item) {
        return transformerRegistry_.findContentTransformer(item.getClass());
    }
    
    private Transformer componentTransformer(JcrNode jcrNode) {
        return transformerRegistry_.findContentTransformerDynamically(jcrNode);
    }
}
