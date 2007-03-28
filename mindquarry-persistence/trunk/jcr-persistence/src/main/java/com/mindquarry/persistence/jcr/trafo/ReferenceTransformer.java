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

import static com.mindquarry.persistence.jcr.Operations.PERSIST_OR_UPDATE;
import static com.mindquarry.persistence.jcr.Operations.READ;

import com.mindquarry.persistence.jcr.JcrNode;
import com.mindquarry.persistence.jcr.Operations;
import com.mindquarry.persistence.jcr.Persistence;
import com.mindquarry.persistence.jcr.Session;
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
        
        Session session = propertyNode.getSession();
        JcrNode entityNode = propertyNode.getProperty("reference").getNode();
        
        return processCommand(READ, session, entityNode);
    }

    public void writeToJcr(Object entity, JcrNode propertyNode) {        
        
        Session session = propertyNode.getSession();
        
        Object entityNode = processCommand(PERSIST_OR_UPDATE, session, entity);
        propertyNode.setProperty("reference", (JcrNode) entityNode);
    }
    
    private Object processCommand(Operations operation, 
            Session session, Object... objects) {
        
        return getCommandProcessor().process(operation, session, objects);
    }
    
    private CommandProcessor getCommandProcessor() {
        return persistence_.getCommandProcessor();
    }
}
