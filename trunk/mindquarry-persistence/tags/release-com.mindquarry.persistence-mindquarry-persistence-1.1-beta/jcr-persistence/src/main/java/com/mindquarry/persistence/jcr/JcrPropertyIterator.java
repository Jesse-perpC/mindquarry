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
package com.mindquarry.persistence.jcr;

import java.util.Iterator;

import javax.jcr.PropertyIterator;

/**
 * @author
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public class JcrPropertyIterator implements 
        Iterator<JcrProperty>, Iterable<JcrProperty> {
    
    private PropertyIterator propertyIterator_;
    private JcrSession session_;
    
    JcrPropertyIterator(PropertyIterator propertyIterator, JcrSession session) {
        session_ = session;
        propertyIterator_ = propertyIterator;
    }

    public JcrProperty nextProperty() {
        return new JcrProperty(propertyIterator_.nextProperty(), session_);
    }

    public long getPosition() {
        return propertyIterator_.getPosition();
    }

    public long getSize() {
        return propertyIterator_.getSize();
    }

    public void skip(long skipNum) {
        propertyIterator_.skip(skipNum);
    }

    public boolean hasNext() {
        return propertyIterator_.hasNext();
    }

    public JcrProperty next() {
        return nextProperty();
    }

    public void remove() {
        propertyIterator_.remove();
    }

    public Iterator<JcrProperty> iterator() {
        return this;
    }

}
