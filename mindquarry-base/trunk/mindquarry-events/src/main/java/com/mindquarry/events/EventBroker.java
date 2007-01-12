/*
 * Copyright (C) 2006-2007 MindQuarry GmbH, All Rights Reserved
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
package com.mindquarry.events;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import com.mindquarry.events.exception.EventAlreadyRegisteredException;
import com.mindquarry.events.exception.UnknownEventException;

/**
 * Local broker for inter application events.
 * 
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public class EventBroker {
    public static final String ROLE = EventBroker.class.getName();
    
    private HashMap<String, Collection<EventListener>> registeredListeners;

    public EventBroker() {
        registeredListeners = new HashMap<String, Collection<EventListener>>();
    }

    public void registerEvent(String id) throws EventAlreadyRegisteredException {
        if (registeredListeners.keySet().contains(id)) {
            throw new EventAlreadyRegisteredException(
                    "Event already registered."); //$NON-NLS-1$
        }
        registeredListeners.put(id, new ArrayList<EventListener>());
    }

    public void registerEventListener(EventListener listener, String id) {
        if (!registeredListeners.keySet().contains(id)) {
            // event was not registered previously, register it first
            registeredListeners.put(id, new ArrayList<EventListener>());
        }
        registeredListeners.get(id).add(listener);
    }

    public void publishEvent(final Event event, boolean block)
            throws UnknownEventException {
        if (block) {
            // deliver event synchronously
            deliverEvent(event);
        } else {
            // deliver event asynchronously
            Thread thread = new Thread(new Runnable() {
                public void run() {
                    try {
                        deliverEvent(event);
                    } catch (UnknownEventException e) {
                        // nothing to do
                        e.printStackTrace();
                    }
                }
            });
            thread.setDaemon(true);
            thread.start();
        }
    }

    private void deliverEvent(Event event) throws UnknownEventException {
        String id = event.getID();
        if (!registeredListeners.keySet().contains(id)) {
            throw new UnknownEventException("Unknown event type."); //$NON-NLS-1$
        }
        Collection<EventListener> listeners = registeredListeners.get(id);
        for (EventListener listener : listeners) {
            listener.onEvent(event);
        }
    }
}
