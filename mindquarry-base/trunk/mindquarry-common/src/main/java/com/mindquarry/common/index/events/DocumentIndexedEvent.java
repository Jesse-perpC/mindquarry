/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
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
