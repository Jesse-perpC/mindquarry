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

import com.mindquarry.persistence.jcr.Persistence;
import com.mindquarry.persistence.jcr.api.JcrNode;
import com.mindquarry.persistence.jcr.api.JcrSession;
import com.mindquarry.persistence.jcr.model.EntityType;
import com.mindquarry.persistence.jcr.trafo.TransformationManager;

/**
 * Add summary documentation here.
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
class DeleteCommand implements Command {

    private Object entity_;
    private Persistence persistence_;
    
    public void initialize(Persistence persistence, Object... objects) {
        entity_ = objects[0];
        persistence_ = persistence;
    }
    
    /**
     * @see com.mindquarry.persistence.jcr.mapping.Command#execute(javax.jcr.Session)
     */
    public Object execute(JcrSession session) {
        JcrNode folderNode = findEntityFolder(session);
        folderNode.getNode(id()).remove();
        return null;
    }
    
    private JcrNode findEntityFolder(JcrSession session) {
        JcrNode rootNode = session.getRootNode();
        return rootNode.getNode(entityFolderName());
    }

    private String id() {
        return entityType().idForEntity(entity_);
    }
    
    private EntityType entityType() {
        return getTransformationManager().entityType(entity_);
    }
    
    private String entityFolderName() {
        return getTransformationManager().entityFolderName(entity_);
    }
    
    private TransformationManager getTransformationManager() {
        return persistence_.getTransformationManager();
    }
}