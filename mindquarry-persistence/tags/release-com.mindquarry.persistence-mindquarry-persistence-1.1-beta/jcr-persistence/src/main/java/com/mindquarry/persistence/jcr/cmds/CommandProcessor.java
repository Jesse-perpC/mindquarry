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

import static com.mindquarry.persistence.jcr.Operations.DELETE;
import static com.mindquarry.persistence.jcr.Operations.PERSIST;
import static com.mindquarry.persistence.jcr.Operations.PERSIST_OR_UPDATE;
import static com.mindquarry.persistence.jcr.Operations.QUERY;
import static com.mindquarry.persistence.jcr.Operations.READ;
import static com.mindquarry.persistence.jcr.Operations.UPDATE;

import java.util.HashMap;
import java.util.Map;

import com.mindquarry.persistence.jcr.JcrPersistenceInternalException;
import com.mindquarry.persistence.jcr.JcrSession;
import com.mindquarry.persistence.jcr.Operations;
import com.mindquarry.persistence.jcr.Persistence;

/**
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public class CommandProcessor {

    private Persistence persistence_;
    private Map<Operations, Class<? extends Command>> commandMap_;
    
    public CommandProcessor(Persistence persistence) {
        persistence_ = persistence;
        commandMap_ = new HashMap<Operations, Class<? extends Command>>();
        commandMap_.put(READ, ReadCommand.class);
        commandMap_.put(DELETE, DeleteCommand.class);
        commandMap_.put(QUERY, QueryCommand.class);
        commandMap_.put(UPDATE, WriteCommand.class);        
        commandMap_.put(PERSIST, PersistCommand.class);
        commandMap_.put(PERSIST_OR_UPDATE, PersistOrUpdateCommand.class);
    }
    
    public Object process(Operations operation, 
                JcrSession session, Object... objects) {
        
        Command command = createCommand(operation);
        command.initialize(persistence_, objects);
        return command.execute(session);   
    }
    
    private Command createCommand(Operations operation) {
        try {
            return commandMap_.get(operation).newInstance();
        } catch (InstantiationException e) {
            throw new JcrPersistenceInternalException(e);
        } catch (IllegalAccessException e) {
            throw new JcrPersistenceInternalException(e);
        }
    }
}
