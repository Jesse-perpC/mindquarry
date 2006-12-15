/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.events;

import com.mindquarry.events.types.TestEvent;
import com.mindquarry.events.types.TestListener;
import com.mindquarry.events.types.TestSource;

import junit.framework.TestCase;

/**
 * Add summary documentation here.
 * 
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public class EventBrokerTest extends TestCase {
    public void testBroker() {
        TestSource source = new TestSource();
        Event event = new TestEvent(source, "a test event"); //$NON-NLS-1$

        EventListener listener = new TestListener();

        EventBroker broker = new EventBroker();
        broker.registerEvent(TestEvent.ID);
        broker.registerEventListener(listener, TestEvent.ID);
        broker.publishEvent(event, true);

        TestCase.assertTrue(event.isConsumed());
    }
}
