/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.events;

/**
 * Common interface for event listeners. Event Listener can register itself with
 * the {@link EventBroker} for receiving events defined by an global event ID.
 * 
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public interface EventListener {
    /**
     * This method is invoked on a registered event listener if an event of the
     * type stated during registration was received.
     * 
     * @param event The event that was received.
     */
    public void onEvent(Event event);
}
