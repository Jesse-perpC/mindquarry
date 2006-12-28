/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.events.types;

import com.mindquarry.events.Event;
import com.mindquarry.events.EventListener;

/**
 * Add summary documentation here.
 *
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public class TestListener implements EventListener {
    /**
     * @see com.mindquarry.events.EventListener#onEvent(java.lang.Object)
     */
    public void onEvent(Event event) {
        System.out.println("Received: " + event); //$NON-NLS-1$
        event.consume();
    }
}
