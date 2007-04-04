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

import static com.mindquarry.common.lang.ReflectionUtil.hasPublicDefaultConstructor;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mindquarry.persistence.jcr.JcrNode;
import com.mindquarry.persistence.jcr.Persistence;
import com.mindquarry.persistence.jcr.model.Model;
import com.mindquarry.persistence.jcr.model.ModelException;

/**
 * Add summary documentation here.
 *
 * @author
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
class TransformerRegistry {

    private Model model_;
    private TransformationManager transformationManager_;
    private Map<Class<?>, Transformer> referenceTransformers_;
    
    public TransformerRegistry(Persistence persistence) {
        model_= persistence.getModel();
        transformationManager_ = persistence.getTransformationManager();
        referenceTransformers_ = new HashMap<Class<?>, Transformer>();
    }
    
    public Transformer findContentTransformer(Type type) {
        
        Transformer result = null;
        
        if (type instanceof GenericArrayType) { 
            GenericArrayType arrayType = (GenericArrayType) type;
            result = new ArrayTransformer(arrayType.getGenericComponentType());
        }
        else if (isParameterizedCollectionType(type)) {
            Class<?> collectionImpl = findCollectionImplementation(type);
            if (collectionImpl != null) {
                result = new ParametrizedCollectionTransformer(
                        collectionComponentType(type), collectionImpl);
            }
        }
        else if (isCollectionType(type)) {
            result = new CollectionTransformer((Class<?>) type);
        }
        else if (isParameterizedMapType(type)) {
            result = new ParametrizedMapTransformer(
                    mapKeyType(type), mapValueType(type), HashMap.class);
        }
        else if (type instanceof Class){
            Class<?> clazz = (Class<?>) type;
            if (clazz.isArray()) {
                result = new ArrayTransformer(clazz.getComponentType());
            }
            else if (clazz.equals(String.class)) {
                result = new StringTransformer();
            }
            else if (getModel().containsClass(clazz)) {                
                result = findOrCreateReferenceTransformer(clazz);
            }
            else if (clazz.isInterface() &&
                    getModel().containsInterface(clazz)) {                
                
                Class<?> entityClazz = getModel().classForInterface(clazz);                
                result = findOrCreateReferenceTransformer(entityClazz);
            }
        }
        
        if (result == null) {
            throw new ModelException("the type: " + type + " is currently " +
                    "not supported by the jcr persistence layer ");
        }
        
        result.initialize(this);
        return result;
    }
    
    public Transformer findContentTransformerDynamically(JcrNode jcrNode) {
        
        Transformer result = null;
        
        if (jcrNode.hasProperty("reference")) {
            JcrNode entityNode = jcrNode.getProperty("reference").getNode();
            String parentName = entityNode.getParent().getName();
            if (isFolderForEntities(parentName)) {
                Class entityClass = entityClassByFolder(parentName);
                result = findOrCreateReferenceTransformer(entityClass);
            }
        }
        
        if (result == null) {
            String parentName = jcrNode.getParent().getName();
            result = transformationManager_
                .entityTransformerByFolder(parentName);
        }       
        
        if (result == null) {
            throw new ModelException("could not dynamically determine " +
                    "a proper transformer for the JCR node: " + jcrNode);
        }
        
        result.initialize(this);
        return result;
    }
    
    private boolean isFolderForEntities(String folder) {
        return null != transformationManager_.entityTypeByFolder(folder);
    }
    
    private Class entityClassByFolder(String folder) {
        return transformationManager_.entityTypeByFolder(folder).entityClazz();
    }
    
    private Model getModel() {
        return model_;
    }
    
    private Transformer findOrCreateReferenceTransformer(Class<?> clazz) {
        
        Transformer result;
        if (referenceTransformers_.containsKey(clazz)) {
            result = referenceTransformers_.get(clazz);
        }
        else {
            result = createReferenceTransformer(clazz);
            referenceTransformers_.put(clazz, result);
        }
        return result;
    }
    
    private Transformer createReferenceTransformer(Class<?> clazz) {
        return transformationManager_.createReferenceTransformer(clazz);
    }
    
    private boolean isParameterizedMapType(Type type) {
        if (type instanceof ParameterizedType) {
            return isMapType(((ParameterizedType) type).getRawType());
        }
        return false;
    }
    
    private boolean isMapType(Type type) {
        return type instanceof Class &&
            Map.class.isAssignableFrom((Class<?>) type);
    }
    
    private boolean isParameterizedCollectionType(Type type) {
        if (type instanceof ParameterizedType) {
            return isCollectionType(((ParameterizedType) type).getRawType());
        }
        return false;
    }
    
    private boolean isCollectionType(Type type) {
        return type instanceof Class &&
            Collection.class.isAssignableFrom((Class<?>) type);
    }
    
    private Type mapKeyType(Type type) {
        ParameterizedType parameterizedType = (ParameterizedType) type;
        return parameterizedType.getActualTypeArguments()[0];
    }  
    
    private Type mapValueType(Type type) {
        ParameterizedType parameterizedType = (ParameterizedType) type;
        return parameterizedType.getActualTypeArguments()[1];
    }
    
    private Type collectionComponentType(Type type) {
        ParameterizedType parameterizedType = (ParameterizedType) type;
        return parameterizedType.getActualTypeArguments()[0];
    }    
    
    private Class<?> findCollectionImplementation(Type type) {
        assert isParameterizedCollectionType(type);
        
        ParameterizedType parameterizedType = (ParameterizedType) type;
        Class<?> collectionClazz = (Class<?>) parameterizedType.getRawType();
        
        // the collection class may be astract or an interface
        // but if we want to create a collection from jcr entries,
        // we need a non abstract class  
        
        Class<?> result = null;
        // matches for supported interfaces and abstract classes
        if (collectionImplementations().containsKey(collectionClazz)) {
            result = collectionImplementations().get(collectionClazz);
        }
        else if (hasPublicDefaultConstructor(collectionClazz)) {
            result = collectionClazz;
        }        
        return result;
    }
    
    private Map<Class, Class> collectionImplementations_ = null;
    
    private Map<Class, Class> collectionImplementations() {
        if (collectionImplementations_ == null) {
            collectionImplementations_ = new HashMap<Class, Class>();
            collectionImplementations_.put(Collection.class, LinkedList.class);
            collectionImplementations_.put(List.class, LinkedList.class);
            collectionImplementations_.put(Set.class, HashSet.class);
        }
        return collectionImplementations_;
    }
}
