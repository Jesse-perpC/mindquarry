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

import com.mindquarry.persistence.jcr.JcrNode;
import com.mindquarry.persistence.jcr.JcrProperty;
import com.mindquarry.persistence.jcr.JcrSession;
import com.mindquarry.persistence.jcr.Persistence;
import com.mindquarry.persistence.jcr.model.EntityType;
import com.mindquarry.persistence.jcr.model.Model;

/**
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
        JcrEntityFolderNode folderNode = findEntityFolder(session);
        JcrNode entityNode = folderNode.getEntityNode(entityId());        
        
        // properties of other entities may reference the currently processed
        // entity. To ensure referential integrity, we have to navigate back
        // to all referencing entities and remove the reference.
        for (JcrProperty property : entityNode.getReferences()) {
            property.getParent().remove();
        }
        
        entityNode.remove();
        return null;
    }
    
    private JcrEntityFolderNode findEntityFolder(JcrSession session) {
        JcrNode rootNode = session.getRootNode();        
        JcrNode folderNode = rootNode.getNode(parentFolderName());
        return new JcrEntityFolderNode(folderNode, entityType());
    }

    private String entityId() {
        return entityType().getId(entity_);
    }
    
    private String parentFolderName() {
        return entityType().parentFolder();
    }
    
    private EntityType entityType() {
        return getModel().findEntityType(entity_);
    }
    
    private Model getModel() {
        return persistence_.getModel();
    }
}