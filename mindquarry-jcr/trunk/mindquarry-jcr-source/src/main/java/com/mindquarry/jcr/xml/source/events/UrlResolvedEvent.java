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
        super(source, "URL resolved: " + url); //$NON-NLS-1$
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
