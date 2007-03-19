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
package com.mindquarry.persistence.jcr.trafo;

import com.mindquarry.persistence.jcr.Command;
import com.mindquarry.persistence.jcr.Operations;
import com.mindquarry.persistence.jcr.Persistence;
import com.mindquarry.persistence.jcr.api.JcrNode;
import com.mindquarry.persistence.jcr.api.JcrSession;
import com.mindquarry.persistence.jcr.cmds.CommandManager;

/**
 * Add summary documentation here.
 *
 * @author
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
class ReferenceTransformer implements Transformer {
    
    private Persistence persistence_;
    
    ReferenceTransformer(Class<?> entityClazz, Persistence persistence) {
        persistence_ = persistence;
    }
    
    public void initialize(TransformerRegistry transformerRegistry) {}
    
    public Object readFromJcr(JcrNode propertyNode) {
        
        if (! propertyNode.hasProperty("reference")) {
            return null;
        }
        
        JcrNode entityNode = propertyNode.getProperty("reference").getNode();
        
        Command command = createCommand(Operations.READ, entityNode);
        return command.execute(propertyNode.getSession());
    }

    public JcrNode writeToJcr(Object entity, JcrNode propertyNode) {
        Operations operation;
        if (propertyNode.hasProperty("reference"))
            operation = Operations.UPDATE;
        else
            operation = Operations.PERSIST;
        
        JcrSession jcrSession = propertyNode.getSession();
        
        Command command = createCommand(operation, entity);
        JcrNode entityNode = (JcrNode) command.execute(jcrSession);
        propertyNode.setProperty("reference", entityNode);        
        
        return propertyNode;
    }
    
    private Command createCommand(Operations operation, Object... objects) {
        return getCommandManager().createCommand(operation, objects);
    }
    
    private CommandManager getCommandManager() {
        return persistence_.getCommandManager();
    }
}
