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
import com.mindquarry.persistence.jcr.model.EntityType;


/**
 * @author
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
class JcrEntityFolderNode {

    private JcrNode folderNode_;
    private EntityType entityType_;
    
    JcrEntityFolderNode(JcrNode folderNode, EntityType entityType) {
        folderNode_ = folderNode;
        entityType_ = entityType;
    }
    
    public JcrNode getEntityNode(String entityId) {
        return folderNode_.getNode(entityId);
    }
    
    public boolean hasEntityNode(String entityId) {
        return folderNode_.hasNode(entityId);
    }
    
    public JcrNode addEntityNode(String entityId) {
        JcrNode result;
        if (entityType_.asComposite())
            result = folderNode_.addNode(entityId, "nt:folder");
        else
            result = folderNode_.addNode(entityId, "nt:file");
        
        result.addMixin("mix:referenceable");        
        return result;
    }
}
