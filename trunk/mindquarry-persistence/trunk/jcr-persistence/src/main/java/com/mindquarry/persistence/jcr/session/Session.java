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
package com.mindquarry.persistence.jcr.session;

import java.util.LinkedList;
import java.util.List;

import com.mindquarry.persistence.jcr.Command;
import com.mindquarry.persistence.jcr.ModuleRegistry;
import com.mindquarry.persistence.jcr.Operations;
import com.mindquarry.persistence.jcr.api.JcrNode;
import com.mindquarry.persistence.jcr.api.JcrSession;
import com.mindquarry.persistence.jcr.cmds.CommandManager;

/**
 * Add summary documentation here.
 *
 * @author
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
class Session implements com.mindquarry.common.persistence.Session {

    private JcrSession jcrSession_;
    private CommandManager commandManager_;
    
    Session(ModuleRegistry moduleRegistry, JcrSession jcrSession) {
        
        jcrSession_ = jcrSession;
        commandManager_ = (CommandManager) 
                moduleRegistry.lookup(CommandManager.class);
    }
    
    JcrSession jcrSession() {
        return jcrSession_;
    }
    
    private Command createCommand(Operations operation, Object entity) {
        return commandManager_.createCommand(operation, entity);
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
        Command command = createCommand(Operations.DELETE, entity);
        command.execute(jcrSession_);
        return true;
    }

    /**
     * @see com.mindquarry.common.persistence.Session#persist(java.lang.Object)
     */
    public void persist(Object entity) {
        Command command = createCommand(Operations.PERSIST, entity);
        command.execute(jcrSession_);
    }

    /**
     * @see com.mindquarry.common.persistence.Session#query(java.lang.String, java.lang.Object[])
     */
    public List<Object> query(String arg0, Object[] arg1) {

        JcrNode userNode = jcrSession_.getRootNode().getNode("users").getNodes().nextNode();
        
        Command command = createCommand(Operations.READ, userNode);
        Object entity = command.execute(jcrSession_);
        
        List<Object> result = new LinkedList<Object>();
        result.add(entity);
        return result;
    }

    /**
     * @see com.mindquarry.common.persistence.Session#update(java.lang.Object)
     */
    public void update(Object entity) {
        Command command = createCommand(Operations.UPDATE, entity);
        command.execute(jcrSession_);
    }
}
