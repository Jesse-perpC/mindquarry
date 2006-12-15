/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.events.types;

import com.mindquarry.events.EventBase;

/**
 * Add summary documentation here.
 * 
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public class TestEvent extends EventBase {
    public static final String ID= TestEvent.class.getName();
    
    public TestEvent(Object source, String message) {
        super(source, message);
    }

    /**
     * @see com.mindquarry.events.Event#getID()
     */
    public String getID() {
        return ID;
    }
}
