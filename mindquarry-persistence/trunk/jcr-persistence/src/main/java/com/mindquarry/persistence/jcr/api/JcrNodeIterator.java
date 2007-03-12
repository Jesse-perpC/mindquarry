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
package com.mindquarry.persistence.jcr.api;

import javax.jcr.NodeIterator;

/**
 * Add summary documentation here.
 *
 * @author
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public class JcrNodeIterator {

    private NodeIterator nodeIterator_;
    
    public JcrNodeIterator(NodeIterator nodeIterator) {
        nodeIterator_ = nodeIterator;
    }

    public JcrNode nextNode() {
        return new JcrNode(nodeIterator_.nextNode());
    }

    public long getPosition() {
        return nodeIterator_.getPosition();
    }

    public long getSize() {
        return nodeIterator_.getSize();
    }

    public void skip(long skipNum) {
        nodeIterator_.skip(skipNum);
    }

    public boolean hasNext() {
        return nodeIterator_.hasNext();
    }

    public Object next() {
        return nodeIterator_.next();
    }

    public void remove() {
        nodeIterator_.remove();
    }
}
