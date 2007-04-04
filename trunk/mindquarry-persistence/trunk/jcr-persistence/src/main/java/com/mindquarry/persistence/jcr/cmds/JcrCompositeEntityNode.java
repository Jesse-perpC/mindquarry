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

/**
 * Add summary documentation here.
 *
 * @author
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
class JcrCompositeEntityNode extends JcrNode {
    
    private JcrCompositeEntityNode(JcrNode entityNode) {
        super(entityNode.getWrappedNode(), entityNode.getSession());
    }

    public void remove() {
        for (JcrProperty property : getReferences()) {
            property.getParent().remove();
        }
        super.remove();
    }
    
    static JcrCompositeEntityNode createNew(JcrNode folderNode, String entityId) {
        JcrNode composite = folderNode.addNode(entityId, "nt:folder");
        composite.addMixin("mix:referenceable");
        JcrNode entiyNode = composite.addNode(entityId+".xml", "nt:file");
        return new JcrCompositeEntityNode(entiyNode);
    }
    
    static JcrCompositeEntityNode load(JcrNode compositeNode) {
        String entityId = compositeNode.getName();
        JcrNode entityNode = compositeNode.getNode(entityId + ".xml");        
        return new JcrCompositeEntityNode(entityNode);
    }
}
