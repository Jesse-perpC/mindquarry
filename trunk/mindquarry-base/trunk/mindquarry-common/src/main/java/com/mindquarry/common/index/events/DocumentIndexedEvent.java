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
package com.mindquarry.common.index.events;

import com.mindquarry.events.EventBase;

/**
 * Add summary documentation here.
 * 
 * @author <a href="mailto:your-email-address">your full name</a>
 */
public class DocumentIndexedEvent extends EventBase {
    public static final String ID = DocumentIndexedEvent.class.getName();

    private String url;

    public DocumentIndexedEvent(Object source, String url) {
        super(source, "Document indexed with URL " + url);
        this.url = url;
    }

    /**
     * @see com.mindquarry.events.Event#getID()
     */
    public String getID() {
        return DocumentIndexedEvent.class.getName();
    }

    /**
     * Getter for url of the indexed document.
     * 
     * @return the url
     */
    public String getUrl() {
        return url;
    }

}
