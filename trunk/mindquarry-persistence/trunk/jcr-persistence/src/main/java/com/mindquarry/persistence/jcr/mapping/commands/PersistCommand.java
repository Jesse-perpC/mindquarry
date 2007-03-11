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
package com.mindquarry.persistence.jcr.mapping.commands;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import com.mindquarry.persistence.jcr.JcrPersistenceInternalException;
import com.mindquarry.persistence.jcr.mapping.model.EntityClass;
import com.mindquarry.persistence.jcr.mapping.trafo.EntityTransformer;

/**
 * Add summary documentation here.
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
class PersistCommand extends CommandBase {

    private Object entity_;
    private EntityClass entityClazz_;
    
    public PersistCommand(Object entity, EntityClass entityClazz) {
        entity_ = entity;
        entityClazz_ = entityClazz;
    }
    
    /**
     * @see com.mindquarry.persistence.jcr.mapping.Command#execute(javax.jcr.Session)
     */
    public void execute(Session session) {
        try {
            Node rootNode = session.getRootNode();
            Node usersNode = rootNode.getNode("users");
            new EntityTransformer().toJcr(entity_, usersNode);
        } catch (PathNotFoundException e) {
            throw new JcrPersistenceInternalException(e);
        } catch (RepositoryException e) {
            throw new JcrPersistenceInternalException(e);
        }
    }

}