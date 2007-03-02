/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
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
