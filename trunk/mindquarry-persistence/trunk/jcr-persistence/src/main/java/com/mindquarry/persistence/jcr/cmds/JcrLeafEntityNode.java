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
class JcrLeafEntityNode extends JcrNode {

    private JcrLeafEntityNode(JcrNode entityNode) {
        super(entityNode.getWrappedNode(), entityNode.getSession());
    }

    public void remove() {
        for (JcrProperty property : getReferences()) {
            property.getParent().remove();
        }
        super.remove();
    }
    
    static JcrLeafEntityNode createNew(JcrNode folderNode, String entityId) {
        JcrNode entityNode = folderNode.addNode(entityId, "nt:file");
        entityNode.addMixin("mix:referenceable");
        return new JcrLeafEntityNode(entityNode);
    }
    
    static JcrLeafEntityNode load(JcrNode entityNode) {
        return new JcrLeafEntityNode(entityNode);
    }
}
