/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.events;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Add summary documentation here.
 * 
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public class EventBroker {
    private HashMap<String, Collection<EventListener>> registeredListeners;

    public EventBroker() {
        registeredListeners = new HashMap<String, Collection<EventListener>>();
    }

    public void registerEvent(String id) {
        if (registeredListeners.keySet().contains(id)) {
            throw new RuntimeException("Event already registered."); //$NON-NLS-1$
        }
        registeredListeners.put(id, new ArrayList<EventListener>());
    }

    public void registerEventListener(EventListener listener, String id) {
        if (!registeredListeners.keySet().contains(id)) {
            throw new RuntimeException("Unknown event type."); //$NON-NLS-1$
        }
        registeredListeners.get(id).add(listener);
    }

    public void publishEvent(final Event event, boolean block) {
        if(block) {
            deliverEvent(event);
        } else {
            Thread thread = new Thread(new Runnable() {
                public void run() {
                    deliverEvent(event);
                }
            });
            thread.setDaemon(true);
            thread.start();
        }
    }

    private void deliverEvent(Event event) {
        String id = event.getID();
        if (!registeredListeners.keySet().contains(id)) {
            throw new RuntimeException("Unknown event type."); //$NON-NLS-1$
        }
        Collection<EventListener> listeners = registeredListeners.get(id);
        for(EventListener listener : listeners) {
            listener.onEvent(event);
        }
    }
}
