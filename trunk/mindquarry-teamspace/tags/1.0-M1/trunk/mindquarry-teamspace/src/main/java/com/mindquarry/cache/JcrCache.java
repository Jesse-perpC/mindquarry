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
package com.mindquarry.cache;

import java.io.IOException;

import org.apache.excalibur.store.Store;


/**
 * Add summary documentation here.
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public class JcrCache {
    
    private Store transientStore_;

    /**
     * Getter for transientStore.
     *
     * @return the transientStore
     */
    public Store getTransientStore() {
        return transientStore_;
    }

    /**
     * Setter for transientStore.
     *
     * @param transientStore the transientStore to set
     */
    public void setTransientStore(Store transientStore) {
        transientStore_ = transientStore;
    }
    
    public void putResultInCache(String cacheKey, Object jcrResult) {
        CachedJcrResult cachedJcrResult = new CachedJcrResult(jcrResult);
        try {
            transientStore_.store(cacheKey, cachedJcrResult);
        } catch (IOException e) {
            throw new RuntimeException("could not put entity in cache store", e);
        }
    }
    
    public Object resultFromCache(String cacheKey) {
        Object result = null;
        
        if (transientStore_.containsKey(cacheKey)) {            
            CachedJcrResult cachedJcrResult = 
                        (CachedJcrResult) transientStore_.get(cacheKey);            
            
            if (cachedJcrResult.isValid())
                result = cachedJcrResult.getEntity();
            else
                transientStore_.remove(cacheKey);
        }
        return result;
    }
    
    public void removeFromCache(String cacheKey) {
        if (transientStore_.containsKey(cacheKey))
            transientStore_.remove(cacheKey);
    }
}
