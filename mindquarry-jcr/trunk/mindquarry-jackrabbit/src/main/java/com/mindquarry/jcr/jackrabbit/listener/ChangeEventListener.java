/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.jcr.jackrabbit.listener;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.RepositoryException;
import javax.jcr.observation.Event;
import javax.jcr.observation.EventIterator;
import javax.jcr.observation.EventListener;

import com.mindquarry.common.index.IndexClient;

/**
 * Add summary documentation here.
 * 
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public class ChangeEventListener implements EventListener {
    private IndexClient iClient;

    public ChangeEventListener(IndexClient iClient) {
        this.iClient = iClient;
    }

    /**
     * @see javax.jcr.observation.EventListener#onEvent(javax.jcr.observation.EventIterator)
     */
    public void onEvent(EventIterator ei) {
        List<String> changed = new ArrayList<String>();
        List<String> deleted = new ArrayList<String>();

        try {
            while (ei.hasNext()) {
                Event event = ei.nextEvent();

                if ((event.getType() == Event.PROPERTY_ADDED)
                        || (event.getType() == Event.PROPERTY_CHANGED)
                        || (event.getType() == Event.NODE_ADDED)) {
                    changed.add(event.getPath());
                } else {
                    deleted.add(event.getPath());
                }
            }
        } catch (RepositoryException re) {
            re.printStackTrace();
        }
        iClient.index(changed, deleted);
    }
}
