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

import com.mindquarry.common.persistence.PersistenceException;
import com.mindquarry.persistence.jcr.api.JcrNode;
import com.mindquarry.persistence.jcr.api.JcrSession;

/**
 * Add summary documentation here.
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
class PersistCommand extends WriteCommand {
    
    /**
     * @see com.mindquarry.persistence.jcr.mapping.Command#execute(javax.jcr.Session)
     */
    public Object execute(JcrSession session) {
        
        String entityId = entityId();
        JcrNode folderNode = findOrCreateEntityFolder(session);
        
        if (folderNode.hasNode(entityId)) {
            throw new PersistenceException("the entity: " + entity_ + 
                    " already exists.");
        }
        
        // here we only create the file jcr node 
        createEntityNode(folderNode, entityId); 
        // and WriteCommand.execute writes the object into this file node
        return super.execute(session);
    }
    
    private JcrNode findOrCreateEntityFolder(JcrSession session) {
        JcrNode rootNode = session.getRootNode();
        String name = entityFolderName();
        
        JcrNode result;
        if (rootNode.hasNode(name))
            result = rootNode.getNode(name);
        else
            result = rootNode.addNode(name, "nt:folder");
        
        return result;
    }
    
    private JcrNode createEntityNode(JcrNode folderNode, String entityId) {
        JcrNode entityNode = folderNode.addNode(entityId, "nt:file");
        entityNode.addMixin("mix:referenceable");
        return entityNode;
    }
}