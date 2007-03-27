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

import com.mindquarry.persistence.jcr.Operations;
import com.mindquarry.persistence.jcr.Persistence;
import com.mindquarry.persistence.jcr.api.JcrSession;

/**
 * Add summary documentation here.
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public class CommandProcessor {

    private Persistence persistence_;
    
    public CommandProcessor(Persistence persistence) {
        persistence_ = persistence;
    }
    
    public Object process(JcrSession jcrSession, 
            Operations operation, Object... objects) {
        
        Command command = null;
        switch (operation) {
            case PERSIST : command = new PersistCommand(); break;
            case UPDATE : command = new WriteCommand(); break;
            case READ : command = new ReadCommand(); break;
            case DELETE : command = new DeleteCommand(); break;
            case QUERY : command = new QueryCommand(); break;
        }
        command.initialize(persistence_, objects);
        return command.execute(jcrSession);
    }
}
