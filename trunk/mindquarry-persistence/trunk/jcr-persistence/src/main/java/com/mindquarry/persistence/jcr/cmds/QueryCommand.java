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

import static com.mindquarry.persistence.jcr.Operations.READ;

import java.util.LinkedList;
import java.util.List;

import com.mindquarry.persistence.jcr.JcrNode;
import com.mindquarry.persistence.jcr.JcrSession;
import com.mindquarry.persistence.jcr.Persistence;
import com.mindquarry.persistence.jcr.query.QueryResolver;

/**
 * Add summary documentation here.
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public class QueryCommand implements Command {

    public static long queryDuration = 0l;
    
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
            result.add(processReadCommand(session, jcrNode));
        }
        return result;
    }
    
    private Object processReadCommand(JcrSession session, JcrNode jcrNode) {
        return getCommandManager().process(READ, session, jcrNode);
    }
    
    private CommandProcessor getCommandManager() {
        return persistence_.getCommandProcessor();
    }
    
    private Iterable<JcrNode> resolveQuery(JcrSession session) {
        
        long start = System.currentTimeMillis();
        
        QueryResolver queryResolver = persistence_.getQueryResolver();        
        Iterable<JcrNode> result = queryResolver.resolve(session, queryKey_, queryParameters_);
        
        long end = System.currentTimeMillis();
        queryDuration += (end - start);
        
        return result;
    }
}