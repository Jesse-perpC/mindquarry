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
public class DocumentRemovedFromIndexEvent extends EventBase {
    public static final String ID = DocumentRemovedFromIndexEvent.class
            .getName();

    private String url;

    public DocumentRemovedFromIndexEvent(Object source, String url) {
        super(source, "Document with URL " + url + " removed from index.");
        this.url = url;
    }

    /**
     * @see com.mindquarry.events.Event#getID()
     */
    public String getID() {
        return DocumentRemovedFromIndexEvent.class.getName();
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
