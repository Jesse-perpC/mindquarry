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
import static com.mindquarry.persistence.jcr.Operations.DELETE;
import static com.mindquarry.persistence.jcr.Operations.PERSIST;
import static com.mindquarry.persistence.jcr.Operations.QUERY;
import static com.mindquarry.persistence.jcr.Operations.UPDATE;

import java.util.List;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.query.QueryManager;

import com.mindquarry.persistence.jcr.cmds.CommandProcessor;

/**
 * Add summary documentation here.
 *
 * @author
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public class Session implements JcrSession,
    com.mindquarry.persistence.api.Session {

    private javax.jcr.Session jcrSession_;
    private CommandProcessor commandProcessor_;
    
    Session(Persistence persistence, javax.jcr.Session jcrSession) {
        jcrSession_ = jcrSession;
        commandProcessor_ = persistence.getCommandProcessor();
    }
    
    /**
     * @see com.mindquarry.common.persistence.Session#commit()
     */
    public void commit() {
        invoke("save", jcrSession_);
    }

    /**
     * @see com.mindquarry.common.persistence.Session#delete(java.lang.Object)
     */
    public boolean delete(Object entity) {
        processCommand(DELETE, entity);
        return true;
    }

    /**
     * @see com.mindquarry.common.persistence.Session#persist(java.lang.Object)
     */
    public void persist(Object entity) {
        processCommand(PERSIST, entity);
    }

    /**
     * @see com.mindquarry.common.persistence.Session#query(java.lang.String, java.lang.Object[])
     */
    public List<Object> query(String queryName, Object[] queryParameters) {
        Object result = processCommand(QUERY, queryName, queryParameters);
        return (List<Object>) result;
    }

    /**
     * @see com.mindquarry.common.persistence.Session#update(java.lang.Object)
     */
    public void update(Object entity) {
        processCommand(UPDATE, entity);
    }
    
    private Object processCommand(Operations operation, Object... objects) {
        return commandProcessor_.process(operation, this, objects);
    }
    
    // implementation of JcrSession interface methods
    
    public JcrNode getRootNode() {
        return new JcrNode((Node) invoke("getRootNode", jcrSession_), this);
    }
    
    public QueryManager getQueryManager() throws RepositoryException {
        return jcrSession_.getWorkspace().getQueryManager();
    }
}
