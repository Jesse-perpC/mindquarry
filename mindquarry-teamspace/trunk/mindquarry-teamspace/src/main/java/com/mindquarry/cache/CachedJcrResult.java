/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.cache;


/**
 * Add summary documentation here.
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
class CachedJcrResult {
    
    static private long EXPIRE_DELAY = 30 * 60* 1000L; 
    
    private Object entity_;
    private long expires_;
    /**
     * @param entity
     * @param expires
     */
    public CachedJcrResult(Object entity) {
        entity_ = entity;
        expires_ = System.currentTimeMillis() + EXPIRE_DELAY;
    }
    
    boolean isValid() {
        return expires_ >= System.currentTimeMillis();
    }
    
    Object getEntity() {
        return entity_;
    }
}
