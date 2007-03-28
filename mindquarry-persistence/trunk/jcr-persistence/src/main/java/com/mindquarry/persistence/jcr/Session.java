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

import java.util.List;

import com.mindquarry.persistence.jcr.api.JcrSession;
import com.mindquarry.persistence.jcr.cmds.CommandProcessor;

/**
 * Add summary documentation here.
 *
 * @author
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
class Session implements com.mindquarry.persistence.api.Session {

    private JcrSession jcrSession_;
    private CommandProcessor commandProcessor_;
    
    Session(Persistence persistence, JcrSession jcrSession) {        
        jcrSession_ = jcrSession;
        commandProcessor_ = persistence.getCommandProcessor();
    }
    
    JcrSession jcrSession() {
        return jcrSession_;
    }
    
    private Object processCommand(Operations operation, Object... objects) {
        return commandProcessor_.process(jcrSession(), operation, objects);
    }
    
    /**
     * @see com.mindquarry.common.persistence.Session#commit()
     */
    public void commit() {
        jcrSession_.save();
    }

    /**
     * @see com.mindquarry.common.persistence.Session#delete(java.lang.Object)
     */
    public boolean delete(Object entity) {
        processCommand(Operations.DELETE, entity);
        return true;
    }

    /**
     * @see com.mindquarry.common.persistence.Session#persist(java.lang.Object)
     */
    public void persist(Object entity) {
        processCommand(Operations.PERSIST, entity);
    }

    /**
     * @see com.mindquarry.common.persistence.Session#query(java.lang.String, java.lang.Object[])
     */
    public List<Object> query(String queryName, Object[] queryParameters) {
        return (List<Object>) processCommand(
                Operations.QUERY, queryName, queryParameters); 
    }

    /**
     * @see com.mindquarry.common.persistence.Session#update(java.lang.Object)
     */
    public void update(Object entity) {
        processCommand(Operations.UPDATE, entity);
    }
}
