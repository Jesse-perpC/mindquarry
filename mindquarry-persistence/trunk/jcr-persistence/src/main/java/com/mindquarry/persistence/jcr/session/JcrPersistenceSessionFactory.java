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

import javax.jcr.Repository;

import com.mindquarry.common.persistence.Session;
import com.mindquarry.common.persistence.SessionFactory;
import com.mindquarry.persistence.jcr.cmds.CommandManager;

/**
 * Add summary documentation here.
 *
 * @author
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public class JcrPersistenceSessionFactory implements SessionFactory {

    private ThreadLocal<Session> currentSession_;
    
    private Repository repository_;
    private CommandManager commandManager_;
    
    public JcrPersistenceSessionFactory(CommandManager commandManager, 
            Repository repository) {
        
        currentSession_ = new ThreadLocal<Session>();
        repository_ = repository;
        commandManager_ = commandManager;
    }
    
    /**
     * @see com.mindquarry.common.persistence.SessionFactory#currentSession()
     */
    public Session currentSession() {
        if (currentSession_.get() == null) {
            currentSession_.set(buildJcrSession());
        }
        return currentSession_.get();
    }
    
    private Session buildJcrSession() {
        JcrPersistenceSession result = 
            new JcrPersistenceSession(commandManager_, repository_);
        result.initialize();
        return result;
    }
}
