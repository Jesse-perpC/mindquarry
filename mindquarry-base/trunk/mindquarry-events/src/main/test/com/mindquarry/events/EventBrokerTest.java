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
package com.mindquarry.events;

import junit.framework.TestCase;

import com.mindquarry.events.types.TestEvent;
import com.mindquarry.events.types.TestListener;
import com.mindquarry.events.types.TestSource;

/**
 * Add summary documentation here.
 * 
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public class EventBrokerTest extends TestCase {
    public void testBroker() throws Exception {
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
