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

import com.mindquarry.persistence.jcr.Operations;
import com.mindquarry.persistence.jcr.Persistence;
import com.mindquarry.persistence.jcr.api.JcrNode;
import com.mindquarry.persistence.jcr.api.JcrSession;
import com.mindquarry.persistence.jcr.cmds.CommandProcessor;

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
        
        JcrSession session = propertyNode.getSession();
        JcrNode entityNode = propertyNode.getProperty("reference").getNode();
        
        return processCommand(session, Operations.READ, entityNode);
    }

    public void writeToJcr(Object entity, JcrNode propertyNode) {
        Operations operation;
        if (propertyNode.hasProperty("reference"))
            operation = Operations.UPDATE;
        else
            operation = Operations.PERSIST;
        
        JcrSession session = propertyNode.getSession();        
        
        Object entityNode = processCommand(session, operation, entity);
        propertyNode.setProperty("reference", (JcrNode) entityNode);
    }
    
    private Object processCommand(JcrSession session,
                    Operations operation, Object... objects) {
        
        return getCommandManager().process(session, operation, objects);
    }
    
    private CommandProcessor getCommandManager() {
        return persistence_.getCommandProcessor();
    }
}
