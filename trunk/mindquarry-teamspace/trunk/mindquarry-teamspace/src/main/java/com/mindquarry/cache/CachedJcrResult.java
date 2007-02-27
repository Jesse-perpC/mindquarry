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
