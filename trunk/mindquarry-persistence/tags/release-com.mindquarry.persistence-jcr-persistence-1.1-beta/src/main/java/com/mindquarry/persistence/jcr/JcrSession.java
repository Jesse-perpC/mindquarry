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
package com.mindquarry.persistence.jcr;

import static com.mindquarry.common.lang.ReflectionUtil.invoke;

import java.util.HashMap;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.query.QueryManager;

/**
 * acts as wrapper for javax.jcr.Session
 * and provides the following "jcr api" convenience methods
 * without checked exceptions 
 *
 * @author
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public class JcrSession {
    
    private Pool pool_;
    private javax.jcr.Session session_;
    private Map<String, Object> attributeMap_;
    
    JcrSession(javax.jcr.Session session, Pool pool) {
        pool_ = pool;
        session_ = session;        
        attributeMap_ = new HashMap<String, Object>();
    }
    
    /*
    public JcrNode getRootNode();
    
    public QueryManager getQueryManager() throws RepositoryException;
    
    public Pool getPool();
    
    public Object getAttribute(String key);
    
    public void setAttribute(String key, Object value);
    */
    
    public JcrNode getRootNode() {
        return new JcrNode((Node) invoke("getRootNode", session_), this);
    }
    
    public QueryManager getQueryManager() throws RepositoryException {
        return session_.getWorkspace().getQueryManager();
    }

    public Pool getPool() {
        return pool_;
    }

    public Object getAttribute(String key) {
        return attributeMap_.get(key);
    }

    public void setAttribute(String key, Object value) {
        attributeMap_.put(key, value);
    }
    
    void save() {
        invoke("save", session_);
    }
}
