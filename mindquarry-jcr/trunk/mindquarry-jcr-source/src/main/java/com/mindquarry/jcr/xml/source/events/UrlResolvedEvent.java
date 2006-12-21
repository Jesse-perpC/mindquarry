/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.jcr.xml.source.events;

import com.mindquarry.events.EventBase;

/**
 * Event for indication that an URL was resolved by the {@ JCRSourceFactory}.
 * 
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public class UrlResolvedEvent extends EventBase {
    public static final String ID = UrlResolvedEvent.class.getName();

    private String url;

    private boolean directResolved;

    public UrlResolvedEvent(Object source, String url, boolean directResolved) {
        super(source, "URL resolved: " + url);
        this.url = url;
        this.directResolved = directResolved;
    }

    /**
     * @see com.mindquarry.events.Event#getID()
     */
    public String getID() {
        return UrlResolvedEvent.class.getName();
    }

    /**
     * Getter for url of the indexed document.
     * 
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Returns true if the URL was resolved directly. If the URL was a query URL
     * it returns false.
     * 
     * @return the directResolved indicator
     */
    public boolean isDirectResolved() {
        return directResolved;
    }
}
