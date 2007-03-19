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
package com.mindquarry.persistence.jcr.cmds;

import java.util.LinkedList;
import java.util.List;

import com.mindquarry.persistence.jcr.Command;
import com.mindquarry.persistence.jcr.Operations;
import com.mindquarry.persistence.jcr.Persistence;
import com.mindquarry.persistence.jcr.api.JcrNode;
import com.mindquarry.persistence.jcr.api.JcrSession;
import com.mindquarry.persistence.jcr.query.QueryResolver;

/**
 * Add summary documentation here.
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
class QueryCommand implements Command {

    private String queryKey_;
    private Object[] queryParameters_;
    
    private Persistence persistence_;
    
    public void initialize(Persistence persistence, final Object... objects) {
        queryKey_ = (String) objects[0];
        queryParameters_ = (Object[]) objects[1];
        persistence_ = persistence;
    }
    
    /**
     * @see com.mindquarry.persistence.jcr.mapping.Command#execute(javax.jcr.Session)
     */
    public Object execute(JcrSession session) {
        List<Object> result = new LinkedList<Object>();
        for (JcrNode jcrNode : resolveQuery(session)) {
            Command command = createReadCommand(jcrNode);
            result.add(command.execute(session));
        }
        return result;
    }
    
    private Command createReadCommand(JcrNode jcrNode) {
        return getCommandManager().createCommand(Operations.READ, jcrNode);
    }
    
    private CommandManager getCommandManager() {
        return persistence_.getCommandManager();
    }
    
    private Iterable<JcrNode> resolveQuery(JcrSession jcrSession) {
        QueryResolver queryResolver = persistence_.getQueryResolver();
        return queryResolver.resolve(jcrSession, queryKey_, queryParameters_);
    }
}