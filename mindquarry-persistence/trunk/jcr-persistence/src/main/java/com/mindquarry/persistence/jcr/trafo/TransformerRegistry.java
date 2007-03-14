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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.mindquarry.persistence.jcr.model.ModelException;

/**
 * Add summary documentation here.
 *
 * @author
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public class TransformerRegistry {

    private TransformationManager transformationManager_;
    
    public TransformerRegistry(TransformationManager transformationManager) {
        transformationManager_ = transformationManager;
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
                result = new CollectionTransformer(
                        collectionComponentType(type), collectionImpl);
            }
        }
        else if (type instanceof Class){
            Class<?> clazz = (Class<?>) type;
            if (clazz.isArray()) {
                result = new ArrayTransformer(clazz.getComponentType());
            }
            else if (clazz.equals(String.class)) {
                result = new StringTransformer();
            }
            else if (isPartOfModel(clazz)) {
                result = createReferenceTransformer(clazz);
            }
        }
        
        if (result == null) {
            throw new ModelException("the type: " + type + " is currently " +
                    "not supported by the jcr persistence layer ");
        }
        
        result.initialize(this);
        return result;
    }
    
    private Transformer createReferenceTransformer(Class<?> referenceesClazz) {
        return transformationManager_.createReferenceTransformer(referenceesClazz);
    }
    
    private boolean isPartOfModel(Class<?> clazz) {
        return transformationManager_.isPartOfModel(clazz);
    }
    
    private boolean isParameterizedCollectionType(Type type) {
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Class<?> clazz = (Class<?>) parameterizedType.getRawType();
            return Collection.class.isAssignableFrom(clazz);
        }
        return false;
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
        }
        return collectionImplementations_;
    }
}
